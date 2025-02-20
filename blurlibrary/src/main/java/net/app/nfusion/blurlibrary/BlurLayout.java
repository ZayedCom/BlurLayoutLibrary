package net.app.nfusion.blurlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A custom ViewGroup that applies a blur effect to its children.
 * <p>
 * For Android S and above, hardware-accelerated blur (RenderEffect) is used.
 * For lower versions, a fallback box blur algorithm is applied.
 */
public class BlurLayout extends ViewGroup {
    // Offscreen bitmaps to capture the view hierarchy and store the blurred result.
    private Bitmap bitmap;
    private Bitmap blurredBitmap;

    // Canvas for drawing the view hierarchy into the offscreen bitmap.
    private Canvas canvas;

    // Handler for scheduling periodic blur updates on the main thread.
    private final Handler handler;

    // Desired frame rate (60 FPS) and corresponding delay between frames.
    private final int frameRate = 60;
    private final long frameDelay = 1000 / frameRate;

    // Blur effect radius.
    private float blurRadius = 50f;

    // Flag to indicate whether the blur effect is active.
    private boolean blurStatus = false;

    /**
     * Constructor for programmatically creating the layout.
     *
     * @param context The context in which this view is running.
     */
    public BlurLayout(Context context) {
        this(context, null);
    }

    /**
     * Constructor called when inflating the view from XML.
     *
     * @param context The context in which this view is running.
     * @param attrs   The attributes of the XML tag inflating the view.
     */
    public BlurLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false); // Ensure that dispatchDraw is called.
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Sets the radius for the blur effect and invalidates the view.
     *
     * @param radius The new blur radius.
     */
    public void setBlurRadius(float radius) {
        blurRadius = radius;
        invalidate();
    }

    /**
     * Activates the blur effect and starts periodic updates.
     */
    public void startBlur() {
        if (!blurStatus) {
            blurStatus = true;
            startBlurUpdate();
        }
    }

    /**
     * Deactivates the blur effect and stops scheduled updates.
     */
    public void stopBlur() {
        if (blurStatus) {
            blurStatus = false;
            handler.removeCallbacksAndMessages(null);
            invalidate(); // Force redraw without blur.
        }
    }

    /**
     * Schedules periodic blur updates to achieve an animated blur effect.
     */
    private void startBlurUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (blurStatus) {
                    invalidate();
                    handler.postDelayed(this, frameDelay);
                }
            }
        }, frameDelay);
    }

    /**
     * Ensures the offscreen bitmaps match the current view dimensions.
     */
    private void ensureBitmaps() {
        if (bitmap == null || blurredBitmap == null ||
                bitmap.getWidth() != getWidth() || bitmap.getHeight() != getHeight()) {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            blurredBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
    }

    /**
     * Applies a fallback box blur algorithm to the input bitmap.
     *
     * @param input  The source bitmap.
     * @param output The bitmap where the blurred image is written.
     */
    private void applyBlurFallback(Bitmap input, Bitmap output) {
        if (input == null || output == null) return;

        int width = input.getWidth();
        int height = input.getHeight();
        int[] pixels = new int[width * height];
        input.getPixels(pixels, 0, width, 0, 0, width, height);

        // Calculate a scaled blur radius for the fallback algorithm.
        int radius = (int) Math.min(25, Math.max(0, blurRadius * 0.25f));
        if (radius > 0) {
            int[] blurredPixels = boxBlur(pixels, width, height, radius);
            output.setPixels(blurredPixels, 0, width, 0, 0, width, height);
        } else {
            new Canvas(output).drawBitmap(input, 0, 0, null);
        }
    }

    /**
     * Performs a box blur on the pixel array.
     *
     * @param pixels The original pixel array.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @param radius The blur radius.
     * @return A new pixel array with the blur applied.
     */
    private int[] boxBlur(int[] pixels, int width, int height, int radius) {
        int[] output = pixels.clone();
        int div = radius * 2 + 1;

        // Horizontal blur pass.
        for (int y = 0; y < height; y++) {
            int sumR = 0, sumG = 0, sumB = 0;
            for (int i = -radius; i <= radius; i++) {
                int idx = y * width + Math.min(width - 1, Math.max(0, i));
                int color = pixels[idx];
                sumR += (color >> 16) & 0xFF;
                sumG += (color >> 8) & 0xFF;
                sumB += color & 0xFF;
            }
            for (int x = 0; x < width; x++) {
                output[y * width + x] = (0xFF << 24) |
                        ((sumR / div) << 16) |
                        ((sumG / div) << 8) |
                        (sumB / div);

                int removeIdx = y * width + Math.max(0, x - radius);
                int addIdx = y * width + Math.min(width - 1, x + radius);
                int removeColor = pixels[removeIdx];
                int addColor = pixels[addIdx];

                sumR += ((addColor >> 16) & 0xFF) - ((removeColor >> 16) & 0xFF);
                sumG += ((addColor >> 8) & 0xFF) - ((removeColor >> 8) & 0xFF);
                sumB += (addColor & 0xFF) - (removeColor & 0xFF);
            }
        }

        // Vertical blur pass.
        for (int x = 0; x < width; x++) {
            int sumR = 0, sumG = 0, sumB = 0;
            for (int i = -radius; i <= radius; i++) {
                int idx = Math.min(height - 1, Math.max(0, i)) * width + x;
                int color = output[idx];
                sumR += (color >> 16) & 0xFF;
                sumG += (color >> 8) & 0xFF;
                sumB += color & 0xFF;
            }
            for (int y = 0; y < height; y++) {
                pixels[y * width + x] = (0xFF << 24) |
                        ((sumR / div) << 16) |
                        ((sumG / div) << 8) |
                        (sumB / div);

                int removeIdx = Math.max(0, y - radius) * width + x;
                int addIdx = Math.min(height - 1, y + radius) * width + x;
                int removeColor = output[removeIdx];
                int addColor = output[addIdx];

                sumR += ((addColor >> 16) & 0xFF) - ((removeColor >> 16) & 0xFF);
                sumG += ((addColor >> 8) & 0xFF) - ((removeColor >> 8) & 0xFF);
                sumB += (addColor & 0xFF) - (removeColor & 0xFF);
            }
        }
        return pixels;
    }

    /**
     * Draws the view hierarchy with an optional blur effect.
     * <p>
     * Uses hardware-accelerated blur on Android S and above. For earlier versions,
     * captures the drawing into an offscreen bitmap and applies a fallback blur.
     *
     * @param canvas The canvas on which to draw.
     */
    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        if (getWidth() == 0 || getHeight() == 0) {
            super.dispatchDraw(canvas);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Use RenderEffect for hardware-accelerated blur.
            if (blurStatus) {
                float scaledRadius = blurRadius * 0.25f;
                RenderEffect blurEffect = RenderEffect.createBlurEffect(scaledRadius, scaledRadius, Shader.TileMode.CLAMP);
                setRenderEffect(blurEffect);
            } else {
                setRenderEffect(null);
            }
            super.dispatchDraw(canvas);
        } else {
            ensureBitmaps();

            if (this.canvas == null) {
                this.canvas = new Canvas(bitmap);
            }

            // Draw the view hierarchy into the offscreen bitmap.
            this.canvas.save();
            this.canvas.translate(-getLeft(), -getTop());
            super.dispatchDraw(this.canvas);
            this.canvas.restore();

            // Apply the fallback blur if active.
            if (blurStatus) {
                applyBlurFallback(bitmap, blurredBitmap);
                canvas.drawBitmap(blurredBitmap, 0, 0, null);
            } else {
                canvas.drawBitmap(bitmap, 0, 0, null);
            }
        }
    }

    /**
     * Positions the child views in the center of this layout.
     *
     * @param changed Indicates if the layout bounds have changed.
     * @param l       Left position.
     * @param t       Top position.
     * @param r       Right position.
     * @param b       Bottom position.
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                // Center each child within the layout.
                int left = (getWidth() - childWidth) / 2;
                int top = (getHeight() - childHeight) / 2;
                child.layout(left, top, left + childWidth, top + childHeight);
            }
        }
    }

    /**
     * Measures the layout and its child views.
     *
     * @param widthMeasureSpec  Horizontal space requirements as imposed by the parent.
     * @param heightMeasureSpec Vertical space requirements as imposed by the parent.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}

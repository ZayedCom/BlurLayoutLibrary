package net.app.nfusion.blurlayoutlibrary;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import net.app.nfusion.blurlibrary.BlurLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BlurLayout blurLayout = findViewById(R.id.blur_layout);
        ImageView background = findViewById(R.id.background);
        ToggleButton blurToggleButton = findViewById(R.id.blur_button);
        SeekBar blurSeekBar = findViewById(R.id.seekBar);
        Button nextButton = findViewById(R.id.next_button);
        Button previousButton = findViewById(R.id.previous_button);

        // Load default image
        Glide.with(this)
                .load(R.drawable.istanbul)
                .into(background);

        // Set the ToggleButton to control blur
        blurToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                blurLayout.startBlur();
            } else {
                blurLayout.stopBlur();
            }
        });

        blurSeekBar.setMax(99); // Max progress is 99, so the range becomes 1 to 100
        blurSeekBar.setProgress(50 - 1); // Default value (e.g., 50) minus 1 since we are adding 1 later
        blurSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float blurRadius = progress + 1; // Add 1 to make the range 1-100
                blurLayout.setBlurRadius(blurRadius); // Update blur radius
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Add behavior when user starts interacting
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Add behavior when user stops interacting
            }
        });

        // Set nextButton listener
        nextButton.setOnClickListener(v -> {
            // Load and loop the GIF
            Glide.with(this)
                    .asGif()
                    .load(R.drawable.android)
                    .into(background);
        });

        // Set previousButton listener
        previousButton.setOnClickListener(v -> {
            // Load default image
            Glide.with(this)
                    .load(R.drawable.istanbul)
                    .into(background);
        });
    }
}

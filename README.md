# BlurLayout Library

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.png" alt="App Icon" width="250" />
</p>

BlurLayout is a modern Android library that delivers smooth, real-time blur effects to your application's interface. Designed for simplicity and performance, it supports devices running Android 5.0 (API 21) and above. On devices with Android 12 (API 31) and later, it leverages the latest hardware-accelerated blur technology, while efficiently falling back to a custom blur algorithm on older devices.

---

## Overview

Enhance your app's visual appeal by adding dynamic blur effects to your UI. BlurLayout provides an easy-to-use solution with minimal configuration, allowing you to create a sophisticated look without complex setup. Whether you’re targeting the newest devices or supporting older versions of Android, BlurLayout ensures a consistent and appealing effect across your user base.

---

## Key Features

- **Wide Compatibility:**  Uses hardware-accelerated blur on Android 12 (API 31) and newer and employs an efficient fallback on devices running Android 5.0 (API 21) and above.
- **Effortless Integration:** Quickly integrate and control blur effects with a straightforward API.
- **Customizable Intensity:** Easily adjust the blur radius to suit your design needs.

---

## Quick Start

### 1. Add BlurLayout to Your Project

Include the BlurLayout library in your project as a dependency.

### 2. Integrate in Your XML Layout

Wrap the background content you want to blur within the BlurLayout. For example:

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- BlurLayout with background content -->
    <net.app.nfusion.blurlibrary.BlurLayout
        android:id="@+id/blur_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <!-- Any content here will be blurred -->

        <ImageView
            android:id="@+id/background"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:contentDescription="The background image to blur"
            android:scaleType="centerCrop"
            tools:ignore="HardcodedText" />
    </net.app.nfusion.blurlibrary.BlurLayout>

</FrameLayout>
```

### 3. Control the Blur in Your Code

Adjust the blur radius and start the effect programmatically:

```java
BlurLayout blurLayout = findViewById(R.id.blur_layout);
blurLayout.setBlurRadius(50f); // Set desired blur intensity
blurLayout.startBlur();        // Activate the blur effect

// To disable the blur effect later:
// blurLayout.stopBlur();
```

---

BlurLayout offers a powerful yet straightforward solution for adding a professional touch to your app’s design. Enjoy enhanced aesthetics and smooth performance with minimal effort.

Below is a demstration of the library being used on UI elements to blur the background.

<p align="center">
  <img src="blur 01.gif" alt="Blur effect applied on still image file" width="45%" style="margin-right: 10px;">
  <img src="blur 02.gif" alt="Blur effect applied on animatied image file" width="45%">
</p>


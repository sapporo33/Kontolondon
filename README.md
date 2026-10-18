# Emberwing Merge Assistant — prototype 0.1

This is an Android Studio Kotlin project for a **manual-queue drag helper**.
It is not a fully autonomous egg recognizer. The user enters each source and destination cell.

## Build
1. Open this folder in Android Studio (Giraffe or newer recommended).
2. Let Gradle sync; Android SDK 35 and JDK 17 are expected.
3. Build > Build APK(s).
4. Install the debug APK on your own Android device.

## First test
1. Open the app and tap "Enable Accessibility service".
2. In Android Accessibility settings, enable "Emberwing drag helper".
3. Return to the app.
4. Confirm the board bounds and set queue to exactly one drag, e.g. `4,2,4,3`.
5. Tap "Send drag queue, then return to Telegram", then quickly bring the Emberwing Hatchery board to the foreground if needed.
6. Verify the gesture lands on the intended cells before adding more moves.

## Coordinates
The defaults are only estimates based on one screenshot. Your screenshot resolution, display scaling, status bar, Telegram header and board position may differ.
`left, top, right, bottom` are the outer grid bounds in physical screen pixels; the code divides the area evenly by row/column count. Measure carefully and test with one drag.

## Queue format
One line per drag: `sourceRow,sourceCol,destinationRow,destinationCol`, 1-based.
Example:
```
4,2,4,3
5,3,4,3
```
These are examples only, not guaranteed valid moves.

## Important limitations
- No image recognition, egg-level detection, merge planning, or automatic Lv 14 verification yet.
- Queue moves are fixed in advance; a merge can change the board and invalidate later moves.
- Some games may reject automation or treat it as a rule violation. Check the game's terms and stop if automation is prohibited.
- Accessibility can perform gestures but does not grant screen-capture permission. Full auto-detection would need a separate MediaProjection capture consent flow and a vision/classification layer.
- Use only on your own device and account.
## Build APK in GitHub Actions (no local Android SDK needed)
1. Create a new GitHub repository in your browser. Prefer **Private** because it contains your source project.
2. Upload the contents of this project folder to the repository (the `.github/workflows/android.yml` file must be included).
3. Open the repository's **Actions** tab and select **Build Emberwing Merge Assistant APK**.
4. Press **Run workflow** and wait for the run to finish successfully.
5. Open the completed run and download the artifact named `emberwing-merge-assistant-debug-apk`.
6. Extract the artifact ZIP; it contains `app-debug.apk`. Install it only if you trust the source and have reviewed the code.

Note: GitHub Actions availability and usage limits depend on the account and repository settings. This workflow builds a debug APK; it is not a Play Store release. It does not add egg recognition or automatic Lv 14 strategy.

# SafeScan
 
**SafeScan** is an Android security application that scans your installed apps for potential threats, excessive resource usage, and suspicious behaviour giving you a clear, real-time picture of your device's health.

---

## Overview
 
Modern Android devices can accumulate dozens of apps over time, many of which run silently in the background, consume excessive data, or request far more permissions than they need. SafeScan cuts through the noise by analysing every installed app and surfacing the ones that may be putting your privacy or performance at risk.
 
---

# Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android device or emulator running API 23+
### Installation
 
1. **Clone the repository**
   ```bash
   git clone https://github.com/emma8047/SafeScanMobile.git
   cd SafeScanMobile
   ```
 
2. **Open in Android Studio**
   - Select **File → Open** and navigate to the cloned folder
3. **Build and run**
   - Connect a device or start an emulator
   - Click **Run ▶** 
4. **Grant permissions**
   - On first launch, follow the in-app prompt to enable **Usage Access** in system settings
---

If everything is set up correctly, you should see your new app running in the Android Emulator or your connected device.

## Project Structure
 
```
com.emma8047.safescan/
├── AppInfo.kt           # Data model for app metadata and safety info
├── DeviceScanner.kt     # Core scanning logic, scoring, and network analysis
├── ThreatManager.kt     # Manages scan state and bridges scanner to UI
├── ScanScreen.kt        # Composable UI for the scan flow and progress animation
└── Dashboard.kt         # Home screen displaying device score and flagged apps
```
 
---

## License
 
This project is licensed under the MIT License. See [`LICENSE`](LICENSE) for details.
 
---

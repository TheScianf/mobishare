## Prerequisites

- Node.js >= 18
- JDK 17, Android SDK / emulator configured
- npm packages installed (`npm install`)

## Step 1: Start Metro

First, start **Metro**, the JavaScript build tool for React Native.

To start the Metro server, run the following command from the root of your React Native project:

```sh
# Using npm
npm start

# Or using Yarn
yarn start
```

## Step 2: Install deps, build, and run

Install dependencies from the root of the `MobileApp` folder:

```sh
npm install
```

With Metro running, open a new terminal window/tab from the root of the React Native project and run one of the following to build and run your Android or iOS app:

### Android

```sh
# Using npm
npm run android

# Or using Yarn
yarn android
```

If everything is set up correctly, you should see your new app running in the Android emulator or on your connected device.

This is one way to run your app — you can also build it directly from Android Studio.

# Mobishare App Structure

- [Contexts](#contexts)
- [Folder Structure](#folder-structure)
- [Navigation](#navigation)
- [Main Screens](#main-screens)

Mobishare is a mobile app built with **React Native**, designed to provide a vehicle sharing service (bike/scooter sharing). The app is organized into clear modules to enable smooth navigation and a clean separation of concerns between components.

______________________________________________________________________

<!-- Folder diagram removed: not present in repo -->

## Folder Structure

- **Navigation**: includes the stack and tab navigators that define the app’s navigation logic.
- **Requests**: modules for API calls to the Kotlin backend (business logic) and Python backend (AI services).
- **Screens**: organized in subfolders and contains the app pages (screens).
- **Styles**: styles defined with `StyleSheet`/styled-components in TypeScript.

______________________________________________________________________

## Contexts

The app uses React’s **Context API** to manage global state:

- **AuthContext**: handles user authentication. It tracks credentials (token, username, role) and exposes them to any component in the app.

______________________________________________________________________

## Navigation

Mobishare uses a **hybrid** navigation system composed of:

- **Stack Navigator**: for navigation between main screens (e.g., Login, Registration, Home, Top-up, History, etc.).
- **Tab Navigator**: visible on the home screen after authentication, provides quick access to sections like *Home*, *Wallet*, *Profile*, and *Maintenance*.

______________________________________________________________________

## Main Screens

- **Login / Registration**: allow users to sign in or create a new account.
- **Face Recognition**: enables an additional layer of biometric security.
- **HomeScreen**: shows the map with all available parking spots and allows viewing or starting a ride.
- **Ride**: manages starting a ride by scanning the vehicle’s QR code.
- **Wallet**: shows available credit, accumulated *green points*, and allows top-ups.
- **Profile**: contains personal user information and access to face recognition settings.
- **Maintenance**: reserved for operators; shows parking and vehicle status with AI-based suggestions to identify bikes that need attention.

______________________________________________________________________

This modular architecture ensures **scalability**, **clarity**, and **maintainability** of the codebase while providing a simple and intuitive user experience for both customers and service operators.

## Tests and lint

```sh
npm test      # Jest
npm run lint  # ESLint
```

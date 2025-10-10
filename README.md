# 📝 NoteApp
A simple note-taking app for Android built with Jetpack Compose and Firebase.

## Features
✅ **Current Features**
- Create and edit notes
- View all notes in a normal list view or grid view
- Delete individual notes
- Delete all notes
- Shimmer loading animations
- Search notes

🚧 **Coming Soon**
- Auto-save notes

## Tech Stack
- **Kotlin** - Programming language
- **Jetpack Compose** - Modern UI toolkit
- **MVVM** - Architecture pattern
- **Firebase Firestore** - Cloud sync
- **Dagger 2** - Dependency injection
- **Kotlin Flow** - Reactive programming
- **Detekt** - Code quality

## Project Structure
```
NoteApp/
└─ app/
│  └─ src/
│     └─ main/
│        └─ java/
│           └─ com/
│              └─ noteapp/
│                 ├─ MainActivity.kt      # Entry Activity
│                 ├─ NoteApp.kt
│                 ├─ data/                # Data layer (implementations)
│                 │  ├─ local/            # Local storage (Room)
│                 │  └─ repository/       # Repo impls calling sources
│                 ├─ di/                  # Dependency injection
│                 ├─ domain/              # Domain layer (pure Kotlin)
│                 │  └─ model/            # Data classes
│                 ├─ presentation/        # UI layer (Compose)
│                 │  ├─ navigation/       # Nav graph/routes/destinations
│                 │  ├─ ui/               # UI components and screens
│                 │  │  ├─ component/     # Reusable composables (Shimmer loader, Confirm dialog)
│                 │  │  └─ screen/        # Feature screens (list/detail/edit)
│                 │  └─ viewmodel/        # ViewModels/state/event handlers
│                 ├─ preview/             # Fake datas for previews
│                 └─ util/                # Extensions, helpers, constants
└─ config/
   └─ detekt/
      └─ detekt.yml                        # Static analysis rules for Kotlin

```

## Getting Started
1. Clone the repository
2. Open in Android Studio
3. Add your `google-services.json` file
4. Build and run

## Demo
[https://github.com/user-attachments/assets/73ed3912-2794-4092-9a02-307bbd549d75](https://github.com/user-attachments/assets/d46cd6f3-51dc-4c57-bc63-aebfd2256fc7)
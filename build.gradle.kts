
//Let me break this down step-by-step in simple terms:
//
//### **What is This Code?**
//This is the **plugins block** in a Gradle build file (`build.gradle.kts`). It declares and configures the plugins you want to use in your Android project.
//
//---
//
//### **What Does Each Line Mean?**
//
//#### **1. `plugins` Block**
//```kotlin
//plugins {
//    alias(libs.plugins.android.application) apply false
//    ...
//}
//```
//- The `plugins` block is where you declare plugins that add features to your project (e.g., building Android apps, Kotlin support).
//- Instead of writing plugin IDs and versions directly, it uses **aliases** defined in the `libs.versions.toml` file.
//
//---
//
//#### **2. `alias(libs.plugins.X)`**
//- `alias` points to a plugin defined in your `libs.versions.toml`.
//- Example from your `libs.versions.toml`:
//```toml
//[plugins]
//android-application = { id = "com.android.application", version.ref = "agp" }
//kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
//```
//
//So, `libs.plugins.android.application` translates to:
//```kotlin
//id("com.android.application") version "8.8.1" // (from "agp" in `libs.versions.toml`)
//```
//
//---
//
//#### **3. `apply false`**
//- This means the plugin is **declared here but not applied** to this particular module.
//- **Why?**
//- You may want to apply the plugin in specific modules, like the `app` module.
//- Declaring it here makes it available to other modules but does not activate it in the current module.
//
//---
//
//### **Line-by-Line Breakdown**
//
//#### **1. `alias(libs.plugins.android.application) apply false`**
//- Declares the **Android Application plugin**, which is used to build Android apps.
//- The `apply false` means this plugin is not applied here but can be used in another module (e.g., `app/build.gradle.kts`).
//
//---
//
//#### **2. `alias(libs.plugins.kotlin.android) apply false`**
//- Declares the **Kotlin Android plugin** to enable Kotlin support for Android.
//- Again, `apply false` defers applying it to specific modules.
//
//---
//
//#### **3. `alias(libs.plugins.kotlin.compose) apply false`**
//- Declares the **Kotlin Compose plugin**, which is used for Jetpack Compose (UI toolkit for Android).
//- It enables Compose features, such as using `@Composable` functions.
//
//---
//
//#### **4. `alias(libs.plugins.dagger.hilt) apply false`**
//- Declares the **Hilt plugin**, which simplifies dependency injection in Android.
//- It works with `@HiltAndroidApp`, `@Inject`, etc.
//
//---
//
//#### **5. `alias(libs.plugins.ksp) apply false`**
//- Declares the **KSP plugin**, which processes annotations at compile time.
//- Used for libraries like Room and Hilt for generating code (e.g., DAOs, dependency injection code).
//
//---
//
//#### **6. `alias(libs.plugins.kotlin.serialization) apply false`**
//- Declares the **Kotlin Serialization plugin**, which enables support for serializing and deserializing Kotlin objects (e.g., JSON parsing).
//
//---
//
//### **How It All Fits Together**
//
//1. **Declaration in `libs.versions.toml`:**
//- Each plugin alias refers to its actual plugin ID and version:
//```toml
//[plugins]
//android-application = { id = "com.android.application", version.ref = "agp" }
//kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
//kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
//dagger-hilt = { id = "com.google.dagger.hilt.android", version.ref = "hiltAndroid" }
//ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
//kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
//```
//
//2. **Available to Modules:**
//- Declaring plugins here makes them available to all modules in the project (e.g., `app`, `feature`).
//
//3. **Applied Where Needed:**
//- The `apply false` allows plugins to be selectively applied in module-level `build.gradle.kts` files.
//- Example in `app/build.gradle.kts`:
//```kotlin
//plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
//}
//```
//
//---
//
//### **In Simple Terms**
//This block does two things:
//1. Makes plugins (like Android, Kotlin, Hilt) available to your project.
//2. Prevents applying them immediately in this file (`apply false`), so they can be applied where needed.
//
//By using `libs.versions.toml`, it keeps plugin versions centralized and consistent across modules.
//
//Let me know if you'd like me to explain further! 😊


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
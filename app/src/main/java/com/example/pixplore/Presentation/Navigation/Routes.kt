package com.example.pixplore.Presentation.Navigation
import kotlinx.serialization.Serializable


/*What’s a Sealed Class?
----------------------------
Imagine you are making a food delivery app. In the app, there are only a few fixed types of screens a user can go to, like:

Home Screen
Search Screen
Profile Screen
Order Details

Now, you want to represent these screens in your code. A sealed class is like saying:
"Hey, there are only these specific screens in my app, and no one can add more types without permission."

It gives a clear list of allowed types (like "Home", "Search", "Profile") and ensures nothing outside this list sneaks in.

You go to a restaurant, and they give you a fixed menu. You can only order from the given options:

Pizza
Pasta
Burger
A sealed class is like this fixed menu. You can’t order something outside the menu (like sushi). It restricts your choices to what is defined.

A sealed class allows you to create a class hierarchy where the set of subclasses is restricted and known at compile time.
Subclasses of a sealed class must be defined in the same file.
You can define them as class, object, or data class.
*/

/*
data object
---------------
A data object is a combination of:

The singleton nature of an object (only one instance of it can exist).
The features of a data class, like structural equality, a generated toString(), and more.
It’s like saying, “I need a single instance that behaves like a data class.”

Key properties of data object:

It's not a full-fledged class, but Kotlin generates methods like toString(), equals(), and hashCode() for it.
You don’t need to create a new instance (like HomeScreen()); instead, you can directly use the object (HomeScreen).

difference between data object  or data class --> You can create multiple instances of a data class(data class).-----  A singleton: only one instance is created.(object)
* */
@Serializable
sealed class Routes {
    @Serializable
    // we are using @Serialization annotation because the navigation library under the hood will use this kotlin serialization library in parsing arguments like
    // (val imageId : String) so we need to add @Serialization annotation with all these screens
    data object HomeScreen : Routes()

    @Serializable
    data object SearchScreen : Routes()

    @Serializable
    data object FavouriteScreen : Routes()

    @Serializable
    data class FullImageScreen(val imageId: String) : Routes()

    @Serializable
    data class ProfileScreen(val profileLink: String) : Routes()

}
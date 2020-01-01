## Editable Profile

This project allows you to create your own profile and edit it anytime you want.

## Challenge Solution Description
- Using firebase database, firebase auth and firebase storage gives me the variaty that i need for this use case.
- Authenticate user with email and password and then using the user id to identify the profile.
- Saving profile data wasn't a problem by using firebase realtime database just call the path i want to store the profile in by firebase database instance
- For the profile picture using firebase storage was good solution because with simple api call i can store and retrieve the picture's url.
- Stored the fixed values on github repository and retrieving it using https://my-json-server.typicode.com/ .
- Working with cities was challenging part but i decided to put all cities in firebase database and then query using name to fetch the city that user wants.
- About building the android application
    - Used MVVM architecture and respository pattern gives me advantages like the following:
        - Separation of concerns.
        - Readable and testable code.
        - Less side effects.
        - Maintainable code.
        - Minimise Coupling.

## Languages, libraries and tools used

 * [Kotlin](https://kotlinlang.org/)
 * [Firebase](https://firebase.google.com/)
 * [androidX libraries](https://developer.android.com/jetpack/androidx)
 * [Android LifeCycle](https://developer.android.com/topic/libraries/architecture)
 * Picasso
 * Retrofit2
 * [Kodien](https://kodein.org/Kodein-DI/)
 * [RxJava](https://github.com/ReactiveX/RxJava)
 * [RxAndroid](https://github.com/ReactiveX/RxAndroid)
 * [Mockito Kotlin](https://github.com/nhaarman/mockito-kotlin/)
 * [Espresso](https://developer.android.com/training/testing/espresso)
 * [AwesomeValidation](https://github.com/thyrlian/AwesomeValidation)
 
 
## Requirements
- min SDK 21

## Installation

- Just clone the app and import to Android Studio.
``git clone https://github.com/marwanadly/EditableProfile``

- Make sure that your are in the ``defaultFlavor`` product flavour.

- For running the unit tests switch to ``mockTesting`` product flavour.


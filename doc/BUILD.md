# Changelog Library: How to build/use library

The library is written with Android Studio and has Gradle support.

You can add this library to your project adding a dependency to your `build.gradle`, or your can reference this project as a library (from Eclipse) or add it as a module (from IntelliJ).


## Including in your project with Gradle

Card Library is pushed to Maven Central as a AAR, so you just need to add the following dependency to your `build.gradle`.

    dependencies {
        compile 'com.github.gabrielemariotti.changeloglib:library:1.3.0'
    }


## Reference this project as a library in Eclipse

if you would like to use this library in Eclipse you have to do these steps:

- clone a copy of this repository, or download it.
- import the `ChangeLogLibrary` code in your workspace
- mark java(*) folder as source (right click on folder -> Build-Path -> use as source folder)
- mark ChangeLogLibrary as Android Library (right click -> Properties -> Android -> Is library)
- use API>=14 to compile library (right click -> Properties -> Android)


If you would like to build the demo you have to do these additional steps:

- import the `ChangeLogDemo` code in your workspace
- mark java(*) folder as source
- add ChangeLogLibrary as dependency ( right click -> Properties -> Android -> Add library)
- add v7 appcompat library following [official guide](http://developer.android.com/tools/support-library/setup.html#libs-with-res)
- add v7-library as dependency ( right click -> Properties -> Android -> Add library)
- use API>=14 to compile demo.


(*) Eclipse uses src and res as source folders.
Android Studio instead uses src/main/java and src/main/res as source folders.


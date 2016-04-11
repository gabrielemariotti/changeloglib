# Changelog Library: How to build/use library

The library is written with Android Studio and has Gradle support.

You can add this library to your project adding a dependency to your `build.gradle`, or your can reference this project as a library (from Eclipse) or add it as a module (from IntelliJ).


## Including in your project with Gradle

Card Library is pushed to Maven Central as a AAR, so you just need to add the following dependency to your `build.gradle`.

    dependencies {
        compile 'com.github.gabrielemariotti.changeloglib:changelog:2.1.0'
    }


## Reference this project as a library in Eclipse

**I really suggest you switching to official IDE Android Studio.**

If you would like to use this library in Eclipse you have to do these steps:

- clone a copy of this repository, or download it (outside eclipse workspace).
- import the `ChangeLogLibrary` code in your workspace starting from ChangeLogLibrary folder. The Wizard will import the code in ChangeLogLibrary/src/main. I suggest you to name it "chgloglib" (or another name) instead of "main".
- mark java(*) folder as source (right click on folder -> Build-Path -> use as source folder).You can also remove the src folder, from the project.
- mark chgloglib as Android Library (right click -> Properties -> Android -> Is library)
- The library targets SDK 23 and works with minSdk=7. In any cases you need to use API>=21 to compile library (right click -> Properties -> Android)
- Clean and build


If you would like to build the demo you have to do these additional steps:

- import the `ChangeLogDemo` code in your workspace starting from ChangeLogDemo folder.
- mark java(*) folder as source
- mark aidl(*) folder as source
- add chgloglib as dependency ( right click -> Properties -> Android -> Add library)
- add support appcompat-v7 (rel 23.3) library following [official guide](http://developer.android.com/tools/support-library/setup.html#libs-with-res)
- add support-v4 (rel 23.3) as dependency ( right click -> Properties -> Android -> Add library)
- add suport design (rel 23.3) as dependency ( right click -> Properties -> Android -> Add library)
- The demo targets SDK 22 and works with minSdk=7. In any cases you need to use API>=21 to compile it (right click -> Properties -> Android).
- Clean and build


(*) Eclipse uses src and res as source folders.
Android Studio instead uses src/main/java and src/main/res as source folders.


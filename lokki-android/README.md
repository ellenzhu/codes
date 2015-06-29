lokki-android
=======================

Development
-----------
To develop this application you will need the [Android SDK](http://developer.android.com/sdk/index.html).

### Build instructions

First replace `ApiUrl` in `ServerAPI.java` with a valid URL leading to the server-side component.
Then run the following command in the project root to build the project:

```
$ ./gradlew assembleDevelopment
```

To install the debug build on an emulator run the following command in the project root (version number is determined by `versionName` in [App/build.gradle](App/build.gradle)):

```
$ adb install -r App/build/outputs/apk/lokki-v[versionName]-debug.apk
```

To install it on a device, connect a device via USB (make sure USB debugging is enabled on the device) and run the command:

```
$ adb install -rd App/build/outputs/apk/lokki-v[versionName]-debug.apk
```

For further information see the [Android documentation](http://developer.android.com/tools/building/building-cmdline.html).

### Product flavors

Project has different product flavors, that can be specified by adding either "Development" or "Production" after some of the gradle commands, like this:

```
$ ./gradlew assembleDevelopment
$ ./gradlew assembleProduction
```

Currently product flavors differ only by server URL they're using.

### Tests

To run the tests run the following command in the project root:

```
$ ./gradlew connectedAndroidTestDevelopment
```

Make sure an emulator or device is connected, running and the screen is unlocked. If it looks like your tests are failing without a good reason, try to [disable animations in your device/emulator you're using](http://lifehacker.com/disable-animations-on-android-to-improve-performance-1583554900).

### Contributing

#### Guidelines

For general code style guidelines see the [Android code style guidelines](http://source.android.com/source/code-style.html). Contrary to what the document says, these are not strict rules for the lokki-android project.

For design follow the [Material design](http://www.google.com/design/spec/material-design/introduction.html) guidelines. Be sure to make your changes [compatible with pre-Lollipop devices](http://android-developers.blogspot.fi/2014/10/appcompat-v21-material-design-for-pre.html) with the [Android Support Library](https://developer.android.com/training/material/compatibility.html#SupportLib).

#### GitHub Issue label explanations

+ *bug* - the issue concerns a bug somewhere in the project

+ *documentation* - the issue concerns the documentation of the project/application

+ *duplicate* - the issue is considered a duplicate of some other issue

+ *enhancement* - a minor change to an existing feature

+ *feature request* - a new feature idea requested to be implemented to the app

+ *in progress* - someone is currently working on this issue

+ *invalid* - the issue or pull request is invalid for some reason

+ *ready* - the issue is ready to be started

+ *test* - the issue concerns the test suite of the project

+ *wontfix* - the issue wont be fixed or implemented


See the [Lokki Wiki](https://github.com/TheSoftwareFactory/lokki/wiki) for more information on development.



Note
----

Lokki is available to the open source community under Apache v2 license AS IS.


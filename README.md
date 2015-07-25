BreastFeedingBuddyFX
======

This project is a sandbox project that allowed me to test JavaFx integration on mobile.
It uses the great work from José Pereda (https://github.com/jperedadnr/Game2048FX)
It is built using JavaFX, JFXtras [library](http://jfxtras.org/), JavaFXPorts mobile [plugin](https://bitbucket.org/javafxports/javafxmobile-plugin) and Gluon [Charm Down](https://bitbucket.org/gluon-oss/charm-down) to run on Desktop, Android and iOS platforms with the very same codebase.

This app allows mother who are breastfeeding their newborn child to keep track of the feeding time and the breast that was used.

Following the very good article from José on [2048FX](http://jperedadnr.blogspot.fr/2015/03/javafx-on-mobile-dream-come-true.html), everything is pretty straighforward.
I'm using Java 8 Lambda Expression in my code, that are backported to Java7 compatible byte code using the [RetroLambda project](https://github.com/orfjackal/retrolambda)

I'm also using JFXTras LocalDateTimePicker control. Since JFXtras is not Java 7 compatible, I had to use their source code in my project, in order to use RetroLambda on them as well.

This app lacks nice UI, and in not very responsive on Android, due to JFXtras LocalDateTimePicker being too heavy.


Desktop
=======

Clone, build and run

```bash
gradlew run
```

Google Play (Android)
=====================

The app has not made it yet on the Google Play Store, but can be launched on a local device connected to your dev machine

```bash
gradlew androidInstall
```

Apple Store (iOS)
=================

The app has not made it yet on the AppStore, and was not  tested on iOS device yet.
Use the following Gradle tasks to try it on your connected iOS device (on MacOS)

```bash
gradlew launchIOSDevice
```

License
===================

The project is licensed under MIT. See [LICENSE](https://github.com/facewindu/BreastFeedingBuddyFX/LICENSE)
file for the full license.
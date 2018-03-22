# Third-Umpire
Third umpire is a voter list watcher standalone application. It watches the general public voter list which is made publicly available by the state of Karnataka in India.

## How To Use This Application?
As of now, there is no executable JAR available through which this app can be launched. Instead, developers have to run [ThirdUmpireApp.java](https://github.com/sbaitmangalkar/third-umpire/blob/master/third-umpire-app/src/main/java/com/voter/info/app/ThirdUmpireApp.java) in order to populate the UI. This approach might drive the general public (non developers) a bit crazy. Very shortly, there will be an executable JAR ready through which the app can be launched.

## Running ThirdUmpireApp.java
Clone, clean and build the project in your local machine.
Run `ThirdUmpireApp.java`
###### On Linux machines
```
$ java ThirdUmpireApp.java
```
###### On Windows machines
```
C:/>java ThirdUmpireApp.java
```
**Note:** You can either navigate to the directory containing ThirdUmpireApp.java and then run it or you can mention the path to ThirdUmpireApp.java while running it.

## Limitations
This application is capable of searching through voter lists which are in English. There are some assembly constituencies in the backend whose details are available only in Kannada (local regional language). In such cases, the application will not parse the content and in turn, there will be no search results available.

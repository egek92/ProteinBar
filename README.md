## Download

in your root `build.gradle` :

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}


```

in your app level `build.gradle` 

```gradle
dependencies {
    ...
    'com.github.egek92:ProteinBar:1.4'
}
```

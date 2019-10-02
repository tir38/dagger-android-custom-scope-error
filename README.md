I have an app that is using `Dagger.android` to minimize boilerplate.
This is working fine if I don't use any custom scopes. In my example lets say I start out with just a "main" package. See [`MainModule`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/main/MainModule.java#L9), 
[`MainComponent`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/main/MainComponent.java#L13), 
and [`MyApplication`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/MyApplication.java#L15).

The dependency graph is really simple: 
* `MainActivity` depends on `DataManager`.


I later add a new feature that has a two screens: [`FeatureActivity`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/newfeature/FeatureActivity.java#L17) and `FeatureActivity2`. They each depend on a new `FeatureManager` and existing `DataManager`. So object graph now looks like 

* `MainActivity` depends on `DataManager`
* `FeatureActivity` depends on `DataManager` and `FeatureManager`
* `FeatureActivity2` depends on `DataManager` and `FeatureManager`

This seems like the perfect use of a custom scope. So I create [`@FeatureScope`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/newfeature/FeatureScope.java#L10)

[`FeatureManager`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/newfeature/FeatureModule.java#L12) is then given this scope. Same with [`FeatureComponenet`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/newfeature/FeatureComponent.java#L13)

I need `FeatureComponent` to inject into more than one Activity, so I can't use `AndroidInjector` like I did in `MainComponent`.

I also need to use [component dependency](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/newfeature/FeatureComponent.java#L15) so that `FeatureActivity` can depend on `DataManager` via `MainComponent`.

I try to compile the app and I get this error

```
> Task :app:compileDebugJavaWithJavac
.../temp/dagger-comp-dep-with-dagger-android/app/src/main/java/com/example/myapplication/MyApplication.java:3: 
error: cannot find symbol
import com.example.myapplication.main.DaggerMainComponent;
                                     ^
  symbol:   class DaggerMainComponent
  location: package com.example.myapplication.main
.../temp/dagger-comp-dep-with-dagger-android/app/src/main/java/com/example/myapplication/newfeature/FeatureComponent.java:16: 
error: [Dagger/MissingBinding] com.example.myapplication.servicelayer.DataManager cannot be provided without an @Inject constructor or an @Provides-annotated method.
public interface FeatureComponent {
       ^
      com.example.myapplication.servicelayer.DataManager is injected at
          com.example.myapplication.newfeature.FeatureActivity.dataManager
      com.example.myapplication.newfeature.FeatureActivity is injected at
          com.example.myapplication.newfeature.FeatureComponent.inject(com.example.myapplication.newfeature.FeatureActivity)
.../temp/dagger-comp-dep-with-dagger-android/app/src/main/java/com/example/myapplication/main/MainComponent.java:17: 
error: [ComponentProcessor:MiscError] dagger.internal.codegen.ComponentProcessor was unable to process this interface because not all of its dependencies could be resolved. Check for compilation errors or a circular dependency with generated code.
public interface MainComponent extends AndroidInjector<MyApplication> {
       ^

```

The key line is 

```
.DataManager cannot be provided without an @Inject constructor or an @Provides-annotated method.
```

I guess this makes sense. If I understand component dependency, `FeatureComponent` depends on `MainComponent` so `MainComponent` has to expose `DataManager` directly. So I add a getter to [`MainComponent`](https://github.com/tir38/dagger-android-custom-scope-error/blob/3fb606f658e0a1b080d399aead8bea6f246a985d/app/src/main/java/com/example/myapplication/main/MainComponent.java#L26).

When I try to compile the app again I get this new error:


```
.../temp/dagger-comp-dep-with-dagger-android/app/src/main/java/com/example/myapplication/main/MainComponent.java:20: 
warning: [dagger.android.AndroidInjector.Builder.build()] com.example.myapplication.main.MainComponent.Builder.build() returns dagger.android.AndroidInjector<com.example.myapplication.MyApplication>, but com.example.myapplication.main.MainComponent declares additional component method(s): getDataManager(). In order to provide type-safe access to these methods, override build() to return com.example.myapplication.main.MainComponent
    abstract class Builder extends AndroidInjector.Builder<MyApplication> {
             ^
.../temp/dagger-comp-dep-with-dagger-android/app/src/main/java/com/example/myapplication/MyApplication.java:3: 
error: cannot find symbol
import com.example.myapplication.main.DaggerMainComponent;
                                     ^
  symbol:   class DaggerMainComponent
  location: package com.example.myapplication.main
.../temp/dagger-comp-dep-with-dagger-android/app/src/main/java/com/example/myapplication/main/MainComponent.java:17: 
error: [Dagger/DependencyCycle] Found a dependency cycle:
public interface MainComponent extends AndroidInjector<MyApplication> {
       ^
      com.example.myapplication.servicelayer.DataManager is injected at
          com.example.myapplication.main.MainModule.bindsDataManager(dataManager)
      com.example.myapplication.servicelayer.DataManager is provided at
          com.example.myapplication.main.MainComponent.getDataManager()
```
          
I don't fully understand this error. How is this a dependency cycle? How is `injected` and `provided` different?

I don't understand the warning either. What does it mean `In order to provide type-safe access to these methods, override build() `?
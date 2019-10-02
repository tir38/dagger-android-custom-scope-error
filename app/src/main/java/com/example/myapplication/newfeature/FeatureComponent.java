package com.example.myapplication.newfeature;

import com.example.myapplication.main.MainComponent;

import dagger.Component;
import dagger.android.AndroidInjector;

/**
 * IMPORTANT:
 * By design this class does not extend {@link AndroidInjector} since we want to inject objects from
 * {@link FeatureModule} into several android components (not just one <T> object)
 */
@FeatureScope
@Component(modules = FeatureModule.class,
        dependencies = MainComponent.class) // depend on MainComponent so FeatureActivity can get access to DataManager
public interface FeatureComponent {

    void inject(FeatureActivity featureActivity);

    void inject(FeatureActivity2 featureActivity2);
}

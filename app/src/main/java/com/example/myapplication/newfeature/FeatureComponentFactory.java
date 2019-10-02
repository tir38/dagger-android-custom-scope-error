package com.example.myapplication.newfeature;

import com.example.myapplication.MyApplication;

/**
 * Class which provides access to {@link FeatureComponent}s
 */
class FeatureComponentFactory {
    // We don't create this with Dagger, so we can't use Dagger to enforce @Singleton, so we do it the old school way.
    private static final FeatureComponentFactory INSTANCE = new FeatureComponentFactory();

    private FeatureComponent featureComponent;

    static FeatureComponent getFeatureComponent() {
        return INSTANCE._getFeatureComponent();
    }

    private FeatureComponent _getFeatureComponent() {
        if (featureComponent == null) {
            featureComponent = DaggerFeatureComponent
                    .builder()
                    .mainComponent(MyApplication.component)
                    .build();
        }
        return featureComponent;
    }
}
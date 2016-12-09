package takamk2.local.wish.application;

import android.app.Application;

import timber.log.Timber;

public class WishApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}

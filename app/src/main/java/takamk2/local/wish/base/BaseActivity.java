package takamk2.local.wish.base;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import takamk2.local.wish.R;
import takamk2.local.wish.fragment.WelcomeFragment;
import timber.log.Timber;

/**
 * Created by takamk2 on 16/12/09.
 * <p/>
 * The Edit Fragment of Base Class.
 */
public abstract class BaseActivity extends Activity {

    protected FragmentManager mFragmentManager;

    protected void initFragmentManager() {
        mFragmentManager = getFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int count = mFragmentManager.getBackStackEntryCount();
                Timber.i("onBackStackChanged - info : count=%d", count);
                if (count == 0) {
                    finish();
                }
            }
        });
    }
}

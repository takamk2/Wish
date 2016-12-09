package takamk2.local.wish.activity;

import android.os.Bundle;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseActivity;
import takamk2.local.wish.fragment.WelcomeFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragmentManager();

        WelcomeFragment.replaceFragment(this);
    }
}

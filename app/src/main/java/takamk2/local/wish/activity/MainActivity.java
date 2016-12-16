package takamk2.local.wish.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

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

    public void showTotalSavingsFragment() {
        View v = findViewById(R.id.fragment_total_savings);
        if (v.getVisibility() != View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
            v.setAnimation(animation);
            v.setVisibility(View.VISIBLE);
        }
    }

    public void hideTotalSavingsFragment() {
        View v = findViewById(R.id.fragment_total_savings);
        if (v.getVisibility() != View.GONE) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out);
            v.setAnimation(animation);
            v.setVisibility(View.GONE);
        }
    }
}

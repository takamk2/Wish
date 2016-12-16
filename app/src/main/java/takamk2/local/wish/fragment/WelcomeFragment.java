package takamk2.local.wish.fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import takamk2.local.wish.R;
import takamk2.local.wish.activity.MainActivity;
import takamk2.local.wish.base.BaseFragment;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeFragment extends BaseFragment {

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        // Todo: bundle
        // Todo: animation
        return fragment;
    }

    public static void replaceFragment(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = WelcomeFragment.newInstance();
        transaction.replace(R.id.fragment_container2, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* ------------------------------------------------------------------------------------------ */
    private Button btNext;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_next:
                    onClickNext();
                    break;
                default:
                    break;
            }
        }
    };

    /* ------------------------------------------------------------------------------------------ */
    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.i("onCreateView - start");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    protected void bindViews(View view) {
        Timber.i("bindViews - start");
        btNext = (Button) view.findViewById(R.id.bt_next);
    }

    @Override
    protected void bindActions() {
        Timber.i("bindActions - start");
        btNext.setOnClickListener(mOnClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideTotalSavingsFragment();
    }

    /**
     * when Tap the "NEXT" button
     */
    private void onClickNext() {
        Timber.i("onClickNext - start");
        TopFragment.replaceFragment(getActivity());
    }
}

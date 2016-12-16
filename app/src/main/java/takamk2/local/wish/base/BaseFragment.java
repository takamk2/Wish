package takamk2.local.wish.base;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import takamk2.local.wish.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    /* ------------------------------------------------------------------------------------------ */
    protected abstract void bindViews(View view);

    protected abstract void bindActions();

    /* ------------------------------------------------------------------------------------------ */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setActivity(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        bindActions();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showTotalSavingsFragment();
        }
    }

    @Override
    public void onDetach() {
        setActivity(null);
        super.onDetach();
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public boolean hasActivity() {
        if (mActivity == null) {
            return false;
        }
        if (mActivity.isFinishing() || mActivity.isDestroyed()) {
            return false;
        }

        return true;
    }
}

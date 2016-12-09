package takamk2.local.wish.fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import takamk2.local.wish.R;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishDisplayFragment extends Fragment {

    private static final String KEY_POSITION = "position";
    private static final String KEY_ID = "id";

    public static WishDisplayFragment newInstance() {
        WishDisplayFragment fragment = new WishDisplayFragment();
        // Todo: bundle
        // Todo: animation
        return fragment;
    }

    public static void replaceFragment(Activity activity, int position, long id) {
        Fragment fragment = WishDisplayFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putLong(KEY_ID, id);
        fragment.setArguments(args);

        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* ------------------------------------------------------------------------------------------ */
    private int mPosition = -1;
    private long mId = -1L;

    public WishDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mPosition = args.getInt(KEY_POSITION);
            mId = args.getLong(KEY_ID);
        }
        Timber.i("onCreate - info : position=%d id=%d", mPosition, mId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_display, container, false);
    }

}

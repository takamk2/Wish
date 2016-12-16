package takamk2.local.wish.fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseFragment;
import takamk2.local.wish.db.WishDBHelper;
import takamk2.local.wish.db.WishDBStore;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopFragment extends BaseFragment {

    public static TopFragment newInstance() {
        TopFragment fragment = new TopFragment();
        // Todo: bundle
        // Todo: animation
        return fragment;
    }

    public static void replaceFragment(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = TopFragment.newInstance();
        transaction.replace(R.id.fragment_container2, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* ------------------------------------------------------------------------------------------ */
    private Button mBtToWishList;
    private Button mBtToTaskList;
    private Button mBtToSummary;

    /* ------------------------------------------------------------------------------------------ */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_to_wish_list:
                    onClickToWishList();
                    break;
                case R.id.bt_to_task_list:
                    onClickToTaskList();
                    break;
                case R.id.bt_to_summary:
                    onClickToSummary();
                    break;
                default:
                    break;
            }
        }
    };

    /* ------------------------------------------------------------------------------------------ */
    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    protected void bindViews(View view) {
        mBtToWishList = (Button) view.findViewById(R.id.bt_to_wish_list);
        mBtToTaskList = (Button) view.findViewById(R.id.bt_to_task_list);
        mBtToSummary = (Button) view.findViewById(R.id.bt_to_summary);
    }

    @Override
    protected void bindActions() {
        mBtToWishList.setOnClickListener(mOnClickListener);
        mBtToTaskList.setOnClickListener(mOnClickListener);
        mBtToSummary.setOnClickListener(mOnClickListener);
    }

    private void onClickToWishList() {
        WishListFragment.replaceFragment(getActivity());
    }

    private void onClickToTaskList() {
        TaskListFragment.replaceFragment(getActivity());
    }

    private void onClickToSummary() {
        // Todo: create SummaryFragment
        if (hasActivity()) {
            Toast.makeText(mActivity, "T.B.D.", Toast.LENGTH_SHORT).show();
        }
    }
}

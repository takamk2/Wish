package takamk2.local.wish.fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseFragment;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.dialog.TaskRegisterDialog;
import takamk2.local.wish.util.TextUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskListFragment extends BaseFragment {

    private TasksAdapter mTasksAdapter;
    private ListView mLvTasks;
    private Button mBtAdd;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_add:
                    onClickAdd();
                    break;
                default:
                    break;
            }
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> mTasksLoaderCallBacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = {
                    WishDBStore.Tasks.COLUMN_ID,
                    WishDBStore.Tasks.COLUMN_TITLE,
                    WishDBStore.Tasks.COLUMN_PRICE
            };
            String sortOrder = WishDBStore.Tasks.COLUMN_ID + " desc";
            return new CursorLoader(mActivity,
                    WishDBStore.Tasks.CONTENT_URI, projection, null, null, sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mTasksAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mTasksAdapter.swapCursor(null);
        }
    };

    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        // Todo: bundle
        // Todo: animation
        return fragment;
    }

    public static void replaceFragment(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = TaskListFragment.newInstance();
        transaction.replace(R.id.fragment_container2, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* ------------------------------------------------------------------------------------------ */
    public TaskListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new TasksAdapter(getActivity());
        getLoaderManager().initLoader(0, null, mTasksLoaderCallBacks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    protected void bindViews(View view) {
        mLvTasks = (ListView) view.findViewById(R.id.lv_tasks);
        mBtAdd = (Button) view.findViewById(R.id.bt_add);

        mLvTasks.setAdapter(mTasksAdapter);
    }

    @Override
    protected void bindActions() {
        mBtAdd.setOnClickListener(mOnClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().getLoader(0).startLoading();
    }

    private void onClickAdd() {
        TaskRegisterDialog.showDialog(getActivity());
    }

    /* ------------------------------------------------------------------------------------------ */
    private class TasksAdapter extends CursorAdapter {

        public TasksAdapter(Context context) {
            super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_task_list, viewGroup, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);

            String title = cursor.getString(cursor.getColumnIndex(WishDBStore.Tasks.COLUMN_TITLE));
            Long price = cursor.getLong(cursor.getColumnIndex(WishDBStore.Tasks.COLUMN_PRICE));

            tvTitle.setText(title);
            tvPrice.setText(TextUtil.convertNumToCammaSeparated(price));
        }
    }
}

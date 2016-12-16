package takamk2.local.wish.fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import takamk2.local.wish.R;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.dialog.DeleteDialog;
import takamk2.local.wish.util.TextUtil;
import timber.log.Timber;

import static android.R.attr.finishOnCloseSystemDialogs;
import static android.R.attr.name;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishDisplayFragment extends Fragment {

    private static final String KEY_POSITION = "position";
    private static final String KEY_ID = "id";

    private static final int REQUEST_DELETE = 1;

    private TextView mTvName;
    private TextView mTvPrice;
    private Button mBtEdit;
    private Button mBtDelete;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_edit:
                    onClickEdit();
                    break;
                case R.id.bt_delete:
                    onClickDelete();
                    break;
                default:
                    break;
            }
        }
    };

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
        transaction.replace(R.id.fragment_container2, fragment, fragment.getClass().getName());
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
        } else {
            Toast.makeText(getActivity(), "Data does not exist", Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
        }
        Timber.i("onCreate - info : position=%d id=%d", mPosition, mId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_display, container, false);
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
        new LoadTask().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DELETE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = ContentUris.withAppendedId(WishDBStore.Wishes.CONTENT_URI, mId);
                getContext().getContentResolver().delete(uri, null, null);
                Toast.makeText(getActivity(), "Deleted!!", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
            }
        }
    }

    private void bindViews(View view) {
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvPrice = (TextView) view.findViewById(R.id.tv_price);
        mBtEdit = (Button) view.findViewById(R.id.bt_edit);
        mBtDelete = (Button) view.findViewById(R.id.bt_delete);
    }

    private void bindActions() {
        mBtEdit.setOnClickListener(mOnClickListener);
        mBtDelete.setOnClickListener(mOnClickListener);
    }

    private void onClickEdit() {

    }

    private void onClickDelete() {
        DeleteDialog.showDialogForResult(this, REQUEST_DELETE);
    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {

        private String mErrorMessage;

        private String mName = "";
        private long mPrice = 0L;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doTask();
            } catch (Exception e) {
                mErrorMessage = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mTvName.setText(mName);
            mTvPrice.setText(TextUtil.convertNumToCurrency(mPrice));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        private void doTask() {
            String[] projection = {
                    WishDBStore.Wishes.COLUMN_NAME, WishDBStore.Wishes.COLUMN_PRICE
            };
            Uri uri = ContentUris.withAppendedId(WishDBStore.Wishes.CONTENT_URI, mId);
            Cursor cursor = null;
            try {
                cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
                if (cursor.moveToFirst()) {
                    mName = cursor.getString(cursor.getColumnIndex(projection[0]));
                    mPrice = cursor.getLong(cursor.getColumnIndex(projection[1]));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }
        }
    }
}

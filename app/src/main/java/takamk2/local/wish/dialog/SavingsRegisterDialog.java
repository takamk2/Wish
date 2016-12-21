package takamk2.local.wish.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import takamk2.local.wish.R;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.util.TextUtil;

public class SavingsRegisterDialog extends DialogFragment {

    private Button mBtOk;
    private Button mBtCancel;
    private EditText mEtPrice;
    private TextView mTvMaxPrice;

    private long mTotalSavings = 0L;
    private long mTotalTaskPrice = 0L;
    private long mTotalWishPrice = 0L;
    private long mLimitPrice = 0L;

    public static SavingsRegisterDialog newInstance() {
        SavingsRegisterDialog dialog = new SavingsRegisterDialog();
        return dialog;
    }

    public static void showDialog(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        DialogFragment dialog = SavingsRegisterDialog.newInstance();
        dialog.show(manager, dialog.getClass().getName());
    }

    /* ------------------------------------------------------------------------------------------ */
    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_ok:
                    onClickOk();
                    break;
                case R.id.bt_cancel:
                    onClickCancel();
                    break;
                default:
                    break;
            }
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> mHistoriesCallBacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = {
                    WishDBStore.Histories.COLUMN_TOTAL_SAVINGS,
                    WishDBStore.Histories.COLUMN_TOTAL_TASK_PRICE,
                    WishDBStore.Histories.COLUMN_TOTAL_WISH_PRICE
            };
            String sortOrder = WishDBStore.Histories.COLUMN_CREATED + " desc";
            return new CursorLoader(getActivity(), WishDBStore.Histories.CONTENT_URI, projection,
                    null, null, sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
                mTotalSavings = cursor.getLong(
                        cursor.getColumnIndex(WishDBStore.Histories.COLUMN_TOTAL_SAVINGS));
                mTotalTaskPrice = cursor.getLong(
                        cursor.getColumnIndex(WishDBStore.Histories.COLUMN_TOTAL_TASK_PRICE));
                mTotalWishPrice = cursor.getLong(
                        cursor.getColumnIndex(WishDBStore.Histories.COLUMN_TOTAL_WISH_PRICE));
                mLimitPrice = mTotalTaskPrice - mTotalSavings;
                mTvMaxPrice.setText(TextUtil.convertNumToCurrency(mLimitPrice));
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /* ------------------------------------------------------------------------------------------ */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        getLoaderManager().initLoader(0, null, mHistoriesCallBacks);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_savings_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        bindActions();
    }

    private void bindViews(View view) {
        mTvMaxPrice = (TextView) view.findViewById(R.id.tv_max_price);
        mEtPrice = (EditText) view.findViewById(R.id.et_price);
        mBtOk = (Button) view.findViewById(R.id.bt_ok);
        mBtCancel = (Button) view.findViewById(R.id.bt_cancel);
    }

    private void bindActions() {
        mBtOk.setOnClickListener(mOnclickListener);
        mBtCancel.setOnClickListener(mOnclickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().getLoader(0).startLoading();
    }

    private void onClickOk() {

        String price = mEtPrice.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WishDBStore.Savings.COLUMN_PRICE, price);
        values.put(WishDBStore.Savings.COLUMN_CREATED, System.currentTimeMillis());

        if (checkValues(values)) {
            getActivity().getContentResolver().insert(WishDBStore.Savings.CONTENT_URI, values);
            dismiss();
        }
    }

    private boolean checkValues(ContentValues values) {

        boolean isError = false;

        String price = values.getAsString(WishDBStore.Savings.COLUMN_PRICE);
        if (price == null || price.equals("")) {
            mEtPrice.setError("required");
            isError = true;
        } else if (!price.matches("^\\d+$")) {
            mEtPrice.setError("invalid");
            isError = true;
        } else if (Long.parseLong(price) == 0) {
            mEtPrice.setError("not zero");
            isError = true;
        } else if ((mLimitPrice - Long.parseLong(price)) < 0) {
            mEtPrice.setError("less than 0");
            isError = true;
        }

        if (!isError) {
            values.put(WishDBStore.Savings.COLUMN_PRICE, Long.parseLong(price));
        }

        return !isError;
    }

    private void onClickCancel() {
        Toast.makeText(getActivity(), "Tapped cancel", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}


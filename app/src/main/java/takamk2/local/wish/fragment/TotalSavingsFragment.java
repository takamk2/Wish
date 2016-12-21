package takamk2.local.wish.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseFragment;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.dialog.SavingsRegisterDialog;
import takamk2.local.wish.util.AnimationNumber;
import takamk2.local.wish.util.TextUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TotalSavingsFragment extends BaseFragment {

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
        return fragment;
    }

    /* ------------------------------------------------------------------------------------------ */
    private TextView mTvTotalSavings;

    public LoaderManager.LoaderCallbacks<Cursor> mTotalSavingsLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = {"sum(" + WishDBStore.Savings.COLUMN_PRICE + ")"};
            return new CursorLoader(getActivity(), WishDBStore.Savings.CONTENT_URI, projection,
                    null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
                long totalSavings = cursor.getLong(0);
                if (mTvTotalSavings == null) {
                    return;
                }
                String oldSavingsStr = mTvTotalSavings.getText().toString();
                if (oldSavingsStr.equals("")) {
                    mTvTotalSavings.setText(TextUtil.convertNumToCurrency(totalSavings));
                    return;
                }
                long oldPrice = TextUtil.extractNumber(oldSavingsStr);
                new AnimationNumber(oldPrice, totalSavings, new AnimationNumber.CallBack() {
                    @Override
                    public void onCall(Long num) {
                        mTvTotalSavings.setText(TextUtil.convertNumToCurrency(num));
                    }
                });
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /* ------------------------------------------------------------------------------------------ */
    public TotalSavingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, mTotalSavingsLoaderCallbacks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total_savings, container, false);
    }

    @Override
    protected void bindViews(View view) {
        mTvTotalSavings = (TextView) view.findViewById(R.id.tv_total_savings);
    }

    @Override
    protected void bindActions() {
        // Todo: DEBUG
        mTvTotalSavings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SavingsRegisterDialog.showDialog(getActivity());
//                Random random = new Random();
//                long oldPrice = TextUtil.extractNumber(mTvTotalSavings.getText().toString());
//                long newPrice = oldPrice - random.nextInt(100) * 10;
//                new AnimationNumber(oldPrice, newPrice, new AnimationNumber.CallBack() {
//                    @Override
//                    public void onCall(Long num) {
//                        mTvTotalSavings.setText(TextUtil.convertNumToCammaSeparated(num));
//                    }
//                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().getLoader(0).startLoading();
    }
}

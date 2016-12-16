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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseFragment;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.dialog.WishRegisterDialog;
import takamk2.local.wish.util.AnimationNumber;
import takamk2.local.wish.util.TextUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishListFragment extends BaseFragment {

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
        // Todo: bundle
        // Todo: animation
        return fragment;
    }

    public static void replaceFragment(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = WishListFragment.newInstance();
        transaction.replace(R.id.fragment_container2, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* ------------------------------------------------------------------------------------------ */
    private WishesAdapter mWishesAdapter;

    private TextView mTvTotalPrice;
    private Button mBtAdd;
    private ListView mLvWishes;

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

    private AdapterView.OnItemClickListener mOnItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    WishDisplayFragment.replaceFragment(getActivity(), position, id);
                }
            };

    private LoaderManager.LoaderCallbacks<Cursor> mTotalPriceLoaderCallBacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    String[] projection = {"sum(" + WishDBStore.Wishes.COLUMN_PRICE + ")"};
                    return new CursorLoader(mActivity, WishDBStore.Wishes.CONTENT_URI, projection,
                            null, null, null);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    if (cursor.moveToFirst()) {
                        long totalPrice = cursor.getLong(0);
                        if (mTvTotalPrice == null) {
                            return;
                        }
                        String oldPriceStr = mTvTotalPrice.getText().toString();
                        if (oldPriceStr.equals("")) {
                            mTvTotalPrice.setText(TextUtil.convertNumToCurrency(totalPrice));
                            return;
                        }
                        long oldPrice = TextUtil.extractNumber(oldPriceStr);
                        new AnimationNumber(oldPrice, totalPrice, new AnimationNumber.CallBack() {
                            @Override
                            public void onCall(Long num) {
                                mTvTotalPrice.setText(TextUtil.convertNumToCurrency(num));
                            }
                        });
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            };

    private LoaderManager.LoaderCallbacks<Cursor> mWishListCallBacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                    String[] projection = {
                            WishDBStore.Wishes.COLUMN_ID,
                            WishDBStore.Wishes.COLUMN_NAME,
                            WishDBStore.Wishes.COLUMN_PRICE
                    };
                    String selection = null;
                    String[] selectionArgs = null;
                    String sortOrder = null;
                    return new CursorLoader(getActivity(), WishDBStore.Wishes.CONTENT_URI,
                            projection, selection, selectionArgs, sortOrder);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                    mWishesAdapter.swapCursor(cursor);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    mWishesAdapter.swapCursor(null);
                }
            };

    /* ------------------------------------------------------------------------------------------ */
    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWishesAdapter = new WishesAdapter(getActivity());
        getLoaderManager().initLoader(0, null, mTotalPriceLoaderCallBacks);
        getLoaderManager().initLoader(1, null, mWishListCallBacks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    protected void bindViews(View view) {
        mTvTotalPrice = (TextView) view.findViewById(R.id.tv_total_price);
        mBtAdd = (Button) view.findViewById(R.id.bt_add);
        mLvWishes = (ListView) view.findViewById(R.id.lv_wishes);
        mLvWishes.setAdapter(mWishesAdapter);
    }

    @Override
    protected void bindActions() {
        mBtAdd.setOnClickListener(mOnClickListener);
        mLvWishes.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().getLoader(0).startLoading();
    }

    private void onClickAdd() {
        WishRegisterDialog.showDialog(getActivity());
    }

    /* ------------------------------------------------------------------------------------------ */
    private class WishesAdapter extends CursorAdapter {

        public WishesAdapter(Context context) {
            super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_wish_list, viewGroup, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvPrice = (TextView) view.findViewById(R.id.tv_price);

            String name = cursor.getString(cursor.getColumnIndex(WishDBStore.Wishes.COLUMN_NAME));
            Long price = cursor.getLong(cursor.getColumnIndex(WishDBStore.Wishes.COLUMN_PRICE));

            tvName.setText(name);
            tvPrice.setText(TextUtil.convertNumToCammaSeparated(price));
        }
    }
}

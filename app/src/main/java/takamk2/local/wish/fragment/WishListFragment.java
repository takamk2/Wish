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

/**
 * A simple {@link Fragment} subclass.
 */
public class WishListFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private WishesAdapter mWishesAdapter;

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
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /* ------------------------------------------------------------------------------------------ */
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

    /* ------------------------------------------------------------------------------------------ */
    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWishesAdapter = new WishesAdapter(getActivity());
        getActivity().getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    protected void bindViews(View view) {
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
            tvPrice.setText(String.valueOf(price)); // Todo: convert currency
        }
    }


    /* ------------------------------------------------------------------------------------------ */
//    private class WishesAdapter extends BaseAdapter {
//
//        private final LayoutInflater mInflater;
//
//        public WishesAdapter() {
//            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            return mWishes.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mWishes.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return mWishes.get(position).getId();
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup viewGroup) {
//            if (view == null) {
//                view = mInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
//            }
//
//            Wish w = mWishes.get(position);
//            TextView tvText1 = (TextView) view.findViewById(android.R.id.text1);
//            tvText1.setText(w.getName() + " (" + w.getId() + ")");
//
//            return view;
//        }
//    }
//
//    /* ------------------------------------------------------------------------------------------ */
//    // Todo: for DEBUG
//    private class Wish {
//
//        private long mId;
//        private String mName;
//
//        public Wish(long id, String name) {
//            mId = id;
//            mName = name;
//        }
//
//        public long getId() {
//            return mId;
//        }
//
//        public String getName() {
//            return mName;
//        }
//    }
}

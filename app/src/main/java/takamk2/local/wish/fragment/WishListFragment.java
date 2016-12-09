package takamk2.local.wish.fragment;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseFragment;
import takamk2.local.wish.dialog.WishRegisterDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishListFragment extends BaseFragment {

    private WishesAdapter mWishesAdapter;
    private List<Wish> mWishes = new ArrayList<>();

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

        Wish w = new Wish(0, "Tako");
        mWishes.add(w);
        w = new Wish(1, "Ika");
        mWishes.add(w);
        mWishesAdapter = new WishesAdapter();
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

    private void onClickAdd() {
        WishRegisterDialog.showDialog(getActivity());
    }

    /* ------------------------------------------------------------------------------------------ */
    private class WishesAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        public WishesAdapter() {
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mWishes.size();
        }

        @Override
        public Object getItem(int position) {
            return mWishes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mWishes.get(position).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = mInflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            }

            Wish w = mWishes.get(position);
            TextView tvText1 = (TextView) view.findViewById(android.R.id.text1);
            tvText1.setText(w.getName() + " (" + w.getId() + ")");

            return view;
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    // Todo: for DEBUG
    private class Wish {

        private long mId;
        private String mName;

        public Wish(long id, String name) {
            mId = id;
            mName = name;
        }

        public long getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }
    }
}

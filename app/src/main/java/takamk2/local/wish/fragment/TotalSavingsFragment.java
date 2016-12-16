package takamk2.local.wish.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

import takamk2.local.wish.R;
import takamk2.local.wish.base.BaseFragment;
import takamk2.local.wish.util.AnimationNumber;
import takamk2.local.wish.util.TextUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TotalSavingsFragment extends BaseFragment {

    private TextView mTvTotalSavings;

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
        return fragment;
    }

    /* ------------------------------------------------------------------------------------------ */
    public TotalSavingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Random random = new Random();
                long oldPrice = TextUtil.extractNumber(mTvTotalSavings.getText().toString());
                long newPrice = oldPrice - random.nextInt(100) * 10;
                new AnimationNumber(oldPrice, newPrice, new AnimationNumber.CallBack() {
                    @Override
                    public void onCall(Long num) {
                        mTvTotalSavings.setText(TextUtil.convertNumToCammaSeparated(num));
                    }
                });
            }
        });
    }
}

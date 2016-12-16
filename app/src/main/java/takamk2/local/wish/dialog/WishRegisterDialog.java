package takamk2.local.wish.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

import takamk2.local.wish.R;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.fragment.WishListFragment;

public class WishRegisterDialog extends DialogFragment {

    private Button mBtOk;
    private Button mBtCancel;
    private EditText mEtName;
    private EditText mEtPrice;

    public static WishRegisterDialog newInstance() {
        WishRegisterDialog dialog = new WishRegisterDialog();
        return dialog;
    }

    public static void showDialog(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        DialogFragment dialog = WishRegisterDialog.newInstance();
        dialog.show(manager, dialog.getClass().getName());
    }

    /* ------------------------------------------------------------------------------------------ */
    private View.OnClickListener mOnClickListner = new View.OnClickListener() {
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

    /* ------------------------------------------------------------------------------------------ */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_wish_register, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        bindActions();
    }

    private void bindViews(View view) {
        mBtOk = (Button) view.findViewById(R.id.bt_ok);
        mBtCancel = (Button) view.findViewById(R.id.bt_cancel);
        mEtName = (EditText) view.findViewById(R.id.et_name);
        mEtPrice = (EditText) view.findViewById(R.id.et_price);
    }

    private void bindActions() {
        mBtOk.setOnClickListener(mOnClickListner);
        mBtCancel.setOnClickListener(mOnClickListner);
    }

    private void onClickOk() {

        String name = mEtName.getText().toString();
        String price = mEtPrice.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WishDBStore.Wishes.COLUMN_NAME, name);
        values.put(WishDBStore.Wishes.COLUMN_PRICE, price);

        if (checkValues(values)) {
            getActivity().getContentResolver().insert(WishDBStore.Wishes.CONTENT_URI, values);
            dismiss();
        }
    }

    private boolean checkValues(ContentValues values) {

        boolean isError = false;

        String name = values.getAsString(WishDBStore.Wishes.COLUMN_NAME);
        if (name == null || name.equals("")) {
            mEtName.setError("required");
            isError = true;
        }

        String price = values.getAsString(WishDBStore.Wishes.COLUMN_PRICE);
        if (price == null || price.equals("")){
            isError = true;
        }
        if (!price.matches("^\\d+$")) {
            mEtPrice.setError("invalid");
            isError = true;
        }
        if (!isError) {
            values.put(WishDBStore.Wishes.COLUMN_PRICE, Long.parseLong(price));
        }

        return !isError;
    }

    private void onClickCancel() {
        Toast.makeText(getActivity(), "Tapped cancel", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

package takamk2.local.wish.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import takamk2.local.wish.R;
import takamk2.local.wish.db.WishDBStore;

/**
 * Created by takamk2 on 16/12/09.
 * <p/>
 * The Edit Fragment of Base Class.
 */
public class TaskRegisterDialog extends DialogFragment {

    private Button mBtOk;
    private Button mBtCancel;
    private EditText mEtName;
    private EditText mEtPrice;

    public static TaskRegisterDialog newInstance() {
        TaskRegisterDialog dialog = new TaskRegisterDialog();
        return dialog;
    }

    public static void showDialog(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        DialogFragment dialog = TaskRegisterDialog.newInstance();
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
        View view = inflater.inflate(R.layout.dialog_task_register, container, false);
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
        mEtName = (EditText) view.findViewById(R.id.et_title);
        mEtPrice = (EditText) view.findViewById(R.id.et_price);
    }

    private void bindActions() {
        mBtOk.setOnClickListener(mOnClickListner);
        mBtCancel.setOnClickListener(mOnClickListner);
    }

    private void onClickOk() {

        String title = mEtName.getText().toString();
        String price = mEtPrice.getText().toString();

        ContentValues values = new ContentValues();
        values.put(WishDBStore.Tasks.COLUMN_TITLE, title);
        values.put(WishDBStore.Tasks.COLUMN_PRICE, price);

        if (checkValues(values)) {
            getActivity().getContentResolver().insert(WishDBStore.Tasks.CONTENT_URI, values);
            dismiss();
        }
    }

    private boolean checkValues(ContentValues values) {

        boolean isError = false;

        String title = values.getAsString(WishDBStore.Tasks.COLUMN_TITLE);
        if (title == null || title.equals("")) {
            mEtName.setError("required");
            isError = true;
        }

        String price = values.getAsString(WishDBStore.Tasks.COLUMN_PRICE);
        if (price == null || price.equals("")){
            isError = true;
        }
        if (!price.matches("^\\d+$")) {
            mEtPrice.setError("invalid");
            isError = true;
        }
        if (!isError) {
            values.put(WishDBStore.Tasks.COLUMN_PRICE, Long.parseLong(price));
        }

        return !isError;
    }

    private void onClickCancel() {
        Toast.makeText(getActivity(), "Tapped cancel", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}


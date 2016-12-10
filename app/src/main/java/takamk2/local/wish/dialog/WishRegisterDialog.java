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
import android.widget.Toast;

import java.util.Random;

import takamk2.local.wish.R;
import takamk2.local.wish.db.WishDBStore;
import takamk2.local.wish.fragment.WishListFragment;

public class WishRegisterDialog extends DialogFragment {

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

        Button btOk = (Button) view.findViewById(R.id.bt_ok);
        btOk.setOnClickListener(mOnClickListner);

        Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
        btCancel.setOnClickListener(mOnClickListner);
    }

    private void onClickOk() {
        // Todo: register in onActivityResult
        Random random = new Random();
        String name = "Dummy " + random.nextInt(1000);
        long price = random.nextInt(10) * 1000;

        ContentValues values = new ContentValues();
        values.put(WishDBStore.Wishes.COLUMN_NAME, name);
        values.put(WishDBStore.Wishes.COLUMN_PRICE, price);
        Uri newUri = getActivity().getContentResolver().insert(WishDBStore.Wishes.CONTENT_URI, values);

        Toast.makeText(getActivity(), "Tapped ok : id=" + newUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void onClickCancel() {
        Toast.makeText(getActivity(), "Tapped cancel", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

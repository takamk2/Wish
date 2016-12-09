package takamk2.local.wish.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import takamk2.local.wish.R;
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

        // Todo: DEBUG
        Button btOk = (Button) view.findViewById(R.id.bt_ok);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Tapped ok", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}

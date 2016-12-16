package takamk2.local.wish.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import takamk2.local.wish.R;

/**
 * Created by takamk2 on 16/12/15.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class DeleteDialog extends DialogFragment {

    public static DeleteDialog newInstance() {
        DeleteDialog dialog = new DeleteDialog();
        return dialog;
    }

    public static void showDialog(Activity activity) {
        FragmentManager manager = activity.getFragmentManager();
        DialogFragment dialog = DeleteDialog.newInstance();
        dialog.show(manager, dialog.getClass().getName());
    }

    public static void showDialogForResult(Fragment fragment, int requestCode) {
        FragmentManager manager = fragment.getActivity().getFragmentManager();
        DialogFragment dialog = DeleteDialog.newInstance();
        dialog.setTargetFragment(fragment, requestCode);
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
        View view = inflater.inflate(R.layout.dialog_delete, container, false);
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
        Fragment target = getTargetFragment();
        Intent intent = new Intent();
        target.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }

    private void onClickCancel() {
        dismiss();
    }
}

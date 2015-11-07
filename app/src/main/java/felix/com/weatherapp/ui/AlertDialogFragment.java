package felix.com.weatherapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import felix.com.weatherapp.R;

/**
 * Created by felix on 11/5/2015.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error_title)
                .setMessage(R.string.error_message).setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}

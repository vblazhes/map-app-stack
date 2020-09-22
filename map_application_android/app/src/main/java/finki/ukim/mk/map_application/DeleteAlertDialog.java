package finki.ukim.mk.map_application;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteAlertDialog extends AppCompatDialogFragment {

    private DeleteAlertDialogListener listener;
    private int POSITION;

    public static DeleteAlertDialog newInstance(int position) {
        
        Bundle args = new Bundle();
        args.putInt("position", position);

        DeleteAlertDialog fragment = new DeleteAlertDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        POSITION = getArguments().getInt("position");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete")
                .setMessage("Are you sure you want to delete this map?")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onYesClicked(POSITION);
                    }
                });
        return builder.create();
    }

    public interface DeleteAlertDialogListener{
        void onYesClicked(int position);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DeleteAlertDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    +" must implement DeleteAlertDialogListener");
        }

    }
}

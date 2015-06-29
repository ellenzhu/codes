/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.utilities;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import cc.softwarefactory.lokki.android.MainApplication;
import cc.softwarefactory.lokki.android.R;

public class DialogUtils {

    private static final String TAG = "DialogUtils";

    // TODO: Move this method to the class that calls it
    public static void addContact(final Context context) {

        final EditText input = new EditText(context); // Set an EditText view to get user input
        input.setSingleLine(true);
        input.setHint(R.string.contact_email_address);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);

        final AlertDialog addContactDialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.add_contact))
                .setView(input)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        addContactDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                addContactDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Editable value = input.getText();
                                if (value == null || value.toString().isEmpty()) {
                                    input.setError(context.getResources().getString(R.string.required));
                                    return;
                                }

                                String email = value.toString();

                                try {
                                    ServerApi.allowPeople(context, email);
                                    Toast.makeText(context, R.string.contact_added, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                addContactDialog.dismiss();
                            }
                        });
            }
        });

        addContactDialog.show();
    }

    // TODO: Move this method to the class that calls it
    public static void securitySignUp(final Context context) {

        Log.e(TAG, "securitySignUp");
        String title = context.getResources().getString(R.string.app_name);
        String message = context.getResources().getString(R.string.security_sign_up, MainApplication.userAccount);
        showDialog(context, title, message);
    }


    public static void generalError(final Context context) {

        Log.e(TAG, "generalError");
        showDialog(context, R.string.app_name, R.string.general_error);
    }

    private static void showDialog(final Context context, int title, int message) {
        showDialog(context,context.getResources().getString(title), context.getResources().getString(message));
    }

    private static void showDialog(final Context context, String title, String message) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void addPlace(final Context context, final LatLng latLng, final int radius) {
        final EditText input = new EditText(context); // Set an EditText view to get user input
        input.setSingleLine(true);
        final AlertDialog addPlaceDialog = new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.write_place_name))
                .setView(input)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create();

        addPlaceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((FragmentActivity)context).findViewById(R.id.add_place_overlay).setVisibility(View.INVISIBLE); // todo maybe re enabled this... it will however also fire on empty input
            }
        });
        addPlaceDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                addPlaceDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                Editable value = input.getText();
                                if (value == null || value.toString().isEmpty()) {
                                    input.setError(context.getResources().getString(R.string.required));
                                    return;
                                }

                                try {
                                    ServerApi.addPlace(context, value.toString(), latLng, radius);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                addPlaceDialog.dismiss();
                            }
                        });
            }
        });

        addPlaceDialog.show();
    }
}

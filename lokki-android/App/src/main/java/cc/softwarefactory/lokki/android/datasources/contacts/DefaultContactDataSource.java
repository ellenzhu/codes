package cc.softwarefactory.lokki.android.datasources.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DefaultContactDataSource implements ContactDataSource {

    private static final String TAG = "DefaultContactDataSrc";

    public JSONObject getContactsJson(Context context) {

        JSONObject contactsObj = new JSONObject();
        JSONObject mapping = new JSONObject();

        Cursor emailsCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, null);
        if (emailsCursor == null) {
            return null;
        }

        while (emailsCursor.moveToNext()) {
            String name = emailsCursor.getString(emailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME_PRIMARY));
            String email = emailsCursor.getString(emailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            long contactId = emailsCursor.getLong(emailsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.CONTACT_ID));

            if (email == null || email.isEmpty() || email.equals(name) || contactsObj.has(email)) {
                continue;
            }

            try {
                int i = 2;
                String newName = name = name.substring(0, 1).toUpperCase() + name.substring(1);
                while (mapping.has(newName)) {
                    newName = name + " " + i;
                    i++;
                }

                JSONObject contact = new JSONObject()
                        .put("id", contactId)
                        .put("name", newName);

                contactsObj.put(email, contact);
                mapping.put(newName, email);

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        try {
            contactsObj.put("mapping", mapping);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        emailsCursor.close();

        return contactsObj;
    }

}

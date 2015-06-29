/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.avatar;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import cc.softwarefactory.lokki.android.utilities.Utils;

import java.lang.ref.WeakReference;

public class AvatarLoader {

    private Context context;
    private static final String TAG = "AvatarLoader";

    public AvatarLoader(Context myContext) {

        context = myContext;
    }

    public void load(String email, ImageView imageView) {
        Log.e(TAG, "1) load: " + email);
        if (!cancelPotentialWork(email, imageView)) {
            return;
        }

        Log.e(TAG, "load: Creating new task");
        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        final WeakReference<BitmapWorkerTask> taskReference = new WeakReference<>(task);
        imageView.setTag(taskReference);
        task.execute(email);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private String data = "";

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {

            Log.e(TAG, "BitmapWorkerTask: doInBackground: " + params[0]);
            data = params[0];
            return processData(data);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            Log.e(TAG, "BitmapWorkerTask: onPostExecute");
            if (isCancelled()) {
                return;
            }

            final ImageView imageView = imageViewReference.get();
            if (imageView == null) {
                return;
            }

            BitmapWorkerTask task = getTaskFromView(imageView);

            if (this == task) {
                imageView.setImageBitmap(bitmap);
                imageView.setTag(data);
            }
        }
    }

    private Bitmap processData(String email) {

        Log.e(TAG, "processData");
        return Utils.getPhotoFromEmail(context, email);
    }

    public static boolean cancelPotentialWork(String data, ImageView imageView) {

        BitmapWorkerTask task = getTaskFromView(imageView);

        if (task == null) {
            Log.e(TAG, "cancelPotentialWork: No task associated with the ImageView, or an existing task was cancelled"); // No task associated with the ImageView, or an existing task was cancelled
            return true;
        }

        if (!task.data.equals(data)) {
            Log.e(TAG, "cancelPotentialWork: Cancel previous task"); // Cancel previous task
            task.cancel(true);
            return true;
        }
        Log.e(TAG, "cancelPotentialWork: The same work is already in progress"); // The same work is already in progress
        return false;
    }

    private static BitmapWorkerTask getTaskFromView(ImageView imageView) {

        if (imageView == null || !(imageView.getTag() instanceof WeakReference)) {
            return null;
        }
        return ((WeakReference<BitmapWorkerTask>) imageView.getTag()).get();
    }

}

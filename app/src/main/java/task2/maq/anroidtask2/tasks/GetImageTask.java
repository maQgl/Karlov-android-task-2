package task2.maq.anroidtask2.tasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import task2.maq.anroidtask2.data.DataManager;
import task2.maq.anroidtask2.ui.PostActivity.PostsActivity;

public class GetImageTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<PostsActivity> activityWeakReference;

    private DataManager dataManager;

    private String filename;

    private int position;

    public GetImageTask(DataManager dataManager, String filename, int position,
                        PostsActivity activity) {
        this.dataManager = dataManager;
        this.filename = filename;
        this.position = position;
        activityWeakReference = new WeakReference<PostsActivity>(activity);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Bitmap b = dataManager.getImage(filename);
        if (b != null) {
            dataManager.putImageIntoCache(filename, b);
        } else {
            Log.e("app3", "GetImageTask: bitmap is null - " + filename);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PostsActivity activity = activityWeakReference.get();
        if (activity != null) {
            activity.notifyItemChanged(position);
            activity.removeTaskFromCurrents(this);
        }
    }
}

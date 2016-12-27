package task2.maq.anroidtask2.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import task2.maq.anroidtask2.data.DataManager;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.ui.PostAddActivity.PostAddActivity;

public class SendPostTask extends AsyncTask<Void, Void, Void>{

    private WeakReference<PostAddActivity> activityWeakReference;
    private Post post;
    private DataManager dataManager;

    public SendPostTask(PostAddActivity activity, DataManager dataManager, Post post) {
        this.post = post;
        this.dataManager = dataManager;
        activityWeakReference = new WeakReference<PostAddActivity>(activity);
    }

    @Override
    protected Void doInBackground(Void... params) {
        dataManager.sendPost(post);
        return null;
    }

    @Override
    protected void onPostExecute(Void res) {
        super.onPostExecute(res);
        PostAddActivity activity = activityWeakReference.get();
        if (activity != null) {
            activity.finish();
        }
    }
}

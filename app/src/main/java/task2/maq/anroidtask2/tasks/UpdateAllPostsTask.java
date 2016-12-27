package task2.maq.anroidtask2.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import task2.maq.anroidtask2.data.DataManager;
import task2.maq.anroidtask2.data.local.PostsObserver;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.ui.PostActivity.PostsActivity;

public class UpdateAllPostsTask extends AsyncTask<Void, List<Post>, Void> {

    private WeakReference<PostsActivity> activityWeakReference;

    private DataManager dataManager;

    private boolean isFirstPage = true;

    public UpdateAllPostsTask(PostsActivity activity, DataManager dataManager) {
        this.dataManager = dataManager;
        activityWeakReference = new WeakReference<PostsActivity>(activity);
    }

    @Override
    protected Void doInBackground(Void... params) {
        dataManager.updateAllPosts(new PostsObserver() {
            @Override
            public void pushPosts(List<Post> posts) {
                publishProgress(posts);
            }
        });
        return null;
    }

    @Override
    protected void onProgressUpdate(List<Post>... values) {
        super.onProgressUpdate(values);
        PostsActivity activity = activityWeakReference.get();
        if (activity != null) {
            if (isFirstPage) {
                activity.setPosts(new ArrayList<Post>());
                isFirstPage = false;
            }
            activity.addPosts(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        PostsActivity activity = activityWeakReference.get();
        if (activity != null) {
            activity.removeTaskFromCurrents(this);
        }
    }
}

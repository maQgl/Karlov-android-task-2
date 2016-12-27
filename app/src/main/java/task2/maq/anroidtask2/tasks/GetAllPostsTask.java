package task2.maq.anroidtask2.tasks;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import task2.maq.anroidtask2.data.DataManager;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.ui.PostActivity.PostsActivity;

public class GetAllPostsTask extends AsyncTask<Void, Void, List<Post>>{

    private WeakReference<PostsActivity> activityWeakReference;

    private DataManager dataManager;

    public GetAllPostsTask(PostsActivity activity, DataManager dataManager) {
        this.dataManager = dataManager;
        activityWeakReference = new WeakReference<PostsActivity>(activity);
    }

    @Override
    protected List<Post> doInBackground(Void... params) {
        return dataManager.getPosts();
    }

    @Override
    protected void onPostExecute(List<Post> posts) {
        super.onPostExecute(posts);
        PostsActivity activity = activityWeakReference.get();
        if (activity != null) {
            activity.setPosts(posts);
            activity.removeTaskFromCurrents(this);
            activity.updatePosts();
        }
    }
}

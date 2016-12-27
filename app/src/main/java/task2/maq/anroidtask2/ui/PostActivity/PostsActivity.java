package task2.maq.anroidtask2.ui.PostActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import task2.maq.anroidtask2.MainApp;
import task2.maq.anroidtask2.R;
import task2.maq.anroidtask2.TokenManager;
import task2.maq.anroidtask2.data.DataManager;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.tasks.GetAllPostsTask;
import task2.maq.anroidtask2.tasks.GetImageTask;
import task2.maq.anroidtask2.tasks.UpdateAllPostsTask;
import task2.maq.anroidtask2.ui.PostAddActivity.PostAddActivity;

public class PostsActivity extends AppCompatActivity {

    private TokenManager mTokenManager;

    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter mAdapter;

    private List<AsyncTask> currentTasks;

    private DataManager dataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = ((MainApp)getApplication()).getDataManager();
        setContentView(R.layout.posts_layout);
        mTokenManager = ((MainApp) getApplication()).getTokenManager();
        currentTasks = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.post_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddPostActivity();
            }
        });
    }

    private void startAddPostActivity(){
        Intent intent = new Intent(this, PostAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPosts();
    }

    private void getPosts() {
        GetAllPostsTask task =
                new GetAllPostsTask(this, dataManager);
        task.execute();
        currentTasks.add(task);
    }

    public void updatePosts() {
        UpdateAllPostsTask task =
                new UpdateAllPostsTask(this, dataManager);
        task.execute();
        currentTasks.add(task);
    }

    public Bitmap getImageFromCache(String filename) {
        return dataManager.getImageFromCache(filename);
    }

    public void getImage(String filename, int pos) {
        GetImageTask task = new GetImageTask(dataManager, filename, pos, this);
        task.execute();
        currentTasks.add(task);
    }

    @Override
    protected void onDestroy() {
        cancelTasks();
        super.onStop();
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void removeTaskFromCurrents(AsyncTask task) {
        currentTasks.remove(task);
    }

    private void cancelTasks() {
        for (AsyncTask task: currentTasks) {
            if (!task.isCancelled()) {
                task.cancel(false);
            }
        }
    }


    public void setPosts(List<Post> postList) {
        mAdapter.setPosts(postList);
    }

    public void addPosts(List<Post> postList) { mAdapter.addPosts(postList);}
}

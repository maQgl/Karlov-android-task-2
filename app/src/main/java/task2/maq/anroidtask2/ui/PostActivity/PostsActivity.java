package task2.maq.anroidtask2.ui.PostActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import task2.maq.anroidtask2.MainApp;
import task2.maq.anroidtask2.R;
import task2.maq.anroidtask2.TokenManager;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.tasks.GetAllPostsTask;

public class PostsActivity extends AppCompatActivity {

    private TokenManager mTokenManager;

    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_layout);
        mTokenManager = ((MainApp) getApplication()).getTokenManager();

        mRecyclerView = (RecyclerView) findViewById(R.id.post_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllPostsTask(this, ((MainApp)getApplication()).getDataManager()).execute();
    }

    public void showPosts(List<Post> postList) {
        mAdapter.setPosts(postList);
    }
}

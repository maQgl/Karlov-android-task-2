package task2.maq.anroidtask2.data;

import android.graphics.Bitmap;

import java.util.List;

import task2.maq.anroidtask2.data.local.PostsObserver;
import task2.maq.anroidtask2.data.pojo.Post;

public interface DataManager {

    List<Post> getPosts();

    void savePost(Post post);

    void updateAllPosts(PostsObserver observer);

    void updateNewPosts();

    void sendPost(Post post);

    Bitmap getImage(String filename);

    void putImageIntoCache(String filename, Bitmap image);

    Bitmap getImageFromCache(String filename);

}

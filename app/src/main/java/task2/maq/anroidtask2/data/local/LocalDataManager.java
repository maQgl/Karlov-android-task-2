package task2.maq.anroidtask2.data.local;

import android.graphics.Bitmap;

import java.util.List;

import task2.maq.anroidtask2.data.pojo.Post;

public interface LocalDataManager {

    List<Post> getPosts();

    void savePosts(List<Post> posts);

    void deleteAllPosts();

    void savePost(Post post);

    boolean isPostSaved(Post post);

    Bitmap getImage(String filename);

    void saveImage(Bitmap image, String filename);

    void putImageIntoCache(String filename, Bitmap image);

    Bitmap getImageFromCache(String filename);

}

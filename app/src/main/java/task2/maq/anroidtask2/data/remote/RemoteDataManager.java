package task2.maq.anroidtask2.data.remote;

import android.graphics.Bitmap;

import java.util.List;

import task2.maq.anroidtask2.data.pojo.Post;

public interface RemoteDataManager {

    int getPageCount();

    List<Post> getPage(int page);

    Bitmap getImage(String filename);

    void sendPost(Post post);
}

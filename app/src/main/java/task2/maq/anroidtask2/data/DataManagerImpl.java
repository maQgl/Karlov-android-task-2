package task2.maq.anroidtask2.data;

import android.graphics.Bitmap;

import java.util.List;

import task2.maq.anroidtask2.data.local.LocalDataManager;
import task2.maq.anroidtask2.data.local.PostsObserver;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.data.remote.RemoteDataManager;

public class DataManagerImpl implements DataManager{

    private RemoteDataManager remoteDataManager;

    private LocalDataManager localDataManager;

    public DataManagerImpl(RemoteDataManager remoteDataManager, LocalDataManager localDataManager) {
        this.remoteDataManager = remoteDataManager;
        this.localDataManager = localDataManager;
    }

    @Override
    public List<Post> getPosts() {
        return localDataManager.getPosts();
    }

    @Override
    public void savePost(Post post) {
        localDataManager.savePost(post);
    }

    @Override
    public void updateAllPosts(PostsObserver observer) {
        int pageCount = remoteDataManager.getPageCount();
        localDataManager.deleteAllPosts();
        for (int i = 1; i <= pageCount; i++) {
            List<Post> posts = remoteDataManager.getPage(i);
            if (observer != null) {
                observer.pushPosts(posts);
            }
            for (Post post: posts) {
                savePost(post);
            }
        }
    }

    @Override
    public void updateNewPosts() {
        int pageCount = remoteDataManager.getPageCount();
        boolean isPostSaved;
        boolean isPostSavedBuf = false;
        for (int i = 0; i <= pageCount; i++) {
            List<Post> posts = remoteDataManager.getPage(i);
            for (Post post: posts) {
                isPostSaved = localDataManager.isPostSaved(post);
                isPostSavedBuf = isPostSaved || isPostSavedBuf;
                savePost(post);
            }
//            if (isPostSavedBuf) return;
        }
    }

    @Override
    public void sendPost(Post post) {
        remoteDataManager.sendPost(post);
    }

    @Override
    public Bitmap getImage(String filename) {
        Bitmap result;
        result = localDataManager.getImage(filename);
        if (result == null) {
            result = remoteDataManager.getImage(filename);
            localDataManager.saveImage(result, filename);
        }
        return result;
    }

    @Override
    public void putImageIntoCache(String filename, Bitmap image) {
        localDataManager.putImageIntoCache(filename, image);
    }

    @Override
    public Bitmap getImageFromCache(String filename) {
        return localDataManager.getImageFromCache(filename);
    }
}
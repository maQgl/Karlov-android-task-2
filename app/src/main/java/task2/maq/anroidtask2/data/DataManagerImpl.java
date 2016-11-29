package task2.maq.anroidtask2.data;

import java.util.List;

import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.data.remote.RemoteDataManager;

public class DataManagerImpl implements DataManager{

    private RemoteDataManager remoteDataManager;

    public DataManagerImpl(RemoteDataManager remoteDataManager) {
        this.remoteDataManager = remoteDataManager;
    }

    @Override
    public List<Post> getAllPosts() {
        return remoteDataManager.getAllPosts();
    }
}

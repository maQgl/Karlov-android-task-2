package task2.maq.anroidtask2.data.local;

import java.util.List;

import task2.maq.anroidtask2.data.pojo.Post;

public interface PostsObserver {

    void pushPosts (List<Post> posts);

}

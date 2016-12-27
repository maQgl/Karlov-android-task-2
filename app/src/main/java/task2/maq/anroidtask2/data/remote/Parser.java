package task2.maq.anroidtask2.data.remote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import task2.maq.anroidtask2.data.pojo.InvalidDataException;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.data.pojo.PostBuilder;
import task2.maq.anroidtask2.data.pojo.User;
import task2.maq.anroidtask2.data.pojo.UserBuilder;

public class Parser {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static List<Integer> parsePostsForUsers(String json) throws JSONException {
        List<Integer> result = new ArrayList<>();
        JSONObject jObj = new JSONObject(json);
        JSONArray arr = jObj.getJSONArray("results");
        int userId;
        for (int i = 0; i < arr.length(); i++) {
            userId = arr.getJSONObject(i).getInt("owner");
            result.add(userId);
        }
        return result;
    }

    public static User parseUser(String json) throws JSONException {
        JSONObject jObj = new JSONObject(json);
        String name = jObj.getString("username");
        String[] split = jObj.getString("url").split("/");
        int id = Integer.parseInt(split[split.length-1]);
        return new UserBuilder().withId(id).withName(name).build();
    }

    public static List<Post> parsePosts(String json, Map<Integer, User> users)
            throws JSONException {
        List<Post> result = new ArrayList<>();
        JSONObject jObj = new JSONObject(json);
        JSONArray arr = jObj.getJSONArray("results");
        String createDate, content, image, author;
        int id, owner;
        PostBuilder postBuilder = new PostBuilder();
        for (int i = 0; i < arr.length(); i++) {
            id = arr.getJSONObject(i).getInt("id");
            owner = arr.getJSONObject(i).getInt("owner");

            String imageUrl = arr.getJSONObject(i).getString("image");
            if (imageUrl.equals("null")) {
                image = null;
            } else {
                String[] splitImageUrl = imageUrl.split("/");
                image = splitImageUrl[splitImageUrl.length - 1];
            }

            createDate = arr.getJSONObject(i).getString("edit_date");
            content = arr.getJSONObject(i).getString("text");
            author = users.get(owner)==null? "" + owner : users.get(owner).getName();
            try {
                result.add(postBuilder
                        .withContent(content)
                        .withAuthor(author)
                        .withCreateDate(createDate, DATE_FORMAT)
                        .withImage(image)
                        .withId(id)
                        .build());
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String generatePostJsonString(Post post) {
        String res = "{\"num_comments\": 0, \"num_likes\": 0, \"text\": \"" + post.getContent() +
                "\"}";
        return res;
    }

    public static int parsePostsCount(String json) throws JSONException {
        JSONObject jObj = new JSONObject(json);
        return jObj.getInt("count");
    }

}

package task2.maq.anroidtask2.data.remote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.data.pojo.PostBuilder;

public class Parser {

    public static List<Post> parseJson(String json) throws JSONException {
        List<Post> result = new ArrayList<>();
        JSONObject jObj = new JSONObject(json);
        JSONArray arr = jObj.getJSONArray("results");
        String author;
        String date;
        String content;
        PostBuilder postBuilder = new PostBuilder();
        for (int i = 0; i < arr.length(); i++) {
            author = arr.getJSONObject(i).getString("owner");
            date = arr.getJSONObject(i).getString("edit_date");
            content = arr.getJSONObject(i).getString("text");
            result.add(postBuilder.withContent(content).withAuthor(author).withDate(date).build());
        }
        return result;
    }

}

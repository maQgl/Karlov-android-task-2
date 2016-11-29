package task2.maq.anroidtask2.data.remote;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import task2.maq.anroidtask2.data.pojo.Post;

public class RemoteDataManagerImpl implements RemoteDataManager {

    private final int statusOk = 200;

    private String host;

    private String postsUrl;

    public  RemoteDataManagerImpl (String host, String postsUrl) {
        this.host = host;
        this.postsUrl = postsUrl;
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> result = new ArrayList<Post>();
        try {
            String urlString = host+postsUrl;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();
            if (status == statusOk) {
                return Parser.parseJson(readRequestResult(connection));
            }
        } catch (IOException e) {
            //TODO handle exception
            Log.i("app3", "getAllPostt remote IOException");
        } catch (JSONException e) {
            //TODO handle exception
            Log.i("app3", "getAllPostt remote JSONException");
        }
        return result;
    }

    private String readRequestResult(HttpURLConnection connection) throws IOException {
        BufferedReader in =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}

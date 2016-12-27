package task2.maq.anroidtask2.data.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import task2.maq.anroidtask2.TokenManager;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.data.pojo.User;

public class RemoteDataManagerImpl implements RemoteDataManager {

    public static final int STATUS_OK = 200;

    private String host;

    private String postsUrl;

    private String usersUrl;

    private String imagesUrl;

    private TokenManager tokenManager;

    public  RemoteDataManagerImpl (String host, String postsUrl, String usersUrl,
                                   TokenManager tokenManager, String imagesUrl) {
        this.host = host;
        this.postsUrl = postsUrl;
        this.usersUrl = usersUrl;
        this.tokenManager = tokenManager;
        this.imagesUrl = imagesUrl;
    }

    @Override
    public List<Post> getPage(int page) {
        List<Post> result = new ArrayList<>();
        try {
            String urlString = host+postsUrl+"?page="+page;
            HttpURLConnection connection = getConnection(urlString);
            int status = connection.getResponseCode();
            if (status == STATUS_OK) {
                String requestResult = readRequestResult(connection);
                Map<Integer, User> users = getUsersByIds(Parser.parsePostsForUsers(requestResult));
                return Parser.parsePosts(requestResult, users);
            }
        } catch (JSONException e) {
            //TODO handle exception
            Log.e("app3", "getAllPosts remote JSONException");
        } catch (IOException e) {
            //TODO handle exception
            Log.e("app3", "getAllPosts remote IOException");
        }
        return result;
    }

    @Override
    public Bitmap getImage(String filename) {
        try {
            String urlString = host+imagesUrl+filename;
            HttpURLConnection connection = getConnection(urlString);
            int status = connection.getResponseCode();
            if (status == STATUS_OK) {
                InputStream is = connection.getInputStream();
                BitmapFactory.Options opt = new BitmapFactory.Options();
                return BitmapFactory.decodeStream(is, null, opt);
            }
        } catch (IOException e) {
            //TODO handle exception
            Log.e("app3", "getImage remote IOException");
        }
        return null;
    }

    @Override
    public void sendPost(Post post) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(host+postsUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + tokenManager.getToken());
            connection.setRequestProperty("Content-Type", "application/json");
            String str = Parser.generatePostJsonString(post);
            byte[] outputInBytes = str.getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(outputInBytes);
            os.close();
            connection.connect();
            Log.e("app3", connection.getResponseMessage().toString()+" " + str);
        } catch (IOException e) {
            //TODO handle exception
            Log.e("app3", "getConnection remote IOException");
        }
    }

    private Map<Integer, User> getUsersByIds(List<Integer> ids) {
        Map<Integer, User> result = new HashMap<>();
        for (Integer id: ids) {
            if (!result.keySet().contains(id)) {
                User user = getUser(id);
                if (user != null) {
                    result.put(id, user);
                }
            }
        }
        return result;
    }

    private User getUser(int id) {
        try {
            String urlString = host+usersUrl+id+"?access_token="+tokenManager.getToken();
            Log.i("app3", urlString);
            HttpURLConnection connection = getConnection(urlString);
            int status = connection.getResponseCode();
            if (status == STATUS_OK) {
                return Parser.parseUser(readRequestResult(connection));
            }
        } catch (IOException e) {
            //TODO handle exception
            Log.e("app3", "getUser remote IOException");
        } catch (JSONException e) {
            //TODO handle exception
            Log.e("app3", "getUser remote JSONException");
        }
        return null;
    }

    private HttpURLConnection getConnection(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
        } catch (IOException e) {
            //TODO handle exception
            Log.e("app3", "getConnection remote IOException");
        }
        return connection;
    }

    @Override
    public int getPageCount() {
        int result = -1;
        try {
            String urlString = host+postsUrl;
            HttpURLConnection connection = getConnection(urlString);
            int status = connection.getResponseCode();
            if (status == STATUS_OK) {
                result = Parser.parsePostsCount(readRequestResult(connection));
                result = result / 10 + 1;
            }
        } catch (JSONException e) {
            //TODO handle exception
            Log.e("app3", "getAllPosts remote JSONException");
        } catch (IOException e) {
            //TODO handle exception
            Log.e("app3", "getAllPosts remote IOException");
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

package task2.maq.anroidtask2.data.local;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import task2.maq.anroidtask2.data.pojo.InvalidDataException;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.data.pojo.PostBuilder;

public class LocalDataManagerImpl implements LocalDataManager {

    private SQLiteDatabase mDb;

    private Context context;

    private LruCache<String, Bitmap> imageMemoryCache;

    private final String DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";

    public LocalDataManagerImpl(Context context) {
        this.context = context;
        mDb = new DBHelper(context).getWritableDatabase();

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        Log.i("app3", "cacheSize: "+cacheSize);
        imageMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public List<Post> getPosts() {
        return getPostsByCondition(null, null);
    }

    @Override
    public void savePosts(List<Post> posts) {
        for (Post post: posts) {
            savePost(post);
        }
    }

    @Override
    public void deleteAllPosts() {
        mDb.execSQL("delete from " + PostEntry.TABLE_NAME + " where 1=1");
    }

    @Override
    public void savePost(Post post) {
        ContentValues values = new ContentValues();
        values.put(PostEntry.ID_COLUMN, post.getId());
        values.put(PostEntry.AUTHOR_NAME_COLUMN, post.getAuthor());
        values.put(PostEntry.CREATE_DATE_COLUMN, post.getCreateDate().toString(DATE_FORMAT));
        values.put(PostEntry.IMAGE_COLUMN, post.getImage());
        values.put(PostEntry.TEXT_COLUMN, post.getContent());
        mDb.insert(PostEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean isPostSaved(Post post) {
        Post savedPost = getPostById(post.getId());
        if (savedPost == null)
            return false;
        else
            return savedPost.equals(post);
    }

    @Override
    public Bitmap getImage(String filename) {
        if (isExternalStorageGranted())
            return getImageFromExternalStorage(filename);
        else
            return getImageFromInternalStorage(filename);
    }

    @Override
    public void saveImage(Bitmap image, String filename) {
        if (isExternalStorageGranted())
            saveImageToExternalStorage(image, filename);
        else
            saveImageToInternalStorage(image, filename);
    }

    @Override
    public void putImageIntoCache(String filename, Bitmap image) {
        imageMemoryCache.put(filename, image);
    }

    @Override
    public Bitmap getImageFromCache(String filename) {
        return imageMemoryCache.get(filename);
    }

    private void saveImageToExternalStorage(Bitmap image, String filename) {
        String state = Environment.getExternalStorageState();
        if (state == Environment.MEDIA_MOUNTED) {
            File cacheDir = Environment.getExternalStorageDirectory();
            File file = new File(cacheDir,filename);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveImageToInternalStorage(Bitmap image, String filename) {
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir,filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getImageFromInternalStorage(String filename) {
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, filename);
        if (file.exists()) {
            try {
                InputStream is = new FileInputStream(file);
                BitmapFactory.Options opt = new BitmapFactory.Options();
                return BitmapFactory.decodeStream(is, null, opt);
            } catch (FileNotFoundException e) {
                Log.i("app3", "Internal storage. File not found: " + filename);
            }
        }
        return null;
    }

    private Bitmap getImageFromExternalStorage(String filename) {
        String state = Environment.getExternalStorageState();
        if (state == Environment.MEDIA_MOUNTED) {
            File cacheDir = Environment.getExternalStorageDirectory();
            File file = new File(cacheDir, filename);
            if (file.exists()) {
                try {
                    InputStream is = new FileInputStream(file);
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    return BitmapFactory.decodeStream(is, null, opt);
                } catch (FileNotFoundException e) {
                    Log.i("app3", "External storage. File not found: " + filename);
                }
            }
        }
        return null;
    }

    private boolean isExternalStorageGranted() {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED);
    }

    private Post getPostById(int id) {
        String args[] = {"" + id};
        List<Post> onePostList = getPostsByCondition(PostEntry.ID_COLUMN + "=?", args);
        if (onePostList.isEmpty())
            return null;
        else
            return onePostList.get(0);
    }

    private List<Post> getPostsByCondition(String selection, String[] selectionArgs) {
        List<Post> posts = new ArrayList<>();
        String[] columns = {
                PostEntry.ID_COLUMN,
                PostEntry.AUTHOR_NAME_COLUMN,
                PostEntry.TEXT_COLUMN,
                PostEntry.CREATE_DATE_COLUMN,
                PostEntry.IMAGE_COLUMN,
        };
        Cursor c = mDb.query(PostEntry.TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex(PostEntry.ID_COLUMN));
                String authorName = c.getString(c.getColumnIndex(PostEntry.AUTHOR_NAME_COLUMN));
                String text = c.getString(c.getColumnIndex(PostEntry.TEXT_COLUMN));
                String image = c.getString(c.getColumnIndex(PostEntry.IMAGE_COLUMN));
                String createDate =  c.getString(c.getColumnIndex(PostEntry.CREATE_DATE_COLUMN));
                PostBuilder postBuilder = new PostBuilder()
                        .withAuthor(authorName)
                        .withContent(text).withId(id)
                        .withCreateDate(createDate, DATE_FORMAT)
                        .withImage(image);
                try {
                    posts.add(postBuilder.build());
                } catch (InvalidDataException e) {
                    e.printStackTrace();
                }
            }
        }
        if (c != null)
            c.close();
        return posts;
    }
}

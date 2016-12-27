package task2.maq.anroidtask2.ui.PostAddActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import task2.maq.anroidtask2.MainApp;
import task2.maq.anroidtask2.R;
import task2.maq.anroidtask2.data.DataManager;
import task2.maq.anroidtask2.data.pojo.InvalidDataException;
import task2.maq.anroidtask2.data.pojo.Post;
import task2.maq.anroidtask2.data.pojo.PostBuilder;
import task2.maq.anroidtask2.tasks.SendPostTask;

public class PostAddActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 9993;
    private static final int REQUEST_IMAGE_CODE = 9997;

    private ImageView imageView;

    private Uri fileUri;

    private Uri imageUri;

    private SendPostTask sendPostTask;

    private boolean shouldRequestPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_layout);
        Button button = (Button) findViewById(R.id.pick_image);
        imageView = (ImageView) findViewById(R.id.image_view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shouldRequestPermission && !isCameraGranted()) {
                    checkPermission();
                } else {
                    startPickerActivity();
                }
            }
        });

        final DataManager dataManager = ((MainApp) getApplication()).getDataManager();

        final EditText editText = (EditText) findViewById(R.id.edit_text);


        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUriString;
                if (imageUri == null) {
                    imageUriString = null;
                } else {
                    imageUriString = imageUri.toString();
                }
                try {
                    if ( editText.getText()!=null) {
                        Post post = new PostBuilder().withContent(editText.getText().toString())
                                .withImage(imageUriString).build();
                        sendPostTask = new SendPostTask(PostAddActivity.this, dataManager, post);
                        sendPostTask.execute();
                    }
                } catch (InvalidDataException e) {
                    Toast.makeText(PostAddActivity.this, "Empty post", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sendPostTask!=null) {
            sendPostTask.cancel(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        startPickerActivity();
    }

    private void startPickerActivity() {
        startActivityForResult(getPickerIntent(), REQUEST_IMAGE_CODE);
    }

    private Intent getPickerIntent() {
        PackageManager packageManager = getPackageManager();
        List<Intent> intents = new ArrayList<>();
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cams = packageManager.queryIntentActivities(capture, 0);
        if (isCameraGranted()) {
            for (ResolveInfo info : cams) {
                if (fileUri == null) {
                    fileUri = getCameraImageUri();
                }
                Intent intent = new Intent(capture);
                intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                intent.setPackage(info.activityInfo.packageName);
                if (fileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                }
                intents.add(intent);
            }
        }
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        List<ResolveInfo> galleries = packageManager.queryIntentActivities(gallery, 0);
        for (ResolveInfo info : galleries) {
            if (isPermitted(info)) {
                Intent intent = new Intent(gallery);
                intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                intent.setPackage(info.activityInfo.packageName);
                intents.add(intent);
            }
        }

        Intent mainIntent = intents.get(intents.size() - 1);
        for (Intent intent : intents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        intents.remove(mainIntent);

        Intent chooser = Intent.createChooser(mainIntent, "Select source");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));

        return chooser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = getUri(data);
                imageView.setImageURI(uri);
                imageUri = uri;
            } else {
                Toast.makeText(this, "Failed to pick image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermission() {
        if (!isCameraGranted()) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private boolean isPermitted(ResolveInfo info) {
        String permission = info.activityInfo.permission;
        if (permission == null) {
            permission = info.activityInfo.applicationInfo.permission;
        }
        return permission == null ||
                (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isCameraGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED);
    }

    private Uri getUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? fileUri : data.getData();
    }

    private Uri getCameraImageUri() {
        try {
            File imgFile = createTempFile();
            return Uri.fromFile(imgFile);
        } catch (IOException e) {
            return null;
        }
    }

    private File createTempFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

}
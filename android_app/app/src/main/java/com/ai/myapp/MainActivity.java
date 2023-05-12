package com.ai.myapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

//    private EditText textField_message;
    private Button button_send_post;
    private Button button_send_get;
//    private Button button_open_camera;
//
    private TextView textView_response;
    private String url = "http://172.20.10.5:5002";// *****put your URL here*********
    private String POST = "POST";
    private String GET = "GET";
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private Uri contentUri;



    // Define the pic id
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button button_open_camera;
    ImageView click_image_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        button_open_camera = findViewById(R.id.button_open_camera);
        click_image_id = findViewById(R.id.click_image);
        // Camera_open button is for open the camera and add the setOnClickListener in this button
        button_open_camera.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            startActivityForResult(camera_intent, pic_id);
        });

//        textField_message = findViewById(R.id.txtField_message);
        button_send_post = findViewById(R.id.button_send_post);
        button_send_get = findViewById(R.id.button_send_get);
        textView_response = findViewById(R.id.textView_response);

//        button_open_camera = findViewById(R.id.button_open_camera);
        /*making a post request.*/
//        button_send_post.setOnClickListener(view -> {
//            //get the test in the text field.In this example you should type your name here
//            String text = textField_message.getText().toString();
//            if (text.isEmpty()) {
//                textField_message.setError("This cannot be empty for post request");
//            } else {
//                /*if name text is not empty,then call the function to make the post request*/
//                executorService.submit(() -> sendRequest(POST, "getname", "name", text));
//            }
//        });

        /*making the get request*/
        button_send_get.setOnClickListener(view -> {

//            // Check if the camera permission is granted
//            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // If the permission has not been granted, request it
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
//
//            } else {
//                // If the permission has been granted, start the video streaming activity
//                Intent videoStreamingIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//                // Modify the video streaming intent to send the server URL to the activity
//
//                videoStreamingIntent.putExtra("serverUrl", url);
//
//                startActivity(videoStreamingIntent);
//            }

            /*in ourr server.py file we implemented a get method  named "get_fact()".
            We specified its URL invocation as '/getfact' there.
            Here we pass it to the sendRequest() function*/

            executorService.submit(() -> sendRequest(GET, "getfact", null, null));
        });
//        button_open_camera.setOnClickListener(v -> {
//
//            // Check if the camera permission is granted
//            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // If the permission has not been granted, request it
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
//
//            } else {
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                startActivity(cameraIntent);
////                Intent intent = new Intent(this, MainActivity.class);
//                someActivityResultLauncher.launch(cameraIntent);
//
//            }
//        });

    }
    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            RequestBody body = RequestBody.create(MediaType.parse("image/png"), byteArray);
            OkHttpClient client = new OkHttpClient();

            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.png", RequestBody.create(MediaType.parse("image/png"), byteArray));

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addPart(imagePart)
                    .build();

            Request request = new Request.Builder()
                    .url("http://172.20.10.5:5002/analyze-image")
                    .post(requestBody)
                    .build();


                /* this is how the callback get handled */
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        // Read data on the worker thread
                        final String responseData = response.body().string();

                        // Run view-related code back on the main thread.
                        // Here we display the response message in our text view
                        MainActivity.this.runOnUiThread(() -> textView_response.setText(responseData));
                    }
                });



        }
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    private class SendToServerTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmaps[0].compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                MediaType mediaType = MediaType.parse("image/png");
                RequestBody body = RequestBody.create(byteArray, mediaType);
                Request request = new Request.Builder()
                        .url("http://172.20.10.5:5002/analyze-image")
                        .post(body)
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                return responseData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // handle the response here
        }

        public void execute(Bitmap photo) {

        }
    }




    private final ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // handle the result
                    if (data != null) {
                        Log.d(TAG, "Data not null: " + data.getData());
                        if (data.getData() != null) {
                            Log.d(TAG, "Data URI not null: " + data.getData());
                            // TODO: handle the image data here
                        }
                    } else {
                        Log.d(TAG, "Data null");
                    }
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            byte[] bytes = IOUtils.toByteArray(inputStream);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            // save the bitmap as an image file
                            saveBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void saveBitmap(Bitmap bitmap) {
        File file = new File( "C:\\Users\\avita\\Desktop\\AI_APP\\android_python_flask\\android_app\\app\\src\\main\\image");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendRequest(String type, String method, String paramname, String param) {

        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        String fullURL = url + "/" + method + (param == null ? "" : "/" );
        Request request;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        if (type.equals(POST)) {
            RequestBody formBody = new FormBody.Builder()
                    .add(paramname, param)
                    .build();

            request = new Request.Builder()
                    .url(fullURL)
                    .post(formBody)
                    .build();

        } else {
            /*If it's our get request, it doen't require parameters, hence just sending with the url*/
            request = new Request.Builder()
                    .url(fullURL)
                    .build();
        }
        /* this is how the callback get handled */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                // Read data on the worker thread
                final String responseData = response.body().string();

                // Run view-related code back on the main thread.
                // Here we display the response message in our text view
                MainActivity.this.runOnUiThread(() -> textView_response.setText(responseData));
            }
        });
    }
}
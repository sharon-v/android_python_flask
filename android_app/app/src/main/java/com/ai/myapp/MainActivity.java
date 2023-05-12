package com.ai.myapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import java.util.Timer;
import java.util.TimerTask;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private EditText textField_message;
    private Button button_send_post;
    private Button button_send_get;
    private TextView textView_response;
    private ImageView imageView;

    private Timer timer;
    private String url = "http://192.168.1.162:5000";// *****put your URL here*********
    private String POST = "POST";
    private String GET = "GET";
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private ActivityResultLauncher<Intent> mStartForResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textField_message = findViewById(R.id.txtField_message);
        button_send_post = findViewById(R.id.button_send_post);
        button_send_get = findViewById(R.id.button_send_get);
        textView_response = findViewById(R.id.textView_response);
        imageView = findViewById(R.id.imageView);

        // Start the video capture activity
//        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            mStartForResultLauncher.launch(takePictureIntent);
        }

        // Set up the activity result launcher for taking pictures
        mStartForResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(imageBitmap);
                        }
                    }
                });

        // Set up a timer to take a picture every 3 seconds
        timer = new Timer();
        timer.schedule(new TakePictureTask(), 0, 30000); // take a picture every 3 seconds


        /*making a post request.*/
        button_send_post.setOnClickListener(view -> {
            //get the test in the text field.In this example you should type your name here
            String text = textField_message.getText().toString();
            if (text.isEmpty()) {
                textField_message.setError("This cannot be empty for post request");
            } else {
                /*if name text is not empty,then call the function to make the post request*/
                executorService.submit(() -> sendRequest(POST, "getname", "name", text));
            }
        });

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

    }

    void sendRequest(String type, String method, String paramname, String param) {

        /* if url is of our get request, it should not have parameters according to our implementation.
         * But our post request should have 'name' parameter. */
        String fullURL = url + "/" + method + (param == null ? "" : "/" + param);
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

    private class TakePictureTask extends TimerTask {
        private CameraDevice cameraDevice;
        private ImageReader imageReader;
        private static final int IMAGE_WIDTH = 640;
        private static final int IMAGE_HEIGHT = 480;

        public TakePictureTask(CameraDevice cameraDevice) {
            this.cameraDevice = cameraDevice;

            // Initialize an ImageReader to capture the image
            imageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT, ImageFormat.JPEG, 1);
        }

        @Override
        public void run() {
            try {
                // Create a CaptureRequest.Builder and set the necessary parameters
                CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureBuilder.addTarget(imageReader.getSurface());
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                // Capture the image
                CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);

                        // Save the image to a file
                        Image image = imageReader.acquireLatestImage();
                        File file = new File(getFilesDir(), "image.jpg");
                        try {
                            OutputStream outputStream = new FileOutputStream(file);
                            ImageUtils.saveImage(outputStream, image);
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            image.close();
                        }
                    }
                };

                cameraDevice.createCaptureSession(Collections.singletonList(imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        try {
                            session.capture(captureBuilder.build(), captureCallback, null);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        session.close();
                    }
                }, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
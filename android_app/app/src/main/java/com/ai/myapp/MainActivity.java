package com.ai.myapp;//package com.ai.myapp;
//import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.content.Intent;
//
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//
//import android.net.Uri;
//import android.os.Bundle;
//
//import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
//
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.apache.commons.io.IOUtils;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//import android.hardware.Camera;
//Camera2Session session = new Camera2Session(this, cameraId);
//session.takePicture(new File("/sdcard/my_picture.jpg"), new Camera2CaptureSettings.Builder().build());
//
//public class MainActivity extends AppCompatActivity {
//    private TextView textView_response;
//
//
//
//    // Define the pic id
//    private static final int pic_id = 123;
//    // Define the button and imageview type variable
//    Button button_open_camera;
//    ImageView click_image_id;
//
//    private Camera camera;
//
//    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
//        @Override
//        public void onPictureTaken(byte[] data, Camera camera) {
//            // Process the captured image
//            Log.d("Camera", "Picture taken");
//        }
//    };
//
//    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
//        @Override
//        public void onShutter() {
//            Log.d("Camera", "onShutter");
//            // This method is called when the image is captured, but before the picture data is available
//        }
//    };
//
//    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
//        @Override
//        public void onAutoFocus(boolean success, Camera camera) {
//            // This method is called when the camera has finished auto-focusing
//            Log.d("Camera", "onAutoFocus");
//            if (success) {
//                // Take the picture when auto-focus is successful
//                camera.takePicture(shutterCallback, null, pictureCallback);
//            }
//        }
//    };
//
//    private void openCamera() {
//        camera = Camera.open();
//        Camera.Parameters parameters = camera.getParameters();
//        // Set any required parameters for the camera, such as flash mode, picture size, etc.
//        camera.setParameters(parameters);
//        Log.d("Camera", "openCamera");
////        Log.d("Camera",getString(Camera.getNumberOfCameras()));
//        // Set the auto-focus mode
//        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
//        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
//            // Set the auto-focus mode
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//            camera.setParameters(parameters);
//
//            // Start auto-focusing
//            camera.autoFocus(autoFocusCallback);
//        } else {
//            // Autofocus is not supported, handle accordingly
//            // For example, you may choose to skip autofocus and directly capture the image
//            camera.takePicture(shutterCallback, null, pictureCallback);
//        }
//
//        Log.d("Camera", "openCamera-END");
//    }
//
//    private void releaseCamera() {
//        if (camera != null) {
//            camera.release();
//            camera = null;
//
//        }
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
//        button_open_camera = findViewById(R.id.button_open_camera);
//        click_image_id = findViewById(R.id.click_image);
//
//        // Camera_open button is for open the camera and add the setOnClickListener in this button
//        button_open_camera.setOnClickListener(v -> {
////            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////            //Start the activity with camera_intent, and request pic id
////            startActivityForResult(camera_intent, pic_id);
////            openCamera();
//            Camera2Session session = new Camera2Session(this, cameraId);
//            session.takePicture(new File("/sdcard/my_picture.jpg"), new Camera2CaptureSettings.Builder().build());
//        });
//
//
//
//        textView_response = findViewById(R.id.textView_response);
//
//    }
//    // This method will help to retrieve the image
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Match the request 'pic id with requestCode
//        if (requestCode == pic_id && resultCode == Activity.RESULT_OK) {
//            // BitMap is data structure of image file which stores the image in memory
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//
//            // Rotate the photo
//            Matrix matrix = new Matrix();
//            matrix.postRotate(270); // Rotate by 90 degrees (adjust the angle as needed)
//            Bitmap rotatedBitmap = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
//
//            // Create a canvas from the rotated bitmap
//            Canvas canvas = new Canvas(rotatedBitmap);
//
//            // Create a Paint object for drawing text
//            Paint textPaint = new Paint();
//            textPaint.setColor(Color.WHITE);
//            textPaint.setTextSize(30);
//
//            // Define the text to be drawn
//            String text = "Your Text Here";
//            // Calculate the position to draw the text (adjust the coordinates as needed)
//            int x = 50;
//            int y = 50;
//
//            // Draw the text on the canvas
//            canvas.drawText(text, x, y, textPaint);
//
//            // Set the image in the imageview for display
//            click_image_id.setImageBitmap(rotatedBitmap);
//
//            // Convert the rotated bitmap to byte array
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            OkHttpClient client = new OkHttpClient();
//
//            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.png", RequestBody.create(MediaType.parse("image/png"), byteArray));
//
//            RequestBody requestBody = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addPart(imagePart)
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url("http://10.100.102.23:5002/analyze-image")
//                    .post(requestBody)
//                    .build();
//
//            /* this is how the callback get handled */
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(Call call, final Response response) throws IOException {
//                    // Read data on the worker thread
//                    final String responseData = response.body().string();
//                    runOnUiThread(() -> {
//                        // Draw the response text on the canvas
//                        canvas.drawText(responseData, x, y, textPaint);
//                        // Update the image view with the modified bitmap
//                        click_image_id.setImageBitmap(rotatedBitmap);
//                        // Update the text view with the response data
//                        textView_response.setText(responseData);
//                    });
//                }
//            });
//        }
//    }
//
//}
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.camera.view.PreviewView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.ai.myapp.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.MediaStore;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};

    private ExecutorService cameraExecutor;
    private ImageCapture imageCapture;
    private OkHttpClient okHttpClient;
    private Button captureButton;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    ImageView click_image_id;
    private TextView textView_response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        cameraExecutor = Executors.newSingleThreadExecutor();
        okHttpClient = new OkHttpClient();
        captureButton = findViewById(R.id.captureButton);

        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        textView_response = findViewById(R.id.textView_response);
        click_image_id = findViewById(R.id.click_image);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startCamera() {
        PreviewView previewView = findViewById(R.id.previewView);

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider, previewView);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider, PreviewView previewView) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());


        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture);
        // Remove the previewView from its parent ViewGroup
//        ViewGroup parent = (ViewGroup) previewView.getParent();
//        parent.removeView(previewView);
        // Make the previewView transparent
        previewView.setAlpha(0);
        captureButton.setOnClickListener(v -> captureImage());
    }

    private void captureImage() {
        // Create a file with a unique name
        File outputDirectory = getOutputDirectory();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentTime = sdf.format(new Date());
        String fileName = "IMG_" + currentTime + ".jpg";
        File outputFile = new File(outputDirectory, fileName);

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(outputFile).build();
        Log.d("captureImage",outputFile.getAbsolutePath());

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // Image captured and saved to outputFile
                Log.d("onImageSaved","sadsd");
                Toast.makeText(MainActivity.this, "Image saved: " + outputFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                // Read the saved image file and convert it to a Bitmap
                Bitmap photo = BitmapFactory.decodeFile(outputFile.getAbsolutePath());
                // Call your "senttoserver" function
                sendImageToServer(photo, outputFile.getAbsolutePath());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                captureImage();

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // Error occurred while capturing image
                exception.printStackTrace();
            }
        });
    }
    private File getOutputDirectory() {
        // Get the appropriate directory for storing the images.
        File mediaDir = getExternalMediaDirs()[0];
        File outputDirectory = new File(mediaDir, "Camera");

        if (!outputDirectory.exists()) {
            if (!outputDirectory.mkdirs()) {
                return null;
            }
        }
        return outputDirectory;
    }

    private void sendImageToServer(Bitmap photo, String imagePath) {

        Log.d("sendImageToServer",imagePath);

            // BitMap is data structure of image file which stores the image in memory
//            Bitmap photo = pic.get("data");

            // Rotate the photo
            Matrix matrix = new Matrix();
            matrix.postRotate(270); // Rotate by 90 degrees (adjust the angle as needed)
            Bitmap rotatedBitmap = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);

            // Create a canvas from the rotated bitmap
            Canvas canvas = new Canvas(rotatedBitmap);

            // Create a Paint object for drawing text
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(30);

            // Define the text to be drawn
//            String text = "Your Text Here";
            // Calculate the position to draw the text (adjust the coordinates as needed)
            int x = 50;
            int y = 50;

            // Draw the text on the canvas
//            canvas.drawText(text, x, y, textPaint);

            // Set the image in the imageview for display
            click_image_id.setImageBitmap(rotatedBitmap);

            // Convert the rotated bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

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
                    runOnUiThread(() -> {
                        // Extract the text from the response data
                        String extractedText = responseData.replace("Your Text Here", "").trim();

                        // Draw the extracted text on the canvas
                        canvas.drawText(extractedText, x, y, textPaint);

                        // Update the image view with the modified bitmap
                        click_image_id.setImageBitmap(rotatedBitmap);

                        // Update the text view with the extracted text
                        textView_response.setText(extractedText);
                    });
                }

            });
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                startCamera();
            }

            surfaceView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}

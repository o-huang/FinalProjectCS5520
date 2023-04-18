package edu.northeastern.finalprojectcs5520;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_STORAGE_PERMISSION = 300;

    private String currentPhotoPath;
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    int position;
    String recordDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        currentUser = auth.getCurrentUser();

        position = getIntent().getIntExtra("position", -1);
        recordDate = getIntent().getStringExtra("date"); // Retrieve the date from the intent extras
        if (hasCameraPermission()) {
            dispatchTakePictureIntent();
//            if (hasStoragePermission()) {
//                dispatchTakePictureIntent();
//                System.err.println("Here1");
//            } else {
//                requestStoragePermission();
//                System.err.println("Here2");
//            }
        } else {
            requestCameraPermission();
            System.err.println("Here3");
        }
    }


    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean hasStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                if (hasStoragePermission()) {
//                    dispatchTakePictureIntent();
//                } else {
//                    requestStoragePermission();
//                }
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Storage permission is required to save images.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



    private void dispatchTakePictureIntent() {
        System.err.println("Hello from dispatchTakePictureIntent()");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e("CameraActivity", "Error creating image file", ex);
        }

        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this, "edu.northeastern.finalprojectcs5520.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
//    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");

        String email = currentUser.getEmail();
        String username = email.split("@")[0];

        // Use the 'recordDate' instead of the 'currentDate'
        reference.child(username).child("recordWeights").child(recordDate).child("imageUrl").setValue(imageUrl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            uploadImageToFirebaseStorage(Uri.fromFile(new File(currentPhotoPath)));

            // Create a Uri object from currentPhotoPath and pass it to saveImageToGallery()
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
            saveImageToGallery(imageUri);

        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // Upload the selected image to Firebase Storage
            uploadImageToFirebaseStorage(selectedImageUri);
        }
    }


    private void saveImageToGallery(Uri imageUri) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Image1");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "New Image1");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera1");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image1/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        Uri insertedImageUri = getContentResolver().insert(externalContentUri, values);
        if (insertedImageUri != null) {
            try (OutputStream outputStream = getContentResolver().openOutputStream(insertedImageUri)) {
                if (outputStream != null) {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    if (inputStream != null) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                    }
                }
            } catch (IOException e) {
                getContentResolver().delete(insertedImageUri, null, null);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear();
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                getContentResolver().update(insertedImageUri, values, null, null);
            }
        }
    }


    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference imageRef = storageRef.child("images/" + currentUser.getUid() + "/" + imageUri.getLastPathSegment());

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CameraActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Toast.makeText(CameraActivity.this, "Upload success", Toast.LENGTH_SHORT).show();
            saveImageUrlToDatabase(uri.toString());
            Intent resultIntent = new Intent();
            resultIntent.putExtra("position", position);
            resultIntent.putExtra("image_url", uri.toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        }));
    }
}


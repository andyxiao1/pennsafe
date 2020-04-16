package edu.upenn.cis350.cis350project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Base64;

public class AccountActivity extends AppCompatActivity {
    private final int RESULT_SELECT_IMAGE = 1;
    private String imageInfo;
    private String val = "http://10.0.2.2:3000/";

    private TextView username;
    private TextView password;

    private TextView address;
    private TextView personalInfo;

    private ImageView buttonLoadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getPermission();

        username = (TextView) findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);

        address = (TextView) findViewById(R.id.address);
        personalInfo = (TextView) findViewById(R.id.personalInfo);

        buttonLoadImage = (ImageView) findViewById(R.id.imageButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                selectImageFromGallery();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == RESULT_OK)
            switch (requestCode){
                case RESULT_SELECT_IMAGE:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                            null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imagePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    bitmap = Bitmap.createBitmap(bitmap, 400, 500, 300, 300);
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    byte[] b = os.toByteArray();
                    imageInfo = (String) Base64.getUrlEncoder().encodeToString(b);

                    // Set the Image in ImageView after decoding the String
                    buttonLoadImage.setImageBitmap(bitmap);
                    break;

            }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_SELECT_IMAGE);
    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    public void onSubmitButtonClick(View view) {
        String id = (String) username.getText().toString();
        if (imageInfo != null) {
            if (!imageInfo.isEmpty() && !id.isEmpty()) {
                try {
                    URL[] urls = new URL[2];
                    urls[1] = new URL(val + "create");
                    urls[0] = new URL(val + "image");

//                    UploadPerson task = new UploadPerson();
//                    task.setImage(imageInfo);
//
//                    String addressString = address.getText().toString();
//                    String infoString = personalInfo.getText().toString();
//
//                    task.setMedicalInfo(addressString, id);
//                    task.setPersonalInfo(infoString);
//                    task.execute(urls);
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }
        }
    }

    public void onExtraImageButtonClick(View view) {
//        String id = ((TextView) findViewById(R.id.id)).getText().toString();
//        if (id != null && !id.isEmpty()) {
//            try {
//                String val = "http://10.0.2.2:3000/api/image?name=" + id;
//                URL url = new URL(val);
////                DecodeImgTask task = new DecodeImgTask();
////                task.execute(url);
////                String resposne = task.get();
////                Log.d("Response", resposne);
////                byte[] bm = Base64.getUrlDecoder().decode(resposne);
////                Bitmap decodedByte = BitmapFactory.decodeByteArray(bm, 0, bm.length);
////                buttonLoadImage.setImageBitmap(decodedByte);
//            } catch (Exception e) {
//                Log.e("Start", e.toString());
//            }
//        }
    }

    public void onDecodeButtonClick(View view) {
    }
}

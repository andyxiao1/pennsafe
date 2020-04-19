package edu.upenn.cis350.cis350project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import edu.upenn.cis350.cis350project.api.APIHandler;
import edu.upenn.cis350.cis350project.api.APIResponse;
import edu.upenn.cis350.cis350project.api.APIResponseWrapper;
import edu.upenn.cis350.cis350project.api.UserData;
import edu.upenn.cis350.cis350project.api.UserDataAPIResponse;

public class AccountActivity extends AppCompatActivity {
    private final int RESULT_SELECT_IMAGE = 1;
    private final String regex_email ="^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    private final String regex_phone = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";

    private String imageInfo;
    private String user;
    private TextView username;
    private ImageView buttonLoadImage;
    private Button buttonSubmit;
    private TextView warningText;

    LinkedList<TextView[]> views = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        getPermission();

        user = getIntent().getStringExtra("username");
        username = findViewById(R.id.username);
        username.setText(user);
        setValues();
        addViews();

        buttonSubmit = (Button) findViewById(R.id.submit);

        warningText = findViewById(R.id.warning_text);
        buttonLoadImage = (ImageView) findViewById(R.id.imageButton);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                selectImageFromGallery();
            }
        });
    }

    private void addViews() {
        TextView[] emails = {findViewById(R.id.email), findViewById(R.id.email_text)};
        TextView[] numbers = {findViewById(R.id.telephone), findViewById(R.id.telephone_text)};
        TextView[] addresses = {findViewById(R.id.address), findViewById(R.id.address_text)};
        TextView[] personal = {findViewById(R.id.personal), findViewById(R.id.personal_text)};

        views.add(emails);
        views.add(numbers);
        views.add(addresses);
        views.add(personal);
    }

    private void setValues() {
        final APIHandler apiHandler = new APIHandler();
        apiHandler.getUserData(user, new APIResponseWrapper() {
            @Override
            public void onResponse(APIResponse response) {
                UserDataAPIResponse dataResponse = (UserDataAPIResponse) response;
                if (dataResponse != null && dataResponse.getUserData() != null) {
                    UserData userData = dataResponse.getUserData();
                    String email = userData.getEmail();
                    String telephone = userData.getTelephone();
                    String address = userData.getAddress();
                    String image = userData.getImage();

                    if (email != null) ((TextView) findViewById(R.id.email_text)).setText(email);
                    if (telephone != null)
                        ((TextView) findViewById(R.id.telephone_text)).setText(telephone);
                    if (address != null)
                        ((TextView) findViewById(R.id.address_text)).setText(address);
                    if (image != null) DecodeImage(image);
                }
            }
        });
    }

    public void DecodeImage(String s) {
        byte[] bm = Base64.getUrlDecoder().decode(s);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bm, 0, bm.length);
        decodedByte = getRoundedCornerBitmap(decodedByte, 100);
        buttonLoadImage.setImageBitmap(decodedByte);
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

    private void selectImageFromGallery() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_SELECT_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK) {
            Bitmap bm = getBitmap(data.getData());
            imageInfo = encodeBitmap(bm);
            buttonLoadImage.setImageBitmap(bm);
            if (imageInfo != null) {
                APIHandler apiHandler = new APIHandler();
                apiHandler.sendImage(user, imageInfo);
            }
        }
    }

    private Bitmap getBitmap(Uri uri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, filePathColumn,
                null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagePath = cursor.getString(columnIndex);
        cursor.close();

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        bitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2 - 100,
                300, 300);
        return getRoundedCornerBitmap(bitmap, 100);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private String encodeBitmap(Bitmap bm) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] b = os.toByteArray();
        return (String) Base64.getUrlEncoder().encodeToString(b);
    }

    public void onUpdateClick(View view) {
        view.setVisibility(View.INVISIBLE);

        for (TextView[] v: views) {
            setTextVisibility(v[0], v[1]);
            v[0].setText(v[1].getText().toString());
        }

        buttonSubmit.setVisibility(View.VISIBLE);
    }

    private void setTextVisibility(TextView ev, TextView tv) {
        ev.setVisibility(View.VISIBLE);
        tv.setVisibility(View.INVISIBLE);
    }

    public void onSubmitButtonClick(View view) {

        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String telephone = ((EditText) findViewById(R.id.telephone)).getText().toString();
        String address = ((EditText) findViewById(R.id.address)).getText().toString();

        if (email == null || telephone == null || address == null) {
            warningText.setText("Please fill out all fields Properly.");
            return;
        }

        if (!isValidRegex(email, regex_email) && email.length() != 0) {
            warningText.setText("Incorrect Email Format");
            return;
        }

        if (!isValidRegex(telephone, regex_phone) && telephone.length() != 0) {
            warningText.setText("Incorrect Telephone Format");
            return;
        } else {
            telephone = telephone.replaceFirst("(\\(?\\d{3}\\)?)-?(\\d{3})-?(\\d+)", "$1-$2-$3");
            ((EditText) findViewById(R.id.telephone)).setText(telephone);
        }

        APIHandler apiHandler = new APIHandler();
        apiHandler.sendUpdatedData(user, email, telephone, address);
        disableEditView();
    }

    public void disableEditView() {
        warningText.setText("");
        findViewById(R.id.submit).setVisibility(View.INVISIBLE);
        findViewById(R.id.update).setVisibility(View.VISIBLE);

        for (TextView[] v: views) {
            setTextVisibility(v[1], v[0]);
            String s = v[0].getText().toString();
            if (s.length() == 0) {
                v[1].setText("Not Available");
            } else {
                v[1].setText(s);
            }
        }
    }

    public static boolean isValidRegex(String s, String regex) {
        Pattern pat = Pattern.compile(regex);
        if (s == null)
            return false;
        return pat.matcher(s).matches();
    }

    public void onEndClick(View view) {
        finish();
    }
}

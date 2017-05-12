package com.example.ee4901.project2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.*;
import android.content.Intent;
import android.view.View;
import android.os.AsyncTask;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okio.Buffer;

public class MainActivity extends AppCompatActivity {
    GoodTask goodTask;
    String Uri_string;
    double longitude;
    double latitude;
    String email;
    ImageView loadImg;
    /*
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent test;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    test = new Intent(MainActivity.this, Scroll_DB.class);
                    test.putExtra("email", email);
                    MainActivity.this.startActivity(test);
                    return true;
                case R.id.navigation_upload:
                    return true;
                case R.id.navigation_search:
                    test = new Intent(MainActivity.this, SearchActivity.class);
                    test.putExtra("email", email);
                    MainActivity.this.startActivity(test);
                    return true;
                case R.id.navigation_account:
                    test = new Intent(MainActivity.this, user_info.class);
                    test.putExtra("email", email);
                    MainActivity.this.startActivity(test);
                    return true;
            }
            return false;
        }

    };
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        email = "test";
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener(email, this));
        navigation.getMenu().getItem(1).setChecked(true);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        try {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } catch (Exception e) {
            System.out.println(e);
            longitude = 40.809281;
            latitude = -73.959754;
        }
        final LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                longitude = location.getLongitude();
//                latitude = location.getLatitude();
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onProviderDisabled(String provider) {
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        System.out.println(longitude);
        System.out.println(latitude);

        Button uploadBtn = (Button)  findViewById(R.id.uploadButton);
        Button chooseBtn = (Button)  findViewById(R.id.chooseButton);

        loadImg = (ImageView) findViewById(R.id.imageView);
//        loadImg =  new ImageView(this);
//        LinearLayout ll = (LinearLayout) findViewById(R.id.Ｍainlayout);
//        ll.addView(loadImg);
        final EditText inputContact   = (EditText)findViewById(R.id.inputContact);
        final EditText inputName   = (EditText)findViewById(R.id.inputName);
        final EditText inputPrice   = (EditText)findViewById(R.id.inputPrice);
        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Uri_string==null){

                }
                else if (goodTask == null) {
                    // Store values at the time of the login attempt.
                    String price = inputPrice.getText().toString();
                    String contact = inputContact.getText().toString();

                    if (TextUtils.isEmpty(price)) {
                        inputPrice.setError(getString(R.string.error_field_required));
                        inputPrice.requestFocus();
                    } else if (TextUtils.isEmpty(contact)) {
                        inputContact.setError(getString(R.string.error_field_required));
                        inputContact.requestFocus();
                    } else {
                        goodTask = new GoodTask();

                        goodTask.execute(inputName.getText().toString(), inputPrice.getText().toString(), inputContact.getText().toString(), String.valueOf(longitude), String.valueOf(latitude));
                        goodTask = null;
                        Intent test = new Intent(MainActivity.this, user_info.class);
                        test.putExtra("email", email);
                        MainActivity.this.startActivity(test);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                loadImg.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Uri_string = getRealPathFromURI(targetUri);
            System.out.println(Uri_string);

        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentURI, projection, null, null, null);
//        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    public File resize(File orgFile) throws IOException {
        try {
            Log.d("TAG here", "File:" + orgFile + " : " + orgFile.exists());
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 8;
            FileInputStream inputStream = new FileInputStream(orgFile);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            final int REQUIRED_SIZE = 50;
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(orgFile);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            System.out.println(Uri_string);
            File newFile = new File("/storage/emulated/0/DCIM/Camera/hahaha.jpg");
            if (!newFile.exists()){
                newFile.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(newFile);
            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//            System.out.println(newFile.length());
            return newFile;
        } catch (Exception e) {
            System.out.println("ohohoho");
            System.out.println(e);
            return null;
        }
    }

    private class GoodTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... str) {
            String res = null;
            String ip = getString(R.string.ip_addr);
            String url = "http://" + ip + ":3000/item/upload";
            System.out.println(Uri_string);
            File sourceFile = new File(Uri_string);
            System.out.println("before"+sourceFile.length());
            try {
                sourceFile = resize(sourceFile);
                System.out.println("after"+sourceFile.length());
                Log.d("TAG", "File:" + sourceFile + " : " + sourceFile.exists());
            } catch (IOException e) {
                e.printStackTrace();
            }
            OkHttpClient client = new OkHttpClient();
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            String filename = Uri_string.substring(Uri_string.lastIndexOf("/") + 1);
            Log.d("filename", filename);
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("result", "my_image")
                    .addFormDataPart("user_id", email)
                    .addFormDataPart("ItemName", str[0])
                    .addFormDataPart("ItemPrice", str[1])
                    .addFormDataPart("ContactInfo", str[2])
                    .addFormDataPart("longtitude", str[3])
                    .addFormDataPart("latitude", str[4])
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),
                    "Image Has Been Uploaded", Toast.LENGTH_SHORT).show();
        }
    }
}

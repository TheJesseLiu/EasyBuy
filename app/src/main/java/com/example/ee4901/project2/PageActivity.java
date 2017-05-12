package com.example.ee4901.project2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;

public class PageActivity extends AppCompatActivity {

    GetUserName goodTask;
    private String url;
    private String ip;
    private String email;
    private String userName;
    private TextView nameView;
    private TextView emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        emailView = (TextView) findViewById(R.id.email);
        emailView.setText("Your E-mail: " + email);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener(email, this));
        navigation.getMenu().getItem(3).setChecked(true);

        ip = getString(R.string.ip_addr);
        url = "http://" + ip + ":3000/auth/getName";

        goodTask = new GetUserName();
        goodTask.execute();


    }

    private class GetUserName extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... input) {
            String url = "http://" + ip + ":3000/auth/getName";
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                JSONObject jsonObj = new JSONObject(jsonData);
                System.out.println(jsonObj);
                userName = jsonObj.getString("fullname");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                System.out.println(e);
            }
            return "string";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(userName);
            nameView = (TextView) findViewById(R.id.name);
            nameView.setText("Your Name: " + userName);
        }
    }
}

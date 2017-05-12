package com.example.ee4901.project2;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
//import android.support.v7.app.AlertController;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Scroll_DB extends AppCompatActivity{
    GoodTask goodTask;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List <MyData> data_List;
    private MaterialSearchView searchView;
    private String ip;
    private String url;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll__db);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener(email, this));
        navigation.getMenu().getItem(0).setChecked(true);

        ip = getString(R.string.ip_addr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setVoiceSearch(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
//                        .show();
                System.out.println(query);
                data_List = new ArrayList<MyData>();
                adapter = new CustomAdapter(Scroll_DB.this, data_List, new CustomAdapter.OnItemClickListener() {
                    @Override public void onItemClick(MyData item, View view) {
                        System.out.println("enter item click listener");
                        //Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                        System.out.println(item.getItemName());
                        Intent intent = new Intent(Scroll_DB.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_PARAM_NAME, item.getItemName());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_PRICE, item.getPrice());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_CONTACT, item.getContact());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_LINK, item.getLink());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_LONGITUDE, item.getLongitude());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_LATITUDE, item.getLatitude());
                        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                Scroll_DB.this,

                                // Now we provide a list of Pair items which contain the view we can transitioning
                                // from, and the name of the view it is transitioning to, in the launched activity
                                new Pair<View, String>(view.findViewById(R.id.image),
                                        DetailActivity.VIEW_NAME_HEADER_IMAGE),
                                new Pair<View, String>(view.findViewById(R.id.description),
                                        DetailActivity.VIEW_NAME_HEADER_TITLE));

                        System.out.println("click item:" + item.getItemName());
                        // Now we can start the Activity, providing the activity options as a bundle
                        ActivityCompat.startActivity(Scroll_DB.this, intent, activityOptions.toBundle());
                        //startActivity(intent);
                        // END_INCLUDE(start_activity)
                    }
                });
                recyclerView.setAdapter(adapter);
                if (goodTask == null) {
                    url = "http://" + ip + ":3000/search/title/description/" + query;
                    System.out.println(url);
                    goodTask = new GoodTask();
                    goodTask.execute(10);
                    System.out.println("haha");
                    goodTask = null;
                }
                //adapter.notifyDataSetChanged();
//                final TextView mTxtDisplay;
//                ImageView mImageView;
//                mTxtDisplay = (TextView) findViewById(R.id.textViewTest);
//
//                url = "http://" + ip + ":3000/search/title/description/" + query;
//                System.out.println(query);
//                AsyncHttpClient client = new AsyncHttpClient();
//
//                client.get(url, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        if (responseBody == null) {
//                            mTxtDisplay.setText("Empty");
//                            return;
//                        }
//                        String test = "";
//                        //success response, do something with it!
//                        String response = new String(responseBody);
//                        System.out.println(response);
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//
//                            // Getting JSON Array node
//                            JSONArray posts = jsonObj.getJSONArray("hits");
//                            System.out.println(posts);
//
//                            // looping through All Contacts
//                            for (int i = 0; i < posts.length(); i++) {
//                                JSONObject c = posts.getJSONObject(i);
//
//                                String title = c.getString("title");
//                                String description = c.getString("description");
////                                String url = c.getString("url");
//                                test = title + description;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        mTxtDisplay.setText(test);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        mTxtDisplay.setText("Network error");
//                    }
//
//                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_List = new ArrayList<MyData>();
        System.out.println("yoyoyo");
        if (goodTask == null) {
            url = "http://" + ip + ":3000/item/download";
            goodTask = new GoodTask();
            goodTask.execute(10);
            System.out.println("haha");
            goodTask = null;
        }
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(this, data_List, new CustomAdapter.OnItemClickListener() {
            @Override public void onItemClick(MyData item, View view) {
                System.out.println("enter item click listener");
                //Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                System.out.println(item.getItemName());
                Intent intent = new Intent(Scroll_DB.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_PARAM_NAME, item.getItemName());
                intent.putExtra(DetailActivity.EXTRA_PARAM_PRICE, item.getPrice());
                intent.putExtra(DetailActivity.EXTRA_PARAM_CONTACT, item.getContact());
                intent.putExtra(DetailActivity.EXTRA_PARAM_LINK, item.getLink());

                intent.putExtra(DetailActivity.EXTRA_PARAM_LONGITUDE, item.getLongitude());
                intent.putExtra(DetailActivity.EXTRA_PARAM_LATITUDE, item.getLatitude());
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        Scroll_DB.this,

                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        new Pair<View, String>(view.findViewById(R.id.image),
                                DetailActivity.VIEW_NAME_HEADER_IMAGE),
                        new Pair<View, String>(view.findViewById(R.id.description),
                                DetailActivity.VIEW_NAME_HEADER_TITLE));

                System.out.println("click item:" + item.getItemName());
                // Now we can start the Activity, providing the activity options as a bundle
                ActivityCompat.startActivity(Scroll_DB.this, intent, activityOptions.toBundle());
                //startActivity(intent);
                // END_INCLUDE(start_activity)
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                System.out.println("scrollllll");
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_List.size() - 1) {
                    if (goodTask == null) {
                        goodTask = new GoodTask();
                        goodTask.execute(10);
                        System.out.println("haha");
                        goodTask = null;
                    }
                }
            }
        });
    }
    private class GoodTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... input) {
            OkHttpClient client = new OkHttpClient();
            int datalength = data_List.size();
            System.out.println(datalength);
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("data_length", String.valueOf(datalength))
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            System.out.println(formBody);
            System.out.println(request);
            try {
                Response response = client.newCall(request).execute();
                System.out.println("test config");
                String jsonData = response.body().string();
                System.out.print(jsonData);
                JSONObject jsonObj = new JSONObject(jsonData);
                System.out.print(jsonObj);
                JSONArray itemStack = jsonObj.getJSONArray("itemStack");
                System.out.println(itemStack);
                for (int i =0; i<itemStack.length(); i++){
                    JSONObject c = itemStack.getJSONObject(i);
                    String itemName = c.getString("itemName");
                    String price = c.getString("price");
                    String contact = c.getString("contact");
//                    String userName = c.getString("userName");
                    JSONObject geo = new JSONObject(c.getString("geo"));
                    System.out.println(geo);
                    String longitude = geo.getString("lon");
                    String latitude = geo.getString("lat");
                    String link = c.getString("link");
                    if (longitude == null || longitude == "" || latitude == null || latitude == "") {
                        longitude = "40.809281";
                        latitude = "-73.959754";
                    }
//                    System.out.println(link);

                    MyData data = new MyData(price, "testuserName", contact, itemName, link, longitude, latitude);
                    System.out.println(data.getUserId());
                    data_List.add(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                System.out.println(e);
            }
            return "string";
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.notifyDataSetChanged();
        }
    }

    /*
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MyData item = (MyData) adapterView.getItemAtPosition(position);

        // Construct an Intent as normal
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_ID, item.getUserId());

        // BEGIN_INCLUDE(start_activity)
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,

                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                new Pair<View, String>(view.findViewById(R.id.imageview_item),
                        DetailActivity.VIEW_NAME_HEADER_IMAGE),
                new Pair<View, String>(view.findViewById(R.id.textview_name),
                        DetailActivity.VIEW_NAME_HEADER_TITLE));
        Log.d("TAG", "click item id:" + item.getUserId());
        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        // END_INCLUDE(start_activity)
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

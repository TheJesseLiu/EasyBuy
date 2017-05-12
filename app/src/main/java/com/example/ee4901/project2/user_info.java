package com.example.ee4901.project2;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
//import android.support.v7.app.AlertController;
import android.speech.RecognizerIntent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class user_info extends AppCompatActivity {
    BadTask badTask;
    private RecyclerView user_recyclerView;
    private GridLayoutManager user_gridLayoutManager;
    private CustomAdapter user_adapter;
    private List <MyData> user_data_List;
    private MaterialSearchView searchView;

    String email;
    String url;
    String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        setTitle(email+"'s items is here");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new MyOnNavigationItemSelectedListener(email, this));
        navigation.getMenu().getItem(2).setChecked(true);

        user_recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        user_data_List = new ArrayList<MyData>();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        if (badTask == null) {
            badTask = new BadTask();
            badTask.execute(10);
            System.out.println("haha");
            badTask = null;
        }

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
                user_data_List = new ArrayList<MyData>();
                user_adapter = new CustomAdapter(user_info.this, user_data_List, new CustomAdapter.OnItemClickListener() {
                    @Override public void onItemClick(MyData item, View view) {
                        System.out.println("enter item click listener");
                        //Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                        System.out.println(item.getItemName());
                        Intent intent = new Intent(user_info.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_PARAM_NAME, item.getItemName());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_PRICE, item.getPrice());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_CONTACT, item.getContact());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_LINK, item.getLink());

                        intent.putExtra(DetailActivity.EXTRA_PARAM_LONGITUDE, item.getLongitude());
                        intent.putExtra(DetailActivity.EXTRA_PARAM_LATITUDE, item.getLatitude());
                        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                user_info.this,

                                // Now we provide a list of Pair items which contain the view we can transitioning
                                // from, and the name of the view it is transitioning to, in the launched activity
                                new Pair<View, String>(view.findViewById(R.id.image),
                                        DetailActivity.VIEW_NAME_HEADER_IMAGE),
                                new Pair<View, String>(view.findViewById(R.id.description),
                                        DetailActivity.VIEW_NAME_HEADER_TITLE));

                        System.out.println("click item:" + item.getItemName());
                        // Now we can start the Activity, providing the activity options as a bundle
                        ActivityCompat.startActivity(user_info.this, intent, activityOptions.toBundle());
                        //startActivity(intent);
                        // END_INCLUDE(start_activity)
                    }
                });
                user_recyclerView.setAdapter(user_adapter);

                if (badTask == null) {
                    ip = getString(R.string.ip_addr);
                    url = "http://" + ip + ":3000/search/title/description/" + query;
                    System.out.println(url);
                    badTask = new BadTask();
                    badTask.execute(10);
                    System.out.println("haha");
                    badTask = null;
                }
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

        user_gridLayoutManager = new GridLayoutManager(this,2);
        user_recyclerView.setLayoutManager(user_gridLayoutManager);
        user_adapter = new CustomAdapter(this,user_data_List, new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MyData item, View view) {
                System.out.println("enter item click listener");
                //Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                System.out.println(item.getItemName());
                Intent intent = new Intent(user_info.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_PARAM_NAME, item.getItemName());
                intent.putExtra(DetailActivity.EXTRA_PARAM_PRICE, item.getPrice());
                intent.putExtra(DetailActivity.EXTRA_PARAM_CONTACT, item.getContact());
                intent.putExtra(DetailActivity.EXTRA_PARAM_LINK, item.getLink());

                intent.putExtra(DetailActivity.EXTRA_PARAM_LONGITUDE, item.getLongitude());
                intent.putExtra(DetailActivity.EXTRA_PARAM_LATITUDE, item.getLatitude());

                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        user_info.this,

                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        new Pair<View, String>(view.findViewById(R.id.image),
                                DetailActivity.VIEW_NAME_HEADER_IMAGE),
                        new Pair<View, String>(view.findViewById(R.id.description),
                                DetailActivity.VIEW_NAME_HEADER_TITLE));

                System.out.println("click item:" + item.getItemName());
                // Now we can start the Activity, providing the activity options as a bundle
                ActivityCompat.startActivity(user_info.this, intent, activityOptions.toBundle());
                //startActivity(intent);
                // END_INCLUDE(start_activity)
            }
        });
        user_recyclerView.setAdapter(user_adapter);
        user_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView user_recyclerView, int dx, int dy) {
                if(user_gridLayoutManager.findLastCompletelyVisibleItemPosition() == user_data_List.size()-1){
                    if (badTask == null) {
                        badTask = new BadTask();
                        badTask.execute(10);
                        System.out.println("haha");
                        badTask = null;
                    }
                }

            }
        });




    }
    private class BadTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... input) {
            String ip = getString(R.string.ip_addr);
            String url = "http://" + ip + ":3000/item/useritem";
            OkHttpClient client = new OkHttpClient();
            int datalength = user_data_List.size();
            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("data_length", String.valueOf(datalength))
                    .addFormDataPart("email", email)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                System.out.println("test config");
                String jsonData = response.body().string();
                JSONObject jsonObj = new JSONObject(jsonData);
//                System.out.print(jsonObj);
                JSONArray itemStack = jsonObj.getJSONArray("itemStack");
                System.out.println(itemStack);
                for (int i =0; i<itemStack.length(); i++){
                    JSONObject c = itemStack.getJSONObject(i);
                    String itemName = c.getString("itemName");
                    String price = c.getString("price");
                    String contact = c.getString("contact");
//                    String userName = c.getString("userName");
                    String link = c.getString("link");
                    JSONObject geo = new JSONObject(c.getString("geo"));
                    System.out.println(geo);
                    String longitude = geo.getString("lon");
                    String latitude = geo.getString("lat");
                    if (longitude == null || longitude == "" || latitude == null || latitude == "") {
                        longitude = "40.809281";
                        latitude = "-73.959754";
                    }
                    System.out.println(link);
                    MyData data = new MyData(price, "testuserName", contact, itemName, link, longitude, latitude);
                    System.out.println(data.getUserId());
                    user_data_List.add(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                System.out.println("json prob");
            }
            return "string";
        }

        @Override
        protected void onPostExecute(String result) {
            user_adapter.notifyDataSetChanged();
        }
    }

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


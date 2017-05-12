package com.example.ee4901.project2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by yuhao on 5/3/17.
 */

public class MyOnNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
    private String email;
    private AppCompatActivity curActivity;
    public MyOnNavigationItemSelectedListener(String e, AppCompatActivity a){
        email = e;
        curActivity = a;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent test;
        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                test = new Intent(curActivity, Scroll_DB.class);
                test.putExtra("email", email);
                curActivity.startActivity(test);
                return true;
            case R.id.navigation_upload:
                test = new Intent(curActivity, MainActivity.class);
                test.putExtra("email", email);
                curActivity.startActivity(test);
                return true;
            case R.id.navigation_search:
                test = new Intent(curActivity, PageActivity.class);
                test.putExtra("email", email);
                curActivity.startActivity(test);
                return true;
            case R.id.navigation_account:
                test = new Intent(curActivity, user_info.class);
                test.putExtra("email", email);
                curActivity.startActivity(test);
                return true;
        }
        return false;
    }
}

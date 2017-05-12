/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.ee4901.project2;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Our secondary Activity which is launched from {@link MainActivity}. Has a simple detail UI
 * which has a large banner image, title and body text.
 */
public class DetailActivity extends Activity {

    // Extra name for the ID parameter
    public static final String EXTRA_PARAM_NAME = "detail:_name";
    public static final String EXTRA_PARAM_DESCRIPTION = "detail:_description";
    public static final String EXTRA_PARAM_PRICE = "detail:_price";
    public static final String EXTRA_PARAM_CONTACT = "detail:_contact";
    public static final String EXTRA_PARAM_LINK = "detail:_link";
    public static final String EXTRA_PARAM_LONGITUDE = "detail:_longitude";
    public static final String EXTRA_PARAM_LATITUDE = "detail:_latitude";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;
    private TextView mHeaderContent;

    private String item_name;
    private String item_price;
    private String item_contact;
    private String item_link;
    private String itemLatitude;
    private String itemLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Retrieve the correct Item instance, using the ID provided in the Intent
        //item = MyData.getItem(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));
        item_name = getIntent().getStringExtra(EXTRA_PARAM_NAME);
        item_price = getIntent().getStringExtra(EXTRA_PARAM_PRICE);
        item_contact = getIntent().getStringExtra(EXTRA_PARAM_CONTACT);
        item_link = getIntent().getStringExtra(EXTRA_PARAM_LINK);

        itemLatitude = getIntent().getStringExtra(EXTRA_PARAM_LATITUDE);

        itemLongitude = getIntent().getStringExtra(EXTRA_PARAM_LONGITUDE);
        mHeaderImageView = (ImageView) findViewById(R.id.imageview_header);
        mHeaderTitle = (TextView) findViewById(R.id.textview_title);
        mHeaderContent = (TextView) findViewById(R.id.textview_content);

        mHeaderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(DetailActivity.this, Scroll_DB.class);
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        DetailActivity.this,
                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        new Pair<View, String>(findViewById(R.id.imageview_header),
                                VIEW_NAME_HEADER_IMAGE),
                        new Pair<View, String>(findViewById(R.id.textview_title),
                                VIEW_NAME_HEADER_TITLE));

                //System.out.println("click item:" + item.getItemName());
                // Now we can start the Activity, providing the activity options as a bundl
                ActivityCompat.startActivity(DetailActivity.this, intent, activityOptions.toBundle());
                //startActivity(intent);
                */
                finish();
            }
        });
        // BEGIN_INCLUDE(detail_set_view_name)
        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);
        // END_INCLUDE(detail_set_view_name)

        loadItem();

    }

    private void loadItem() {
        // Set the title TextView to the item's name and author
        mHeaderTitle.setText("Prodcut Name: "+item_name + "\nPrice: "+item_price+"\nContact: "+item_contact);
        mHeaderContent.setText("Contact: "+item_contact+"\nLocation: "+itemLatitude+", "+itemLongitude);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
            loadThumbnail();
        } else {
            // If all other cases we should just load the full-size image now
            loadFullSizeImage();
        }
    }

    /**
     * Load the item's thumbnail image into our {@link ImageView}.
     */

    private void loadThumbnail() {
        Picasso.with(mHeaderImageView.getContext())
                .load(item_link)
                .noFade()
                .into(mHeaderImageView);
    }

    /**
     * Load the item's full-size image into our {@link ImageView}.
     */

    private void loadFullSizeImage() {
        Picasso.with(mHeaderImageView.getContext())
                .load(item_link)
                .noFade()
                .noPlaceholder()
                .into(mHeaderImageView);
    }

    /**
     * Try and add a {@link Transition.TransitionListener} to the entering shared element
     * {@link Transition}. We do this so that we can load the full-size image after the transition
     * has completed.
     *
     * @return true if we were successful in adding a listener to the enter transition
     */

    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }
}

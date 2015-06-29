/*
Copyright (c) 2014-2015 F-Secure
See LICENSE for details
*/
package cc.softwarefactory.lokki.android.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import cc.softwarefactory.lokki.android.R;

public class FirstTimeActivity extends AppCompatActivity {

    private String TAG = "FirstTimeActivity";
    private TextView textView;
    private Boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        textView = new TextView(this);
        textView.setPadding(15, 15, 15, 15);
        textView.setText(Html.fromHtml(getString(R.string.welcome_text)));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        setContentView(R.layout.first_time_activity_layout);
        ScrollView scroller = (ScrollView) findViewById(R.id.first_time_text_scrollview);
        scroller.addView(textView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Log.e(TAG, "onPrepareOptionsMenu - next: " + next);
        ActionBar actionBar = getSupportActionBar();
        if (!next) {
            getMenuInflater().inflate(R.menu.first_time_welcome, menu);
            actionBar.setTitle(R.string.welcome_title);
        } else {
            getMenuInflater().inflate(R.menu.first_time_terms, menu);
            actionBar.setTitle(R.string.terms_title);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.continue_with_terms) {
            textView.setText(Html.fromHtml(getString(R.string.terms_text)));
            next = true;
            supportInvalidateOptionsMenu();
            return true;

        } else if (id == R.id.i_agree) {
            MainActivity.firstTimeLaunch = false;
            setResult(RESULT_OK);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

package com.sunzhongyang.sjd.first_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by SJD on 9/15/16.
 */
public class first_project extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_layout);
    }
}

package com.starnine.passwd;

import android.inputmethodservice.Keyboard;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

import dalvik.annotation.TestTarget;

/**
 * Created by licheng on 16-7-18.
 */
public class ActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>{
    public ActivityTest(){
        super("com.starnine.passwd",LoginActivity.class);
    }
    LoginActivity activity;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity=getActivity();
    }

    public void testA() throws InterruptedException {


    }
}

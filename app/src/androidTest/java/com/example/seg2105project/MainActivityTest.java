package com.example.seg2105project;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import androidx.annotation.UiThread;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> myActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    private LoginActivity mActivity = null;
    private TextView text;

    @Before
    public void setUp() throws Exception {
        mActivity = myActivityTestRule.getActivity();
    }

    @Test
    @UiThreadTest
    public void checkFirstName() throws Exception{
        assertNotNull(mActivity.findViewById(R.id.textView4));
        text = mActivity.findViewById(R.id.username);
        text.setText("user1");
        String name = text.getText().toString();
        assertNotEquals("user", name);
    }

}

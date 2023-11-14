package com.example.seg2105project;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest2 {

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityTestRule = new
            ActivityScenarioRule<>(LoginActivity.class);
    @Test
    public void emailIsInvalid() {
        onView(withId(R.id.username)).perform(typeText("email@"), closeSoftKeyboard());
        onView(withId(R.id.logInPassword)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.button)).perform(click());
        onView(withText("Email Address")).check(matches(isDisplayed()));
    }
}

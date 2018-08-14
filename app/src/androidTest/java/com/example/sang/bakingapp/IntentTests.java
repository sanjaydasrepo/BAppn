package com.example.sang.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.sang.bakingapp.ui.ItemListActivity;
import com.example.sang.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by bspl-hpl18 on 14/8/18.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntentTests {
    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<MainActivity>(MainActivity.class);

    private IdlingResource mIdlingResource;
//
//    @Before
//    public void registerIdlingResource() {
//        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
//        // To prove that the test fails, omit this call:
//        Espresso.registerIdlingResources(mIdlingResource);
//    }

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void checkIntent_ItemActivity() {
        onView(ViewMatchers.withId(R.id.rv_baking_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(ItemListActivity.class.getName()));

    }
//
//    @After
//    public void unregisterIdlingResource() {
//        if (mIdlingResource != null) {
//            Espresso.unregisterIdlingResources(mIdlingResource);
//        }
//    }


}

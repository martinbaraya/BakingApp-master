package com.example.android.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;

public class PlayerFragmentTest {
    @Rule
    public ActivityTestRule<TestActivity>mActivityTestRule = new ActivityTestRule<TestActivity>(TestActivity.class);

    private TestActivity mActivity=null;

    @Before
    public void setUp() {
        mActivity=mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchFragmentAndBackButton(){
        //testLaunchFragment
        FrameLayout flContainer = mActivity.findViewById(R.id.test_container);
        assertNotNull(flContainer);
        PlayerFragment fragment = new PlayerFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(flContainer.getId(),fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = fragment.getView().findViewById(R.id.player_main);
        assertNotNull(view);
    }


    @After
    public void tearDown() {
        mActivity = null;
    }
}
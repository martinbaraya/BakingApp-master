package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.data.Step;


//This is an activity that contains the PlayerFragment for the phone view.

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        Bundle data = getIntent().getExtras();
        Step step = null;

        if (data.containsKey(RecipeAdapter.RECIPE)) {
            setTitle(data.getString(RecipeAdapter.RECIPE));
        }

        if (data.containsKey(RecipeActivity.STEP_BUNDLE)) {
            Bundle bundle = data.getBundle(RecipeActivity.STEP_BUNDLE);

            if (bundle.containsKey(RecipeActivity.STEP)) {
                step = bundle.getParcelable(RecipeActivity.STEP);
            }
        }

        if (savedInstanceState == null) {
            Fragment playerFragment = new PlayerFragment();

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.exo_player_container, playerFragment)
                    .addToBackStack(null)
                    .commit();

            Bundle bundle = new Bundle();
            if (step != null) {
                bundle.putParcelable(RecipeActivity.STEP, step);
                playerFragment.setArguments(bundle);
            }

        }
    }


    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            getSupportFragmentManager().popBackStackImmediate();
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}

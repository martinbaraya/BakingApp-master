package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements com.example.android.bakingapp.RecyclerViewClickListener {

    public static final String STEP = "Step";
    public static final String STEP_BUNDLE = "StepBundle";
    private com.example.android.bakingapp.StepsFragment stepsFragment;
    private PlayerFragment playerFragment;
    private View playerContainer;
    private ArrayList<Step> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        SharedPreferences sharedPreferences = getSharedPreferences(com.example.android.bakingapp.WidgetUtils.WIDGET_INGREDIENTS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (savedInstanceState == null) {

            stepsFragment = new com.example.android.bakingapp.StepsFragment();
            playerFragment = new PlayerFragment();

            Bundle data = getIntent().getExtras();

            if (data.containsKey(com.example.android.bakingapp.RecipeAdapter.RECIPE)) {
                String recipeName = data.getString(com.example.android.bakingapp.RecipeAdapter.RECIPE);
                setTitle(recipeName);
                editor.putString(com.example.android.bakingapp.WidgetUtils.RECIPE_STRING, recipeName);
            }

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.step_list_container, stepsFragment)
                    .commit();

            playerContainer = findViewById(R.id.exo_player_container);
            if (playerContainer != null) {
                fm.beginTransaction()
                        .add(R.id.exo_player_container, playerFragment)
                        .commit();
            }

            if (data.containsKey(com.example.android.bakingapp.RecipeAdapter.INGREDIENTS) &&
                    (data.containsKey(com.example.android.bakingapp.RecipeAdapter.STEPS))) {

                // Set the ingredients for this recipe in shared preferences.

                ArrayList<Ingredient> ingredients = data.getParcelableArrayList(com.example.android.bakingapp.RecipeAdapter.INGREDIENTS);

                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < ingredients.size(); i++) {
                    builder.append(ingredients.get(i).toString());

                    if (i < ingredients.size() - 1) {
                        builder.append("\n");
                    }
                }

                editor.putString(com.example.android.bakingapp.WidgetUtils.INGREDIENT_STRING, builder.toString());

                stepsFragment.setArguments(data);
                mSteps = data.getParcelableArrayList(com.example.android.bakingapp.RecipeAdapter.STEPS);

                Bundle bundle = new Bundle();
                bundle.putParcelable(STEP, mSteps.get(0));

                playerFragment.setArguments(bundle);
                editor.apply();

                //Update the app widget with new data.
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, com.example.android.bakingapp.RecipeWidgetProvider.class));
                for (int appWidgetId : appWidgetIds) {
                    com.example.android.bakingapp.RecipeWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId);
                }

            }
        } else {
            // mSteps gets lost on rotation so we had to save it and restore it.
            mSteps = savedInstanceState.getParcelableArrayList(com.example.android.bakingapp.RecipeAdapter.STEPS);
        }
    }

    @Override
    public void onClick(View view, int i) {

        //Toast.makeText(this, String.valueOf(i), Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEP, mSteps.get(i));

        // Launch two-pane mode if you have a container for your second pane.
        if (playerContainer != null) {
            PlayerFragment newPlayerFragment = new PlayerFragment();
            newPlayerFragment.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.exo_player_container, newPlayerFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, com.example.android.bakingapp.RecipeDetailActivity.class);
            intent.putExtra(com.example.android.bakingapp.RecipeAdapter.RECIPE, getTitle());
            intent.putExtra(STEP_BUNDLE, bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(com.example.android.bakingapp.RecipeAdapter.STEPS, mSteps);
        super.onSaveInstanceState(outState);
    }
}

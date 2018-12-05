package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.network.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent= new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final RecyclerView rv = findViewById(R.id.main_recycler_view);


        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            rv.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            rv.setLayoutManager(new LinearLayoutManager(this));
        }

        Call<List<Recipe>> call = NetworkUtils.buildRecipeListCall();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.message().contentEquals("OK")) {
                    mRecipes = response.body();
                    rv.setAdapter(new com.example.android.bakingapp.RecipeAdapter(MainActivity.this, mRecipes));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.i(TAG, t.toString());
            }
        });
    }
}

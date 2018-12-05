package com.example.android.bakingapp.network;

import com.example.android.bakingapp.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

interface RecipeService {

  @GET("baking.json")
  Call<List<Recipe>> getRecipes();

}

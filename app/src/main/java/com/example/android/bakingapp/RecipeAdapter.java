package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

  public static final String INGREDIENTS = "Ingredients";
  public static final String RECIPE = "RecipeName";
  public static final String STEPS = "Steps";

  private static final String TAG = "RecipeAdapter";
  private List<Recipe> mRecipes;
  Context mContext;

  public RecipeAdapter(Context context, List<Recipe> recipes) {
    mRecipes = recipes;
    mContext = context;
  }

  @NonNull
  @Override
  public RecipeHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_main, parent, false);

    RecyclerViewClickListener listener = new RecyclerViewClickListener() {
      @Override
      public void onClick(View view, int i) {
        Intent intent = new Intent(parent.getContext(), RecipeActivity.class);
        intent.putExtra(RECIPE, mRecipes.get(i).getName());
        intent.putParcelableArrayListExtra(INGREDIENTS,
            new ArrayList<Ingredient>(mRecipes.get(i).getIngredients()));
        intent.putParcelableArrayListExtra(STEPS,
            new ArrayList<Step>(mRecipes.get(i).getSteps()));

        mContext.startActivity(intent);
      }
    };

    return new RecipeHolder(view, listener);
  }

  @Override
  public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
    Recipe recipe = mRecipes.get(position);
    holder.setRecipe(recipe.getName(), recipe.getServings().toString() + " servings");
  }

  @Override
  public int getItemCount() {
    return mRecipes.size();
  }

  public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RecyclerViewClickListener mRecyclerViewClickListener;
    private TextView mRecipeTextView;
    private TextView mServingTextView;
    private CardView mCardView;

    RecipeHolder(View itemView, RecyclerViewClickListener listener) {
      super(itemView);
      mRecyclerViewClickListener = listener;
      mRecipeTextView = itemView.findViewById(R.id.recipe_text_view);
      mServingTextView = itemView.findViewById(R.id.serving_text_view);
      mCardView = itemView.findViewById(R.id.main_card_view);
      mCardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      mRecyclerViewClickListener.onClick(v, getAdapterPosition());
    }

    void setRecipe(String recipe, String serving) {
      mRecipeTextView.setText(recipe);
      mServingTextView.setText(serving);
    }
  }

}

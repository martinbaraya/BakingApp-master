package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                              int appWidgetId) {

    String recipeText = context.getString(R.string.recipe_body_text);
    String titleText = context.getString(R.string.recipe_title_text);

    SharedPreferences sharedPreferences = context.getSharedPreferences(WidgetUtils.WIDGET_INGREDIENTS,
        Context.MODE_PRIVATE);

    titleText = sharedPreferences.getString(WidgetUtils.RECIPE_STRING, titleText);
    recipeText = sharedPreferences.getString(WidgetUtils.INGREDIENT_STRING, recipeText);


    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
    views.setTextViewText(R.id.widget_recipe_text, recipeText);
    views.setTextViewText(R.id.widget_recipe_title, titleText);

    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setOnClickPendingIntent(R.id.widget_recipe_title, pendingIntent);

    // Instruct the widget manager to update the widget

      appWidgetManager.updateAppWidget(appWidgetId, views);

  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }

  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}


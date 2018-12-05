
package com.example.android.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

   /* Handle your Parcel */

   public Ingredient(Parcel in) {
       ingredient = in.readString();
       measure = in.readString();
       quantity = in.readDouble();
   }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getIngredient());
        dest.writeString(getMeasure());
        dest.writeDouble(getQuantity());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    //Only include units behind the decimal if you need them.
    if (quantity != Math.round(quantity)) {
      builder.append(quantity.toString());
    } else {
      builder.append(Math.round(quantity));
    }

    builder.append(" ");

    //UNIT is usually just used for xxx and does not need to specified.
    if (!measure.contentEquals("UNIT")) {
      builder.append(measure);
    }

    builder.append(" ");
    builder.append(ingredient);
    return builder.toString();
  }
}

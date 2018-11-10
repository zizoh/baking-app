
package com.zizohanto.bakingapp.data.database;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    private float quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String ingredient;

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    /*StringBuilder sb = new StringBuilder();
    sb.append(ingredientsListHeader);

    for (Ingredient ingredient : ingredients) {

      String name = ingredient.ingredient();
      float quantity = ingredient.quantity();
      String measure = ingredient.measure();

      sb.append("\n");
      sb.append(StringUtils.formatIngredient(getContext(), name, quantity, measure));
    }

    TextViewUtils.setTextWithSpan(recipeDetailsIngredients, sb.toString(), ingredientsListHeader,
        new StyleSpan(Typeface.BOLD));*/
}

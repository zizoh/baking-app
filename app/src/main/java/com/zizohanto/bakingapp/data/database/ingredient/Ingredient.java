
package com.zizohanto.bakingapp.data.database.ingredient;

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
}

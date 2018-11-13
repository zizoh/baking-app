package com.zizohanto.bakingapp.data.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zizohanto.bakingapp.data.database.ingredient.Ingredient;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class IngredientListTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Ingredient> stringToIngredientList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ingredientListToString(List<Ingredient> ingredientList) {
        return gson.toJson(ingredientList);
    }
}

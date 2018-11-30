package com.zizohanto.bakingapp.data.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.ingredient.Ingredient;

import java.util.List;
import java.util.Locale;

public class StringUtils {

    private static String formatIngredient(Context context, String name, double quantity, String measure) {

        String line = context.getString(R.string.recipe_details_ingredient_line);

        String quantityStr = String.format(Locale.US, "%s", quantity);
        if (quantity == (long) quantity) {
            quantityStr = String.format(Locale.US, "%d", (long) quantity);
        }

        return String.format(Locale.US, line, name, quantityStr, measure);
    }

    private static SpannableStringBuilder buildSpannableString(String fullText, String styledText, StyleSpan style) {
        SpannableStringBuilder sb = new SpannableStringBuilder(fullText);
        int start = fullText.indexOf(styledText);
        int end = start + styledText.length();
        sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    public static SpannableStringBuilder formatIngredientForTextViewDisplay(Context context,
                                                                            String textViewTitle, List<Ingredient> ingredients) {
        StringBuilder sb = new StringBuilder();
        sb.append(textViewTitle);
        for (Ingredient ingredient : ingredients) {
            String name = ingredient.getIngredient();
            double quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();

            sb.append("\n");
            sb.append(formatIngredient(context, name, quantity, measure));
        }

        return buildSpannableString(sb.toString(), textViewTitle,
                new StyleSpan(Typeface.BOLD));
    }
}

package com.zizohanto.bakingapp.data.network;

import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    //https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipeResponse>> getRecipes();

//    //api.themoviedb.org/3/movie/157336/videos?api_key=1234#
//    @GET("movie/{id}/videos")
//    Call<VideoResponse> getVideos(@Path("id") long id, @Query("api_key") String apiKey);
//
//    //api.themoviedb.org/3/movie/157336/reviews?api_key=1234#
//    @GET("movie/{id}/reviews")
//    Call<ReviewResponse> getReviews(@Path("id") long id, @Query("api_key") String apiKey);

}


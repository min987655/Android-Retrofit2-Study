package com.example.studyretrofit2application;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

///////////////////////////////////////////////////////////////////////////// GET

//    post 모두 불러오기
//    @GET("posts")
//    Call<List<Post>> getPosts();

//    /comments?postId=1  // 찾고 싶은 postId getPosts에 넣기
//    @GET("posts")
//    Call<List<Post>> getPosts(@Query("userId") int userId);

//    /posts?userId=1&sort=id&_oder=DESC  // 쿼리스트림으로 찾기
//    @GET("posts")
//    Call<List<Post>> getPosts(
//            @Query("userId") int userId,
//            @Query("_sort") String sort,
//            @Query("_oder") String _oder
//    );

//  /posts/1/comments  // 1번 글에 달린 comment 모두 가져오기
    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    //  /posts/1/comments  // 1번 글에 달린 comment 모두 가져오기
    @GET()
    Call<List<Comment>> getComments(@Url String url);

//  /posts?userId=1&sort=id&_oder=DESC  // 쿼리스트림, userId 여러개로 찾기
    @GET("posts")
    Call<List<Post>> getPosts(
//            @Query("userId") Integer userId,
//            @Query("userId") Integer userId2,
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_oder") String _oder
    );

//  Key=value 형태로 찾기
    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String , String > parms);

///////////////////////////////////////////////////////////////////////////// POST

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    // 위와 같음. key=value 형태
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    // 위와 같음. key=value 형태
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

            Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

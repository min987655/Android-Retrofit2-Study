package com.example.studyretrofit2application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parms);

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

///////////////////////////////////////////////////////////////////////////// UPDATE

    // null값 그대로 update (ex) 기존.title : 제목 ==> put.title : null ==> 출력값.title : null
//    @PUT("posts/{id}")
//    Call<Post> putPost(@Path("id") int id, @Body Post post);

    // null값 변경되지 않음 기존값으로 대체 (ex) 기존.title : 제목 ==> patch.title : null ==> 출력값.title : 제목
//    @PATCH("posts/{id}")
//    Call<Post> patchPost(@Path("id") int id, @Body Post post);

///////////////////////////////////////////////////////////////////////////// DELETE

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);

///////////////////////////////////////////////////////////////////////////// header

    @Headers({"Static-Header1: 123 ","Static-Header2: 456"})
    @PUT("posts/{id}")
    Call<Post> putPost( @Header("Dynamic-Header") String header,
                        @Path("id") int id,
                        @Body Post post);

    @PATCH("posts/{id}")
    Call<Post> patchPost( @HeaderMap Map<String, String> headers,
                          @Path("id") int id,
                          @Body Post post);


}

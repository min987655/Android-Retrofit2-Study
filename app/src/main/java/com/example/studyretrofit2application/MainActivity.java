package com.example.studyretrofit2application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.PATCH;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";

    private Button btnTest;
    private TextView tvTest;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initNetwork();
        initListener();
    }

    private void init(){
        btnTest = findViewById(R.id.btn_test);
        tvTest = findViewById(R.id.tv_test);
    }

    private void initNetwork() {

        // json 형태로 변환 (patch시에도 null 값 덮어 씌움, gson 변환 안할 시 null은 아예   안넘어 감.)
        Gson gson = new GsonBuilder().serializeNulls().create();

        // OkHttp
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() { // 모든 request시 header 보내기.
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request newRequest = originalRequest.newBuilder()
                                .addHeader("Interceptor-Header","jkl")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        // Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient) // client를 OkHttp로 함.
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }

    private void initListener(){
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPosts();
//                getComments();
//                createPost();
//                updatePost();
//                deletePost();
            }
        });
    }

    private void getPosts(){

        Map<String, String> parms = new HashMap<>();
        parms.put("userId", "1");
        parms.put("_sort", "id");
        parms.put("_order", "desc");

//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(1, 2,"id", "desc");
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,5,6},"id", "desc");
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parms);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()){
                    Log.d(TAG, "onResponse: " + response.code());
                    tvTest.setText(response.code());
                    return;
                }
                List<Post> postList = response.body();

                for (Post post : postList) {
                    String content = "";
                    content += "id : " + post.getId() + "\n";
                    content += "userId : " + post.getUserId() + "\n";
                    content += "title : " + post.getTitle() + "\n\n";

                    tvTest.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tvTest.setText(t.getMessage());
            }
        });
    }

    private void getComments(){
//        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(3);
        Call<List<Comment>> call = jsonPlaceHolderApi.getComments("posts/3/comments");
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    tvTest.setText(response.code());
                    return;
                }
                List<Comment> commentList = response.body();

                for (Comment comment : commentList) {
                    String content = "";
                    content += "id : " + comment.getId() + "\n";
                    content += "postId : " + comment.getPostId() + "\n";
                    content += "name : " + comment.getName() + "\n";
                    content += "email : " + comment.getEmail() + "\n";
                    content += "text : " + comment.getText() + "\n\n";

                    tvTest.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                tvTest.setText(t.getMessage());
            }
        });
    }

    private void createPost(){
        Post post = new Post(23, "testTitle", "testText");

        Map<String, String> fields = new HashMap<>();
        fields.put("userId","23");
        fields.put("title","testTitle");

//        Call<Post> call = jsonPlaceHolderApi.createPost(post);
        Call<Post> call = jsonPlaceHolderApi.createPost(fields);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    tvTest.setText("code" + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "code : " + response.code() + "\n";
                content += "id : " + postResponse.getId() + "\n";
                content += "userId : " + postResponse.getUserId() + "\n";
                content += "title : " + postResponse.getTitle() + "\n";
                content += "text : " + postResponse.getText() + "\n\n";

                tvTest.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                tvTest.setText(t.getMessage() );
            }
        });
    }

    private void updatePost() {
        Post post = new Post(12, null, "updateTest");

        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1","def");
        headers.put("Map-Header2","ghi");

//        Call<Post> call = jsonPlaceHolderApi.putPost("abc",1, post);
        Call<Post> call = jsonPlaceHolderApi.patchPost(headers,1, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    tvTest.setText("code" + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "code : " + response.code() + "\n";
                content += "id : " + postResponse.getId() + "\n";
                content += "userId : " + postResponse.getUserId() + "\n";
                content += "title : " + postResponse.getTitle() + "\n";
                content += "text : " + postResponse.getText() + "\n\n";

                tvTest.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                tvTest.setText(t.getMessage() );
            }
        });
    }

    private void deletePost() {

        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                tvTest.setText("Code : " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                tvTest.setText(t.getMessage());
            }
        });
    }

}
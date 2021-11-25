package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    void fetchData(){
        String url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=231d1dcbe51f4d9ebdac748337294104";
        ArrayList<News> newsList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, response -> {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = response.getJSONArray("articles");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject newsJsonObject = new JSONObject();
                        try {
                            newsJsonObject = jsonArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        News news = new News();
                        try {
                            System.out.println(newsJsonObject.getString("author"));
                            news.author = newsJsonObject.getString("author");
                            news.description = newsJsonObject.getString("description");
                            news.title = newsJsonObject.getString("title");
                            news.url = newsJsonObject.getString("url");
                            news.urlToImage = newsJsonObject.getString("urlToImage");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        newsList.add(news);
                    }
                    recyclerView.setAdapter(new NewsAdapter(newsList));
                },
                    error -> {
                    // TODO: Handle error
                    }
            ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
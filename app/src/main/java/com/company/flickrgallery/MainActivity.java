package com.company.flickrgallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.company.flickrgallery.adapters.PhotoAdapter;
import com.company.flickrgallery.models.Photo;
import com.company.flickrgallery.utils.MyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public List<Photo> photos;
    private Context context;
    public PhotoAdapter adapter;
    private SharedPreferences preferences;
    String [] numbers;
    String values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){

        context = MainActivity.this;
        preferences = getSharedPreferences("XU",Context.MODE_PRIVATE);
        values = preferences.getString("XU","100");
        photos = new ArrayList<>();
        recyclerView = findViewById(R.id.photo_grid);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));

        setUpRecyclerView();
    }

    public List<Photo> stuff(List<Photo> photoz){
        return photoz;
    }

    private void setUpRecyclerView(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(MyUtils.flickrURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("photos");
                    JSONArray array = object.getJSONArray("photo");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        Photo photo = new Photo();
                        photo.setFarm(String.valueOf(object1.get("farm")));
                        photo.setId(object1.getString("id"));
                        photo.setPhoto_title(object1.getString("title"));
                        photo.setSecret(object1.getString("secret"));
                        photo.setServer(object1.getString("server"));
                        photo.setImage_url();
                        photos.add(photo);
                    }


                    adapter = new PhotoAdapter(context,stuff(photos));
                    new MyUtils().setPhotoList(photos);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        numbers = values.split(",");
        for (int i = 0; i < numbers.length; i++) {
            int pos = Integer.parseInt(numbers[i]);
            if(pos>photos.size()){
                break;
            }
            photos.remove(pos);
            adapter.notifyItemRemoved(pos);
        }


    }
}

package com.example.do_an_android.Activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.do_an_android.Adapter.CategoryAdapter;
import com.example.do_an_android.Model.CategoryModel;
import com.example.do_an_android.Model.GridSpacingItemDecoration;
import com.example.do_an_android.Model.Server;
import com.example.do_an_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCategoryActivity extends AppCompatActivity {
    RecyclerView AllCategoryRecycler;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryModel> allCategoryModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        setControl();
        setCategoryRecycler();
        loadDataAllCategory();
    }

    private void loadDataAllCategory() {
        RequestQueue queue = Volley.newRequestQueue(this);
        @SuppressLint("NotifyDataSetChanged") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlTypeProduct, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    allCategoryModelList.add(new CategoryModel(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getString("image")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            categoryAdapter.notifyDataSetChanged();
        }, error -> {

        });
        queue.add(jsonArrayRequest);
    }

    private void setControl() {
        AllCategoryRecycler = findViewById(R.id.all_category);
    }

    private void setCategoryRecycler() {
        allCategoryModelList=new ArrayList<>();
        AllCategoryRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        AllCategoryRecycler.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(16), true));
        AllCategoryRecycler.setItemAnimator(new DefaultItemAnimator());
        categoryAdapter = new CategoryAdapter(this,R.layout.item_category,allCategoryModelList);
        AllCategoryRecycler.setAdapter(categoryAdapter);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
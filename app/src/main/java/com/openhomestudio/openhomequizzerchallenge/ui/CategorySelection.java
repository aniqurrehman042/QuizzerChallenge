package com.openhomestudio.openhomequizzerchallenge.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.openhomestudio.openhomequizzerchallenge.R;
import com.openhomestudio.openhomequizzerchallenge.adapters.CategoryAdapter;

import java.util.ArrayList;

public class CategorySelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        RecyclerView rvCategories = findViewById(R.id.rv_categories);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("General Knowledge");
        categories.add("Books");
        categories.add("Movies");
        categories.add("Music");
        categories.add("Musicals And Theatres");
        categories.add("Television");
        categories.add("Video Games");
        categories.add("Board Games");
        categories.add("Science And Nature");
        categories.add("Computers");
        categories.add("Maths");
        categories.add("Mythology");
        categories.add("Sports");
        categories.add("Geology");
        categories.add("History");
        categories.add("Politics");
        categories.add("Arts");
        categories.add("Celebrities");
        categories.add("Animals");
        categories.add("Vehicles");
        categories.add("Comics");
        categories.add("Gadgets");

        CategoryAdapter adapter = new CategoryAdapter(categories, this);
        rvCategories.setAdapter(adapter);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));

    }
}

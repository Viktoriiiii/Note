package com.example.note.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.note.Models.Category;
import com.example.note.Models.Note;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {

    Context context;
    List <Category> listCategories;

    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<Category> categories) {
        super(context, resource, categories);
        this.context = context;
        this.listCategories = categories;
    }

    public Category getItem(int position) {
        return listCategories.get(position);
    }

    public int getCount(){
        return listCategories.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(listCategories.get(position).getCategoryName());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(listCategories.get(position).getCategoryName());
        view.setHeight(60);
        return view;
    }
}

package com.learn.mobile.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.mobile.R;

import java.util.ArrayList;
import java.util.List;

public class Sample1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final List<String> data = new ArrayList<>();
        int i = 0;
        for (int n = 1000; i < n; i++) {
            data.add(String.format("Line %d", i));
        }

        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.user_item_layout, parent, false);

                SimpleViewHolder itemBaseViewHolder = new SimpleViewHolder(itemView);
                return itemBaseViewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String str = data.get(position);
                SimpleViewHolder simpleViewHolder = (SimpleViewHolder) holder;
                simpleViewHolder.setTitle(str);
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        };
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public SimpleViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_title);
        }

        public void setTitle(String title) {
            textView.setText(title);
        }
    }

}

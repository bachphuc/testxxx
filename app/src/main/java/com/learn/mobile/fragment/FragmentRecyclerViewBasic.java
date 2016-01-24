package com.learn.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learn.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecyclerViewBasic extends Fragment {
    public static final String TAG = FragmentRecyclerViewBasic.class.getSimpleName();

    View rootView;
    RecyclerView recyclerView;

    public FragmentRecyclerViewBasic() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recycler_basic, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.base_recycler_view);
        BasicRecyclerViewAdapter adapter = new BasicRecyclerViewAdapter();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            list.add("Item " + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return rootView;
    }


    class BasicRecyclerViewAdapter extends RecyclerView.Adapter {
        List<String> data;

        public void setData(List<String> list) {
            data = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.fragment_recycler_view_item, parent, false);

            Log.i(TAG, "onCreateViewHolder: " + viewType);
            BasicViewHolder itemBaseViewHolder = new BasicViewHolder(itemView);
            return itemBaseViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String title = data.get(position);
            BasicViewHolder basicViewHolder = (BasicViewHolder) holder;
            basicViewHolder.textView.setText(title);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    class BasicViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public BasicViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}

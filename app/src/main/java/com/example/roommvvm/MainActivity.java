package com.example.roommvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roommvvm.database.UrlEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private UrlAdapter adapter;
    private UrlViewModel urlViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("UWC", "Activity created");
        //RecyclerView init to display the data
        RecyclerView rv = findViewById(R.id.item_list);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new UrlAdapter();
        rv.setAdapter(adapter);

        //Retrieving data from ViewModel and passing to RecyclerView
//        UrlViewModel urlViewModel = new UrlViewModel(getApplication());
        urlViewModel = new ViewModelProvider(this).get(UrlViewModel.class);

        urlViewModel.getUrls().observe(this, urls ->  {
            Log.i("UWC", "Urls live data changed");
            adapter.setUrls(urls);
        });

        TextView searchBtn = findViewById(R.id.search_btn);
        EditText searchText = findViewById(R.id.search_bar);

        searchBtn.setOnClickListener(v -> {
            urlViewModel.setSearchFilter(searchText.getText().toString());
        });
    }
    private class UrlAdapter extends RecyclerView.Adapter<UrlAdapter.UrlCardHolder> {
        List<UrlEntity> urls;

        public void setUrls(List<UrlEntity> urls) {
            this.urls = new ArrayList<>(urls);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UrlAdapter.UrlCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            return new UrlAdapter.UrlCardHolder(inflater.inflate(R.layout.view_url_item, parent, false));
        }
        @Override
        public void onBindViewHolder(@NonNull UrlAdapter.UrlCardHolder holder, int position) {
            holder.id.setText(String.valueOf(urls.get(position).id));
            holder.url.setText(urls.get(position).url);
            holder.date.setText(String.valueOf(urls.get(position).date));
        }

        @Override
        public int getItemCount() {
            return urls != null ? urls.size() : 0;
        }
        class UrlCardHolder extends RecyclerView.ViewHolder {
            TextView id;
            TextView url;
            TextView date;
            ImageView delete;

            public UrlCardHolder(@NonNull View itemView) {
                super(itemView);
                id = itemView.findViewById(R.id.url_id);
                url = itemView.findViewById(R.id.url_name);
                date = itemView.findViewById(R.id.url_date);
                delete = itemView.findViewById(R.id.url_delete);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        urlViewModel.deleteUrl(urls.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
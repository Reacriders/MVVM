package com.example.roommvvm;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.roommvvm.database.AppDatabase;
import com.example.roommvvm.database.DatabaseClient;
import com.example.roommvvm.database.UrlEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UrlViewModel extends AndroidViewModel {
    private MutableLiveData<List<UrlEntity>> urls;
    private AppDatabase appDatabase;
    private String urlFilter;

    public UrlViewModel(@NonNull Application application) {
        super(application);
        Log.i("UWC", "ViewModel created");
        urls = new MutableLiveData<>();
        appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();

        AsyncTask.execute(() -> {
            Log.i("UWC", "ViewModel thread started DB reading");
            refreshUrlList();
        });
    }

    public LiveData<List<UrlEntity>> getUrls() {
        return urls;
    }


    public void addUrl(String url, Integer date) {
        Log.i("UWC", "Adding new url: " + url);
        //DB access must be done from background thread
        AsyncTask.execute(() -> {
            appDatabase.urlDao().insertUrl(new UrlEntity(url, date));
            refreshUrlList();
        });
    }

    public void deleteUrl(UrlEntity url){
        AsyncTask.execute(() -> {
            appDatabase.urlDao().deleteUrl(url);
            refreshUrlList();
        });
    }

    public void setSearchFilter(String f) {
        urlFilter = f.toLowerCase();
        AsyncTask.execute(() -> {
            refreshUrlList();
        });
    }
    private void refreshUrlList() {
        List<UrlEntity> all = appDatabase.urlDao().getAll();
        if(urlFilter != null && !urlFilter.isEmpty()) {
            List<UrlEntity> filteredUrls = all.stream().filter(url -> url.getName().toLowerCase().contains(urlFilter)).collect(Collectors.toList());
            urls.postValue(filteredUrls);
        } else {
            urls.postValue(all);
        }
    }
}
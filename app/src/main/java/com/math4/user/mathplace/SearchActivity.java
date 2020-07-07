package com.math4.user.mathplace;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    ListView list;
    AdapterClass adapter;
    SearchView editsearch;
    ArrayList<String> searchQueries;
    ArrayList<SearchQuery> arraylist = new ArrayList<SearchQuery>();

    public class SearchQuery {

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        private String query;
        public SearchQuery(String query)
        {
            this.query = query;
        }
    }

    static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        searchQueries = Distrib.allNameTheme;
        progressBar=findViewById(R.id.textViewLoadSearch);
        list = (ListView) findViewById(R.id.list_view);
        for (String searchQuery:searchQueries) {
            SearchQuery searchQuery1 = new SearchQuery(searchQuery);
            // Binds all strings into an array
            arraylist.add(searchQuery1);
        }
        adapter = new AdapterClass(this, arraylist);
        list.setAdapter(adapter);
        editsearch = (SearchView) findViewById(R.id.search_box);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adapter.filter(text);
                return false;
            }
        });
        editsearch.setIconifiedByDefault(false);
//        ActionBar actionBar =getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                showInterstitial();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


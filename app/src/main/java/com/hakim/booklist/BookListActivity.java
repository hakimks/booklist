package com.hakim.booklist;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = (ProgressBar) findViewById(R.id.bp_loading);
        Intent intent = getIntent();
        String query = intent.getStringExtra("Query");

        rvBooks = (RecyclerView) findViewById(R.id.rv_books);
        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);

        URL bookUrl;

        try{
            if (query ==null || query.isEmpty()){
                bookUrl = ApiUtil.buildUrl("programming");
            } else {
                bookUrl = new URL(query);
            }

            new BooksQueryTask().execute(bookUrl);

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        final android.widget.SearchView searchView = (android.widget.SearchView) searchItem.getActionView();
//        final SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_advanced_search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try{
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);

        } catch (Exception e){
            Log.d("Error", e.getMessage());

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String >{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try{
                result = ApiUtil.getJson(searchUrl);
            } catch (IOException e){
                Log.d("Error", e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            TextView errorMsg = (TextView) findViewById(R.id.tv_error);

            mLoadingProgress.setVisibility(View.INVISIBLE);
            if (result == null){
                rvBooks.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.VISIBLE);


            } else {
                rvBooks.setVisibility(View.VISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);

                ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
                String resultString = "";

                BooksAdaptor adaptor = new BooksAdaptor(books);
                rvBooks.setAdapter(adaptor);

            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}

package com.hakim.booklist;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        mLoadingProgress = (ProgressBar) findViewById(R.id.bp_loading);

        try{
            URL url = ApiUtil.buildUrl("cooking");
            new BooksQueryTask().execute(url);

        } catch (Exception e){
            Log.d("Error", e.getMessage());
        }
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
        protected void onPostExecute(String jsonResults) {
            TextView tvResult = (TextView) findViewById(R.id.tvResponse);
            TextView errorMsg = (TextView) findViewById(R.id.tv_error);

            mLoadingProgress.setVisibility(View.INVISIBLE);
            if (jsonResults == null){
                tvResult.setVisibility(View.INVISIBLE);
                errorMsg.setVisibility(View.VISIBLE);

            } else {
                tvResult.setVisibility(View.VISIBLE);
                errorMsg.setVisibility(View.INVISIBLE);
            }

            tvResult.setText(jsonResults);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}

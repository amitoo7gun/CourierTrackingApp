package com.example.amit.porterapp;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.amit.porterapp.data.ParcelsDBContract;

/**
 * Created by amit on 7/9/2015.
 */
public class SearchActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private final static String SCREEN_LABEL = "Search";
    private SimpleCursorAdapter adapter;


    SearchView mSearchView = null;
    String mQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);




        String query = getIntent().getStringExtra(SearchManager.QUERY);
        query = query == null ? "" : query;
        mQuery = query;


        if (mSearchView != null) {
            mSearchView.setQuery(query, false);
        }

        overridePendingTransition(0, 0);

        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent();
    }
    private void handleIntent() {


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        if (searchItem != null) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            final SearchView view = (SearchView) searchItem.getActionView();
            mSearchView = view;
            if (view == null) {

            } else {
                view.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                view.setIconified(false);
                view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        view.clearFocus();

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        //String query = getIntent().getStringExtra(SearchManager.QUERY);
                          fillData(s);
//                        TextView tv=(TextView)findViewById(R.id.search_query);
//                        tv.setText(s);
                        return true;
                    }
                });
                view.setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        finish();
                        return false;
                    }
                });
            }

            if (!TextUtils.isEmpty(mQuery)) {
                view.setQuery(mQuery, false);
            }
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ParcelsDBContract.ParcelsEntry._ID,
                ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_NAME,
                ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_WEIGHT };
        CursorLoader cursorLoader = new CursorLoader(this,
                ParcelsDBContract.ParcelsEntry.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    private void fillData(String q){
        ListView dic_word_text=(ListView)findViewById(R.id.productsearch_listView2);
        String[] COLUMNS_TO_BE_BOUND  = new String[] {
                ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_NAME,
                ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_WEIGHT
        };

        int[] LAYOUT_ITEMS_TO_FILL = new int[] {
                android.R.id.text1,
                android.R.id.text2
        };
        //String query = getIntent().getStringExtra(SearchManager.QUERY);
        Uri search_db_query = ParcelsDBContract.ParcelsEntry.CONTENT_URI.buildUpon().build();
        Toast toast = Toast.makeText(this, search_db_query.toString(), Toast.LENGTH_SHORT);
        toast.show();
        String[] whereArgs = new String[] {
                q
        };
        getLoaderManager().initLoader(0, null, this);
        Cursor cursor=getContentResolver().query(search_db_query, null, "name LIKE '%"+q+"%'", null, null);
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                cursor,
                COLUMNS_TO_BE_BOUND,
                LAYOUT_ITEMS_TO_FILL,
                0);
        dic_word_text.setAdapter(adapter);
    }
}

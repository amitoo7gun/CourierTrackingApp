package com.example.amit.porterapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.porterapp.data.ParcelsDBContract;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
/**
 * Created by amit on 7/27/2015.
 */
public class DetailFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    static final String DETAIL_URI = "URI";

    private static final String PORTER_SHARE_HASHTAG = "#porterApp";

    private ShareActionProvider mShareActionProvider;
    private String mPorterShare;
    private Uri mUri;

    public static double loc_lat,loc_lng;


    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
          ParcelsDBContract.ParcelsEntry._ID,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_NAME,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_IMAGE_URL,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_DATE,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_TYPE,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_WEIGHT,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_PHONE,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_PRICE,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_QUANTITY,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_COLOR,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_LINK,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_LOC_LAT,
          ParcelsDBContract.ParcelsEntry.COLUMN_PARCEL_LOC_LONG
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_PRODUCTS_ID = 0;
    public static final int COLUMN_PARCEL_NAME = 1;
    public static final int COLUMN_PARCEL_IMAGE_URL = 2;
    public static final int COLUMN_PARCEL_DATE = 3;
    public static final int COLUMN_PARCEL_TYPE = 4;
    public static final int COLUMN_PARCEL_WEIGHT = 5;
    public static final int COLUMN_PARCEL_PHONE = 6;
    public static final int COLUMN_PARCEL_PRICE = 7;
    public static final int COLUMN_PARCEL_QUANTITY = 8;
    public static final int COLUMN_PARCEL_COLOR = 9;
    public static final int COLUMN_PARCEL_LINK = 10;
    public static final int COLUMN_PARCEL_LOC_LAT = 11;
    public static final int COLUMN_PARCEL_LOC_LONG = 12;




    private TextView mNameView;
    private ImageView mImageUrlView;
    private TextView mDateView;
    private TextView mTypeView;
    private TextView mWeightView;
    private TextView mPhoneView;
    private TextView mPriceView;
    private TextView mQuantityView;
    private TextView mColorView;
    private TextView mLinkView;
    private TextView mLoc_LatView;
    private TextView mLoc_LongView;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void loadmap();
    }
    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mImageUrlView = (ImageView) rootView.findViewById(R.id.ParcelimageView);
        mDateView = (TextView) rootView.findViewById(R.id.detail_date_textview);
        mNameView = (TextView) rootView.findViewById(R.id.detail_parcelname_textview);
        mTypeView = (TextView) rootView.findViewById(R.id.detail_parcelcategory_textview);
        mWeightView = (TextView) rootView.findViewById(R.id.detail_parcelweight_textview);
        mPhoneView = (TextView) rootView.findViewById(R.id.detail_parcelphone_textview);
        mPriceView = (TextView) rootView.findViewById(R.id.detail_parcelprice_textview);
        mQuantityView = (TextView) rootView.findViewById(R.id.detail_parcelquantity_textview);
        mColorView = (TextView) rootView.findViewById(R.id.detail_parcelColor_textview);
        mLoc_LatView = (TextView) rootView.findViewById(R.id.detail_lat_textview);
        mLoc_LongView = (TextView) rootView.findViewById(R.id.detail_long_textview);

        ((Callback) getActivity())
                .loadmap();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mPorterShare != null) {
            mShareActionProvider.setShareIntent(createShareParcelIntent());
        }
    }

    private Intent createShareParcelIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mPorterShare + PORTER_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
//
//    void onLocationChanged( String newLocation ) {
//        // replace the uri, since the location has changed
//        Uri uri = mUri;
//        if (null != uri) {
//            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
//            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
//            mUri = updatedUri;
//            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
//        }
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

      // Read description from cursor and update view
//            new DownloadImageTask(mImageUrlView)
//                    .execute(data.getString(COLUMN_PARCEL_IMAGE_URL));

            Picasso.with(getContext()).load(data.getString(COLUMN_PARCEL_IMAGE_URL)).into(mImageUrlView);


            mDateView.setText("Delivery Date: " + Utility.formatDate(Long.parseLong(data.getString(COLUMN_PARCEL_DATE))));
            mNameView.setText(data.getString(COLUMN_PARCEL_NAME));
            mTypeView.setText("Category: " + data.getString(COLUMN_PARCEL_TYPE));
            mPriceView.setText("Price: " + data.getString(COLUMN_PARCEL_PRICE));
            mWeightView.setText("Weight: " + data.getString(COLUMN_PARCEL_WEIGHT));
            mQuantityView.setText("Quanity: " + data.getString(COLUMN_PARCEL_QUANTITY));
            mPhoneView.setText("Phone: " + data.getString(COLUMN_PARCEL_PHONE));
            mColorView.setText("Color: " + data.getString(COLUMN_PARCEL_COLOR));
            mLoc_LatView.setText("lat: " + data.getString(COLUMN_PARCEL_LOC_LAT));
            mLoc_LongView.setText("lng: " + data.getString(COLUMN_PARCEL_LOC_LONG));

            loc_lat=Double.parseDouble(data.getString(COLUMN_PARCEL_LOC_LAT));
            loc_lng=Double.parseDouble(data.getString(COLUMN_PARCEL_LOC_LONG));

            mPorterShare= String.format("%s - of: %s -pricing %s/Contact: %s", data.getString(COLUMN_PARCEL_NAME), data.getString(COLUMN_PARCEL_TYPE), data.getString(COLUMN_PARCEL_PRICE), data.getString(COLUMN_PARCEL_PHONE));
            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareParcelIntent());
            }
        }

        ((Callback) getActivity())
                .loadmap();
    }

    //method to download image asynchronysly
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
               Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }


}

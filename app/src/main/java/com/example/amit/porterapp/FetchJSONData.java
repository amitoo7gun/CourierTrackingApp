//package com.example.amit.porterapp;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.example.amit.porterapp.data.ParcelsDBContract;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Vector;
//
///**
// * Created by amit on 7/16/2015.
// */
//public class FetchJSONData extends AsyncTask<Void,Void,Void> {
//    public final String LOG_TAG = FetchJSONData.class.getSimpleName();
//    private final Context mContext;
//
//    public FetchJSONData(Context context) {
//        mContext = context;
//
//    }
//    @Override
//    protected Void doInBackground(Void... params) {
//
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//            // Will contain the raw JSON response as a string.
//            String JsonStr = null;
//
//            try {
//                // Construct the URL for the OpenWeatherMap query
//                // Possible parameters are avaiable at OWM's forecast API page, at
//                // http://openweathermap.org/API#forecast
//                final String BASE_URL =
//                        "http://xseed.0x10.info/api/game_data?type=json";
//
//                Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();
//
//                URL url = new URL(builtUri.toString());
//
//                // Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                //InputStream inputStream = getClass().getResourceAsStream("game_data.json");
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//
//                }
//                JsonStr = buffer.toString();
//                getDataFromJson(JsonStr);
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Error ", e);
//                // If the code didn't successfully get the weather data, there's no point in attempting
//                // to parse it.
//            } catch (JSONException e) {
//                Log.e(LOG_TAG, e.getMessage(), e);
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//            }
//
//        return null;
//    }
//
//
//
//    private void getDataFromJson(String JsonStr)
//            throws JSONException {
//
//        //Log.v(LOG_TAG,JsonStr);
//        // Now we have a String representing the complete forecast in JSON Format.
//        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
//        // into an Object hierarchy for us.
//
//        // These are the names of the JSON objects that need to be extracted.
//
//        // Location information
//        final String GAME_NAME = "name";
//        final String GAME_IMAGE_URL= "image";
//        final  String GAME_URL= "url";
//        final String GAME_PRICE="price";
//        final String GAME_RATING="rating";
//        final String GAME_DESCRIPTION="description";
//        final  String GAME_DEMOGRAPHIC="demographic";
//        final String DEMOGRAPHIC_COUNTRY="country";
//        final String DEMOGRAPHIC_PERCENTAGE="percentage";
//
//        try {
//            JSONArray productsJsonArray = new JSONArray(JsonStr);
//            Vector<ContentValues> cVVector = new Vector<ContentValues>(productsJsonArray.length());
//            for(int i=0;i<productsJsonArray.length();i++)
//            {
//                JSONObject produsctsJson = productsJsonArray.getJSONObject(i);
//                String product_name = produsctsJson.getString(GAME_NAME);
//                String product_img_url = produsctsJson.getString(GAME_IMAGE_URL);
//                String product_url = produsctsJson.getString(GAME_URL);
//                String product_price = produsctsJson.getString(GAME_PRICE);
//                String product_rating = produsctsJson.getString(GAME_RATING);
//                String product_description = produsctsJson.getString(GAME_DESCRIPTION);
//
//                JSONArray product_demographic = produsctsJson.getJSONArray(GAME_DEMOGRAPHIC);
//
//                Log.v(LOG_TAG,product_name);
//                ContentValues productsValues = new ContentValues();
//                productsValues.put(ParcelsDBContract.ProductsEntry.COLUMN_NAME, product_name);
//                productsValues.put(ParcelsDBContract.ProductsEntry.COLUMN_IMG_URL, product_img_url);
//                productsValues.put(ParcelsDBContract.ProductsEntry.COLUMN_URL, product_url);
//                productsValues.put(ParcelsDBContract.ProductsEntry.COLUMN_PRICE, product_price);
//                productsValues.put(ParcelsDBContract.ProductsEntry.COLUMN_RATING, product_rating);
//                productsValues.put(ParcelsDBContract.ProductsEntry.COLUMN_DESCRIPTION, product_description);
//                cVVector.add(productsValues);
//            }
//
//            int inserted = 0;
//            // add to database
//            if ( cVVector.size() > 0 ) {
//                ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                cVVector.toArray(cvArray);
//
//                mContext.getContentResolver().bulkInsert(ParcelsDBContract.ProductsEntry.CONTENT_URI, cvArray);
//
//
//            }
//
//            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");
//
//        }
//        catch (JSONException e) {
//            Log.e(LOG_TAG, e.getMessage(), e);
//            e.printStackTrace();
//        }
//    }
//}

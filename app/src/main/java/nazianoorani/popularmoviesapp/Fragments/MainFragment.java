package nazianoorani.popularmoviesapp.Fragments;



import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nazianoorani.popularmoviesapp.R;
import nazianoorani.popularmoviesapp.activities.SettingsActivity;
import nazianoorani.popularmoviesapp.adapters.CardAdapter;
import nazianoorani.popularmoviesapp.database.DatabaseHelper;
import nazianoorani.popularmoviesapp.database.MovieContract;
import nazianoorani.popularmoviesapp.dto.MovieDetailsDto;
import nazianoorani.popularmoviesapp.networkmanager.AppController;
import nazianoorani.popularmoviesapp.util.NetworkUtil;
import nazianoorani.popularmoviesapp.util.ProgressDialogUtil;

/**
 * Created by nazianoorani on 04/02/16.
 **/
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    ArrayList<MovieDetailsDto> moviesArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    CardAdapter recyclerViewAdapter;
    TextView textViewNoItems;
    Toolbar toolbar;
    String URL;
    private static final int RESULT_SETTINGS = 1;
    private static final int LOADER_FAVOURITE_MOVIES_ID = 1001;
    private ProgressBar mProgressBar;

    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_main);
        textViewNoItems = (TextView) view.findViewById(R.id.textView_no_items);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);


            if(savedInstanceState == null) {
                Log.i("info", "SAVED STATE == NULL");
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                evaluateSortOrder();
            }
            else{
                moviesArrayList = savedInstanceState.getParcelableArrayList(getString(R.string.movie_details_dto_array_list));
            }

        setHasOptionsMenu(true);
        initRecyclerView();
        recyclerViewAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        Log.i("info","RESEUME");
        super.onResume();
        evaluateSortOrder();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_setting){

            Intent i = new Intent(getActivity(),SettingsActivity.class);
            startActivityForResult(i,RESULT_SETTINGS);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SETTINGS:
                evaluateSortOrder();
                break;

        }
    }

    private void evaluateSortOrder() {
        Log.i("info","EVALUATE SORT");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPreferences.getString(getString(R.string.pref_list), "popularity");
        textViewNoItems.setVisibility(View.GONE);
        if(sortOrder.equals(getString(R.string.popularity))){
            URL = getString(R.string.url_popular);
            populateJson();
        }else if(sortOrder.equals(getString(R.string.rating))){
            URL = getString(R.string.url_ratings);
            populateJson();
        }else if(sortOrder.equals(getString(R.string.fav_list))){
            Log.i("info", "fav list");
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            getLoaderManager().restartLoader(LOADER_FAVOURITE_MOVIES_ID, null,this);
//            DatabaseHelper db = new DatabaseHelper(getContext());
//            moviesArrayList = db.getFavMovieDtoList();
//
//            if(moviesArrayList.size() <= 0)
//            {
//                textViewNoItems.setText(getString(R.string.empty_list));
//                textViewNoItems.setVisibility(View.VISIBLE);
//            }
//            initRecyclerView();
//            recyclerViewAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.movie_details_dto_array_list), moviesArrayList);
        super.onSaveInstanceState(outState);
    }

    void initRecyclerView() {
        Log.i("info","INIT RECYC");
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewAdapter = new CardAdapter(getActivity(), moviesArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void populateJson() {

        if( !NetworkUtil.isNetworkAvailable(getContext())){
            ProgressDialogUtil.hidePDialog();
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            Toast.makeText(getContext(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
        }
        else {

            // append the api key here
            String apikey = "381c854a1a41b329208dff51e7cb34b5";
            URL = URL + apikey;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject result) {
                    try {
                        if (!moviesArrayList.isEmpty()) {
                            moviesArrayList.clear();
                        }
                        JSONArray jsonArray = result.getJSONArray("results");


                        if (jsonArray.length() <= 0) {
                            textViewNoItems.setText(getString(R.string.empty_list));
                            textViewNoItems.setVisibility(View.VISIBLE);
                            return;
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MovieDetailsDto movieDetailsDto = new MovieDetailsDto();

                            if (jsonObject.has(getString(R.string.poster_path))) {
                                movieDetailsDto.setPosterpath(getString(R.string.image_constant) + getString(R.string.image_size) + jsonObject.getString(getString(R.string.poster_path)));
                            }
                            if (jsonObject.has(getString(R.string.backdrop_path))) {
                                movieDetailsDto.setBackdroppath(getString(R.string.image_constant) + getString(R.string.image_size) + jsonObject.getString(getString(R.string.poster_path)));
                            }
                            if (jsonObject.has(getString(R.string.vote_average))) {
                                movieDetailsDto.setVoteAverage(jsonObject.getString(getString(R.string.vote_average)));
                            }
                            if (jsonObject.has(getString(R.string.title))) {
                                movieDetailsDto.setTitle(jsonObject.getString(getString(R.string.title)));
                            }
                            if (jsonObject.has(getString(R.string.release_date))) {
                                movieDetailsDto.setReleaseDate(jsonObject.getString(getString(R.string.release_date)));
                            }
                            if (jsonObject.has(getString(R.string.plot_synopsis))) {
                                movieDetailsDto.setPlotSynopsis(jsonObject.getString(getString(R.string.plot_synopsis)));
                            }
                            if (jsonObject.has(getString(R.string.id))) {
                                movieDetailsDto.setId(jsonObject.getString(getString(R.string.id)));
                            }
                      mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                            moviesArrayList.add(movieDetailsDto);
                            recyclerViewAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT);
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.FavoriteMovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        moviesArrayList.clear();
         while (cursor.moveToNext()) {
             Log.d("debug Cursor" , "can be created");
             Log.d("TITLEEEEEE" , cursor.getString(cursor.getColumnIndex("title")));
             Log.d("VOTE_AVGGGGGGGG" , cursor.getString(cursor.getColumnIndex("vote_average")));
            MovieDetailsDto movie = new MovieDetailsDto(cursor.getString(
                    cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH)));
            moviesArrayList.add(movie);
        }
        cursor.close();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    /**
     * Interface implemented by MainActivity to launch movie details fragment or activity
     */
    public interface ClickCallback {
        void onItemSelected(MovieDetailsDto movieDetailsDto);
    }


}

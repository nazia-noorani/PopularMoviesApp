package nazianoorani.popularmoviesapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import nazianoorani.popularmoviesapp.R;
import nazianoorani.popularmoviesapp.adapters.TrailersListAdapter;
import nazianoorani.popularmoviesapp.database.DatabaseHelper;
import nazianoorani.popularmoviesapp.dto.MovieDetailsDto;
import nazianoorani.popularmoviesapp.dto.MovieVideosDto;
import nazianoorani.popularmoviesapp.networkmanager.AppController;
import nazianoorani.popularmoviesapp.util.NetworkUtil;

/**
 * Created by nazianoorani on 02/02/16.
 */
public class DetailsFragment extends Fragment {
//    Button buttonReviews;
    MovieDetailsDto movieDetailsDto;
    RecyclerView recyclerViewDetails;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    TrailersListAdapter recyclerAdapter;
    ArrayList<MovieVideosDto> list = new ArrayList<>();
    DatabaseHelper db;
    private ProgressBar mProgressBar;


    public static final String PARCELABLE_MOVIE_DETAILS_DTO = "parcelable";
    private String videos ="/videos?api_key=";
    private ImageView imageViewBackdrop;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail_layout, container, false);
        // created db
        db = new DatabaseHelper(getContext());
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarDetails);

        if (savedInstanceState == null) {
            // checking required in two pane UI
            if(getActivity().getIntent() != null) {
                movieDetailsDto = getActivity().getIntent().getParcelableExtra(PARCELABLE_MOVIE_DETAILS_DTO);

            }
            else{
                // for 2 pane UI
                Bundle bundle = getArguments();
                if(bundle != null){
                    movieDetailsDto = bundle.getParcelable(DetailsFragment.PARCELABLE_MOVIE_DETAILS_DTO);}
            }

            mProgressBar.setVisibility(View.VISIBLE);
            fetchVideos();
        } else {
            list = savedInstanceState.getParcelableArrayList("videosList");
            movieDetailsDto = savedInstanceState.getParcelable("movieDetailsDto");
        }

//        buttonReviews = (Button) view.findViewById(R.id.button_reviews);
        imageViewBackdrop = (ImageView) view.findViewById(R.id.imageBackDrop);
        recyclerViewDetails = (RecyclerView) view.findViewById(R.id.recylerViewDetails);
        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new TrailersListAdapter(getActivity(), list ,movieDetailsDto);
        recyclerViewDetails.setAdapter(recyclerAdapter);
//        buttonReviews.setOnClickListener(this);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        if (movieDetailsDto.getBackdroppath() != null) {
            Picasso.with(getContext()).load(movieDetailsDto.getBackdroppath()).into(imageViewBackdrop);
        }

        collapsingToolbarLayout.setTitle(movieDetailsDto.getTitle());

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("videosList", list);
        outState.putParcelable("movieDetailsDto", movieDetailsDto);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_share:
                MovieVideosDto mdto = list.get(0);
                if (mdto.getKey() != null) {
                    String key = mdto.getKey();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.video_link) + key);
                    startActivity(Intent.createChooser(shareIntent, "Share link using"));}
                break;

            case android.R.id.home :
                getActivity().onBackPressed();
                break;
        }
        return true;
    }




    private void fetchVideos() {

        if (!NetworkUtil.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), getString(R.string.no_network_msg), Toast.LENGTH_LONG);
            mProgressBar.setVisibility(View.GONE);
        } else {
            //  insert api key
            String apiKey = "";
            String URL = getString(R.string.base_url) + movieDetailsDto.getId() + videos + apiKey;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (!list.isEmpty()) {
                            list.clear();
                        }
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            MovieVideosDto movieVideosDto = new MovieVideosDto();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.has(getString(R.string.key))) {
                                movieVideosDto.setKey(jsonObject.getString(getString(R.string.key)));
                            }
                            if (jsonObject.has(getString(R.string.name))) {
                                movieVideosDto.setName(jsonObject.getString(getString(R.string.name)));
                            }
                            if (jsonObject.has(getString(R.string.type))) {
                                movieVideosDto.setType(jsonObject.getString(getString(R.string.type)));
                            }
                            mProgressBar.setVisibility(View.INVISIBLE);
                            list.add(movieVideosDto);
                            recyclerAdapter.notifyDataSetChanged();

                        }
                        if (jsonArray.length() == 0) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), getString(R.string.no_trailers), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        }
    }

}

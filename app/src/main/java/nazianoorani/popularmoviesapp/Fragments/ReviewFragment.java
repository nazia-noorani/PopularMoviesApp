package nazianoorani.popularmoviesapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import nazianoorani.popularmoviesapp.adapters.CustomAdapter;
import nazianoorani.popularmoviesapp.dto.MovieReviewDto;
import nazianoorani.popularmoviesapp.dto.MovieVideosDto;
import nazianoorani.popularmoviesapp.networkmanager.AppController;
import nazianoorani.popularmoviesapp.util.NetworkUtil;
import nazianoorani.popularmoviesapp.util.ProgressDialogUtil;

/**
 * Created by nazianoorani on 02/02/16.
 */
public class ReviewFragment extends Fragment {
    RecyclerView recyclerViewReviews;
    String movieId;
    private final String reviews = "/reviews?api_key=";
    ArrayList<MovieReviewDto> listMovieReviewDto = new ArrayList<>();
    CustomAdapter customAdapter;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_review);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        Bundle bundle = getArguments();
        if (bundle != null) {
            movieId = bundle.getString("movieId");

        }


        if (!NetworkUtil.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.no_network_msg), Toast.LENGTH_LONG).show();
        } else {
            ProgressDialogUtil.showDialog(getContext(),"Loading");
            requestReviews();
            initRecyclerView(view);
        }
        toolbar.setTitle("Reviews");
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return true;
    }
    private void requestReviews () {
        // insert api key
        String apiKey ="";
        String URL = getString(R.string.base_url)+movieId+reviews+apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MovieReviewDto movieReviewDto = new MovieReviewDto();
                        if (jsonObject.has(getString(R.string.content))) {
                            movieReviewDto.setContent(jsonObject.getString(getString(R.string.content)));
                        }
                        if (jsonObject.has(getString(R.string.author))) {
                            movieReviewDto.setAuthor(jsonObject.getString(getString(R.string.author)));
                        }
                        listMovieReviewDto.add(movieReviewDto);
                        customAdapter.notifyDataSetChanged();
                        ProgressDialogUtil.hidePDialog();
                    }
                    if(jsonArray.length() == 0){
                        ProgressDialogUtil.hidePDialog();
                        Toast.makeText(getContext(),getString(R.string.no_reviews),Toast.LENGTH_LONG).show();
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



    private void initRecyclerView(View view) {
        recyclerViewReviews = (RecyclerView) view.findViewById(R.id.recylerView_reviews);
        customAdapter = new CustomAdapter(getActivity(), listMovieReviewDto);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReviews.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();


    }

}
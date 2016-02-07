package nazianoorani.popularmoviesapp.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import nazianoorani.popularmoviesapp.util.AppUtil;
import nazianoorani.popularmoviesapp.util.NetworkUtil;
import nazianoorani.popularmoviesapp.util.ProgressDialogUtil;

import static android.R.drawable.btn_star;
import static android.R.drawable.btn_star_big_on;

/**
 * Created by nazianoorani on 02/02/16.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener{
    TextView textViewTitle,textViewReleaseDate,textViewPlotSynopsis,textViewVoteAverage;
    ImageView imageViewBackdrop;
    Button buttonReviews,buttonFav;
    MovieDetailsDto movieDetailsDto;
    ListView listView;
    Toolbar toolbar;
    TrailersListAdapter listViewAdapter;
    ArrayList<MovieVideosDto> list = new ArrayList<>();
    private int mflagFav = 0;
    DatabaseHelper db;


    public static final String PARCELABLE_MOVIE_DETAILS_DTO = "parcelable";
    private String videos ="/videos?api_key=";
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_details, container, false);
        // created db
        db = new DatabaseHelper(getContext());

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

            ProgressDialogUtil.showDialog(getContext(), "Fetching tailers");
            fetchVideos();
        } else {
            list = savedInstanceState.getParcelableArrayList("videosList");
            movieDetailsDto = savedInstanceState.getParcelable("movieDetailsDto");
        }
        listView = (ListView) view.findViewById(R.id.listView_details);
        LayoutInflater inflater1 = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        ViewGroup header = (ViewGroup) inflater1.inflate(R.layout.list_header_items, listView, false);
        listView.addHeaderView(header, null, false);
        listViewAdapter = new TrailersListAdapter(getActivity(), list);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieVideosDto dto = list.get(position -1);
                if (dto.getKey() != null) {
                    if (AppUtil.isAppInstalled("com.google.android.youtube", getActivity())) {

                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + dto.getKey()));
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" +  dto.getKey()));
                            startActivity(intent);
                        }
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" +  dto.getKey()));
                            startActivity(intent);

                        } catch (ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(), getString(R.string.video_error), Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }
        });

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_details);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        buttonFav = (Button) view.findViewById(R.id.buttonFav);
        textViewTitle = (TextView) view.findViewById(R.id.textView);
        textViewReleaseDate = (TextView) view.findViewById(R.id.textView_releaseDate);
        textViewVoteAverage = (TextView) view.findViewById(R.id.textView_voteAverage);
        textViewPlotSynopsis = (TextView) view.findViewById(R.id.textView_plotSynopsis);
        imageViewBackdrop = (ImageView) view.findViewById(R.id.imageView_backdrop);
        buttonReviews = (Button) view.findViewById(R.id.button_reviews);
        buttonReviews.setOnClickListener(this);
        buttonFav.setOnClickListener(this);

        // checking wether the selected movie is fav or not
        ArrayList<MovieDetailsDto> favList = db.getFavMovieDtoList();
        for(int i = 0; i < favList.size(); i++){
            MovieDetailsDto dto = favList.get(i);
            if(dto.getId().equals(movieDetailsDto.getId())){
                mflagFav = 1;
                buttonFav.setBackgroundResource(btn_star_big_on);
                break;
            }
        }

        if (movieDetailsDto.getBackdroppath() != null) {
            Picasso.with(getContext()).load(movieDetailsDto.getBackdroppath()).into(imageViewBackdrop);
        }

        textViewTitle.setText(movieDetailsDto.getTitle());
        textViewReleaseDate.setText("Release Date : "+movieDetailsDto.getReleaseDate());
        textViewVoteAverage.setText(movieDetailsDto.getVoteAverage() + "/10");
        textViewPlotSynopsis.setText(movieDetailsDto.getPlotSynopsis());

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
            ProgressDialogUtil.hidePDialog();
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
                            ProgressDialogUtil.hidePDialog();
                            list.add(movieVideosDto);
                            listViewAdapter.notifyDataSetChanged();

                        }
                        if (jsonArray.length() == 0) {
                            ProgressDialogUtil.hidePDialog();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_reviews :
                Bundle bundle = new Bundle();
                bundle.putString("movieId",movieDetailsDto.getId());
                ReviewFragment reviewFragment = new ReviewFragment();
                reviewFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_container, reviewFragment).addToBackStack(null).commit();
                break;
            case R.id.buttonFav :
                toggleFavButton();
                break;

        }
    }

    private void toggleFavButton() {

        if(mflagFav == 0){
            mflagFav = 1;
            buttonFav.setBackgroundResource(btn_star_big_on);
            db.favouriteMovie(movieDetailsDto);
            Toast.makeText(getContext(),getString(R.string.favorite),Toast.LENGTH_LONG).show();

        }
        else{
            mflagFav = 0;
            buttonFav.setBackgroundResource(btn_star);
            db.unFavourite(movieDetailsDto);
            Toast.makeText(getContext(),getString(R.string.unfavorite),Toast.LENGTH_LONG).show();
        }
    }

}

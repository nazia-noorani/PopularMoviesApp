package nazianoorani.popularmoviesapp.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nazianoorani.popularmoviesapp.Fragments.ReviewFragment;
import nazianoorani.popularmoviesapp.R;
import nazianoorani.popularmoviesapp.activities.DetailsActivity;
import nazianoorani.popularmoviesapp.database.MovieContract;
import nazianoorani.popularmoviesapp.dto.MovieDetailsDto;
import nazianoorani.popularmoviesapp.dto.MovieVideosDto;
import nazianoorani.popularmoviesapp.util.AppUtil;

import static android.R.drawable.btn_star;
import static android.R.drawable.btn_star_big_on;

/**
 * Created by nazianoorani on 02/02/16.
 **/
public class TrailersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<MovieVideosDto> list = new ArrayList<>();
    MovieDetailsDto movieDetailsDto;
    Activity activity;
    MovieVideosDto movieVideosDto;
    LayoutInflater inflater;
    private ContentResolver resolver;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    int pos;
    public TrailersListAdapter(Activity activity, ArrayList<MovieVideosDto> list,MovieDetailsDto movieDetailsDto){
        this.activity = activity;
        this.list = list;
        this.movieDetailsDto = movieDetailsDto;
        inflater = LayoutInflater.from(this.activity);

    }

    @Override
    public int getItemViewType(int position) {
        if(position == TYPE_HEADER){
            return TYPE_HEADER;
        }else if(position == list.size() || list.size() == 0){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder =  null;
        if(viewType == TYPE_HEADER){
            View viewHeader = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header_items,parent,false);
            viewHolder = new MyViewHolderHeader(viewHeader);
        }else if(viewType == TYPE_ITEM) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_videos,parent,false);
            viewHolder = new MyViewHolder(viewItem);

        }else if(viewType == TYPE_FOOTER ){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_review_btn,parent,false);
            viewHolder = new MyViewHolderFooter(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        pos = position;
        if (holder instanceof MyViewHolder){

            if(list.size() != 0) {
                ImageView imageView = ((MyViewHolder) holder).imgPlay;
                movieVideosDto = list.get(position);
                String url = "http://img.youtube.com/vi/" + movieVideosDto.getKey() + "/1.jpg";
                Picasso.with(activity).load(url).into(imageView);
                ((MyViewHolder) holder).textViewName.setText(movieVideosDto.getName());
            }
        }
        else if(holder instanceof MyViewHolderHeader){
            ((MyViewHolderHeader)holder).textViewReleaseDate.setText("Release Date : " + movieDetailsDto.getReleaseDate());
            ((MyViewHolderHeader)holder).textViewVoteAverage.setText(movieDetailsDto.getVoteAverage() + "/10");
            ((MyViewHolderHeader)holder).textViewPlotSynopsis.setText(movieDetailsDto.getPlotSynopsis());

            if( ((MyViewHolderHeader)holder).isFavourtie(movieDetailsDto.getId())){
                ((MyViewHolderHeader)holder).buttonFav.setBackgroundResource(btn_star_big_on);
            }
        }

    }



    @Override
    public int getItemCount() {
        if(list.isEmpty()){
            return 2;
        }
        return list.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        TextView textViewName;
        ImageView imgPlay;

        public MyViewHolder(View item) {
            super(item);
            textViewName = (TextView) item.findViewById(R.id.textView_video);
            imgPlay = (ImageView) item.findViewById(R.id.img_play);
            imgPlay.setOnClickListener(this);
            textViewName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_play:

                case R.id.textView_video :
                    MovieVideosDto dto = list.get(getLayoutPosition());
                    if (dto.getKey() != null) {
                        if (AppUtil.isAppInstalled("com.google.android.youtube", activity)) {

                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + dto.getKey()));
                                activity.startActivity(intent);
                            } catch (ActivityNotFoundException ex) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + dto.getKey()));
                                activity.startActivity(intent);
                            }
                        } else {
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + dto.getKey()));
                                activity.startActivity(intent);

                            } catch (ActivityNotFoundException ex) {
                                Toast.makeText(activity, activity.getString(R.string.video_error), Toast.LENGTH_SHORT).show();
                            }

                        }

                        break;

                    }
            }
        }
    }

    class MyViewHolderHeader extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final Button buttonFav;
        private final TextView textViewReleaseDate;
        private final TextView textViewVoteAverage;
        private final TextView textViewPlotSynopsis;

        public MyViewHolderHeader(View item) {
            super(item);
            buttonFav = (Button) item.findViewById(R.id.buttonFav);
            textViewReleaseDate = (TextView) item.findViewById(R.id.textView_releaseDate);
            textViewVoteAverage = (TextView) item.findViewById(R.id.textView_voteAverage);
            textViewPlotSynopsis = (TextView) item.findViewById(R.id.textView_plotSynopsis);
            buttonFav.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.buttonFav:
//                toggleFavButton();
                    boolean flag = isFavourtie(movieDetailsDto.getId());
                    updateDatabase(flag, movieDetailsDto.getId());
                    break;

            }
        }

        public boolean isFavourtie(String movieId) {
            resolver = activity.getContentResolver();
            Cursor movieCursor = resolver.query(MovieContract.FavoriteMovieEntry.
                    buildFavouriteMoviesUriWithMovieId(movieId), null, null, null, null);
            if (movieCursor.getCount() == 0)
                return false;
            else
                return true;
        }

        public void updateDatabase(boolean isFavourite, String movieId) {
            if (isFavourite) {
            /* Movie already in Favourites - Delete the movie from DB*/
                resolver.delete(MovieContract.FavoriteMovieEntry.
                        buildFavouriteMoviesUriWithMovieId(movieId), null, null);
                Toast.makeText(activity, activity.getString(R.string.unfavorite), Toast.LENGTH_LONG).show();
                buttonFav.setBackgroundResource(btn_star);
            } else {
            /*Add values into DB*/
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_ID, movieDetailsDto.getId());
                contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_TITLE, movieDetailsDto.getTitle());
                contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movieDetailsDto.getReleaseDate());
                contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS, movieDetailsDto.getPlotSynopsis());
                contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, movieDetailsDto.getVoteAverage());
                contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, movieDetailsDto.getPosterpath());
                contentValues.put(MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH, movieDetailsDto.getBackdroppath());

                resolver.insert(MovieContract.FavoriteMovieEntry.CONTENT_URI, contentValues);


                Toast.makeText(activity, activity.getString(R.string.favorite), Toast.LENGTH_LONG).show();
                buttonFav.setBackgroundResource(btn_star_big_on);
            }
        }

    }

    private class MyViewHolderFooter extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button buttonReviews;
        public MyViewHolderFooter(View view) {
            super(view);
            buttonReviews = (Button) view.findViewById(R.id.button_reviews);
            buttonReviews.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_reviews:
                    Bundle bundle = new Bundle();
                    bundle.putString("movieId", movieDetailsDto.getId());
                    ReviewFragment reviewFragment = new ReviewFragment();
                    reviewFragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((DetailsActivity)activity).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, reviewFragment).addToBackStack(null).commit();
                    break;
            }
        }
    }
}


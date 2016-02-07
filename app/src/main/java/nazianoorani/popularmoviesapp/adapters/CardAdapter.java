package nazianoorani.popularmoviesapp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import nazianoorani.popularmoviesapp.Fragments.DetailsFragment;
import nazianoorani.popularmoviesapp.Fragments.MainFragment;
import nazianoorani.popularmoviesapp.R;
import nazianoorani.popularmoviesapp.activities.DetailsActivity;
import nazianoorani.popularmoviesapp.dto.MovieDetailsDto;

/**
 * Created by nazianoorani on 30/01/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    Activity activity;
    ArrayList<MovieDetailsDto> movieDetailsDtoArrayList;
    MovieDetailsDto movieDetailsDto;
    public CardAdapter(Activity activity,ArrayList<MovieDetailsDto> movieDetailsDtoArrayList){
        super();
        this.activity = activity;
        this.movieDetailsDtoArrayList = movieDetailsDtoArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_movie_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        movieDetailsDto = movieDetailsDtoArrayList.get(position);
        Picasso.with(activity)
                .load(movieDetailsDto.getPosterpath())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieDetailsDtoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imageView :
                    MovieDetailsDto movieDto = movieDetailsDtoArrayList.get(getLayoutPosition());
                    ((MainFragment.ClickCallback)activity).onItemSelected(movieDto);
                    break;

            }
        }
    }
}

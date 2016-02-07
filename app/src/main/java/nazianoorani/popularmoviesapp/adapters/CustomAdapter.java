package nazianoorani.popularmoviesapp.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import nazianoorani.popularmoviesapp.R;
import nazianoorani.popularmoviesapp.dto.MovieReviewDto;

/**
 * Created by nazianoorani on 02/02/16.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Activity activity;
    ArrayList<MovieReviewDto> list;
    public CustomAdapter(Activity activity,ArrayList<MovieReviewDto>list){
        this.activity = activity;
        this.list = list;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reviews,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieReviewDto movieReviewDto = list.get(position);
        holder.textViewContent.setText(movieReviewDto.getContent());
        holder.textViewAuthor.setText(movieReviewDto.getAuthor());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAuthor,textViewContent;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = (TextView) itemView.findViewById(R.id.textView_author);
            textViewContent = (TextView) itemView.findViewById(R.id.textView_content);
        }
    }
}

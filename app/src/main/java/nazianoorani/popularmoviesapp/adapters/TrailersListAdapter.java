package nazianoorani.popularmoviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import nazianoorani.popularmoviesapp.R;
import nazianoorani.popularmoviesapp.dto.MovieVideosDto;

/**
 * Created by nazianoorani on 02/02/16.
 */
public class TrailersListAdapter extends BaseAdapter{
    ArrayList<MovieVideosDto> list = new ArrayList<>();
    Context context;
    MovieVideosDto movieVideosDto;
    LayoutInflater inflater;
    public TrailersListAdapter(Context context, ArrayList<MovieVideosDto> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_videos, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        movieVideosDto = (MovieVideosDto)getItem(position);
        mViewHolder.textViewName.setText(movieVideosDto.getName());

        return convertView;
    }

    private class MyViewHolder{
        TextView textViewName;
        Button buttonPlay;

        public MyViewHolder(View item) {
            textViewName = (TextView) item.findViewById(R.id.textView_video);
            buttonPlay = (Button) item.findViewById(R.id.button_play);
        }
    }
}


package nazianoorani.popularmoviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nazianoorani.popularmoviesapp.Fragments.DetailsFragment;
import nazianoorani.popularmoviesapp.Fragments.MainFragment;
import nazianoorani.popularmoviesapp.R;
import nazianoorani.popularmoviesapp.dto.MovieDetailsDto;

public class MainActivity extends AppCompatActivity implements MainFragment.ClickCallback{
    boolean mTwoPane;
    private static final String DETAIL_FRAGMENT_TAG ="detailFragment";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.frame_container)!= null){
            mTwoPane = true;
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new DetailsFragment()).commit();
            }
        }else{
            mTwoPane = false;
        }

    }


    @Override
    public void onItemSelected(MovieDetailsDto movieDetailsDto) {
        Bundle args = new Bundle();
        args.putParcelable(DetailsFragment.PARCELABLE_MOVIE_DETAILS_DTO, movieDetailsDto);
        if (mTwoPane) {

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtras(args);
            startActivity(intent);
        }
    }
}

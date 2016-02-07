package nazianoorani.popularmoviesapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import nazianoorani.popularmoviesapp.Fragments.DetailsFragment;
import nazianoorani.popularmoviesapp.R;

/**
 * Created by nazianoorani on 31/01/16.
 **/
public class DetailsActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState == null) {
            DetailsFragment fragment = new DetailsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        }
    }

}

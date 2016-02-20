package nazianoorani.popularmoviesapp.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by nazianoorani on 18/02/16.
 */
public class MovieContract {

        public static final String CONTENT_AUTHORITY = "nazianoorani.popularmoviesapp";
        // Build Base URI for content provider
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        // Possible paths (appended to base content URI for possible URI's)
        // For instance, content://nazianoorani.popularmoviesapp/favouriteMovies/ is a valid path for
        // looking at favorite movie data.
        public static final String PATH_FAVOURITES = "favouriteMovies";

        public static long normalizeDate(long startDate) {
            // normalize the start date to the beginning of the (UTC) day
            Time time = new Time();
            time.set(startDate);
            int julianDay = Time.getJulianDay(startDate, time.gmtoff);
            return time.setJulianDay(julianDay);
        }


    public static final class FavoriteMovieEntry implements BaseColumns {

        // Movie table name
        public static final String TABLE_NAME = "favouriteMovies";
        // Contacts Table Columns names
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

        public static Uri buildFavouritesURI(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // content://..../MovieId
        public static Uri buildFavouriteMoviesUriWithMovieId(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }


    }

}

package nazianoorani.popularmoviesapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import nazianoorani.popularmoviesapp.dto.MovieDetailsDto;

/**
 * Created by nazianoorani on 03/02/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Movie.db";
    static DatabaseHelper sInstance;

//    // Movie table name
//    private static final String TABLE_NAME = "favouriteMovies";
//
//    // Contacts Table Columns names
//    private static final String COLUMN_ID = "id";
//    private static final String COLUMN_POSTER_PATH = "poster_path";
//    private static final String COLUMN_BACKDROP_PATH = "backdrop_path";
//    private static final String COLUMN_TITLE = "title";
//    private static final String COLUMN_VOTE_AVERAGE = "vote_average";
//    private static final String COLUMN_RELEASE_DATE = "release_date";
//    private static final String COLUMN_PLOT_SYNOPSIS = "plot_synopsis";


    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        final String SQL_CREATE_FAVOURITE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
//                MovieContract.FavoriteMovieEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
//                MovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
//                MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
//                MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH+ " TEXT NOT NULL, " +
//                MovieContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL, " +
//                MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
//                MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
//                ");";
        final String SQL_CREATE_FAVOURITE_MOVIE_TABLE = "Create TABLE " + MovieContract.FavoriteMovieEntry.TABLE_NAME+"("
                + MovieContract.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavoriteMovieEntry.COLUMN_ID + " TEXT NOT NULL, "
                + MovieContract.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS + " TEXT, "
                + MovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT, "
                + MovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH + " TEXT, "
                + MovieContract.FavoriteMovieEntry.COLUMN_BACKDROP_PATH + " TEXT, "
                + " UNIQUE (" + MovieContract.FavoriteMovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

            db.execSQL(SQL_CREATE_FAVOURITE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavoriteMovieEntry.TABLE_NAME);
        // Create tables again
        onCreate(db);

    }
//    // Adding new movie to fav movie db
//   public void favouriteMovie(MovieDetailsDto movieDetailsDto) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_ID, movieDetailsDto.getId());
//        values.put(COLUMN_TITLE, movieDetailsDto.getTitle());
//        values.put(COLUMN_POSTER_PATH, movieDetailsDto.getPosterpath());
//        values.put(COLUMN_BACKDROP_PATH, movieDetailsDto.getBackdroppath());
//        values.put(COLUMN_PLOT_SYNOPSIS, movieDetailsDto.getPlotSynopsis());
//        values.put(COLUMN_RELEASE_DATE, movieDetailsDto.getReleaseDate());
//        values.put(COLUMN_VOTE_AVERAGE, movieDetailsDto.getVoteAverage());
//
//        // Inserting Row
//        db.insert(TABLE_NAME, null, values);
//        db.close(); // Closing database connection
//    }
//   public MovieDetailsDto getMovieDetailsDto(int id){
//    // Getting single contact
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_NAME, new String[] { COLUMN_ID,
//                        COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE,COLUMN_TITLE,COLUMN_PLOT_SYNOPSIS,
//                        COLUMN_BACKDROP_PATH,COLUMN_RELEASE_DATE }, COLUMN_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        MovieDetailsDto movieDetailsDto = new Contact(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return contact;
//    }
//
//    public ArrayList<MovieDetailsDto> getFavMovieDtoList() {
//        ArrayList<MovieDetailsDto> favMovieList = new ArrayList<MovieDetailsDto>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                MovieDetailsDto movieDetailsDto = new MovieDetailsDto();
//                movieDetailsDto.setId(cursor.getString(0));
//                movieDetailsDto.setTitle(cursor.getString(1));
//                movieDetailsDto.setPosterpath(cursor.getString(2));
//                movieDetailsDto.setBackdroppath(cursor.getString(3));
//                movieDetailsDto.setPlotSynopsis(cursor.getString(4));
//                movieDetailsDto.setReleaseDate(cursor.getString(5));
//                movieDetailsDto.setVoteAverage(cursor.getString(6));
//
//                // Adding movie dto to list
//                favMovieList.add(movieDetailsDto);
//            } while (cursor.moveToNext());
//        }
//
//        // return fav movie list
//        return favMovieList;
//    }
//
//    //deleting a particular movie from the fav list
//    public void unFavourite(MovieDetailsDto movieDetailsDto) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
//                new String[] {movieDetailsDto.getId() });
//        db.close();
//    }

}

package nazianoorani.popularmoviesapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by nazianoorani on 18/02/16.
 */
public class MovieProvider extends ContentProvider {

    static SQLiteQueryBuilder sQueryBuilder;
    static final int FAVOURITES = 100;
    static final int FAVOURITE_WITH_ID = 101;
    private DatabaseHelper mOpenHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    static {
        sQueryBuilder = new SQLiteQueryBuilder();
        sQueryBuilder.setTables(MovieContract.FavoriteMovieEntry.TABLE_NAME);
    }

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVOURITES,FAVOURITES);
        mUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_FAVOURITES+"/#", FAVOURITE_WITH_ID);
        return mUriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = DatabaseHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        int match = mUriMatcher.match(uri);
        switch (match){
            case FAVOURITES :  retCursor = mOpenHelper.getReadableDatabase().query(
                    MovieContract.FavoriteMovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
                break;
        case FAVOURITE_WITH_ID:
            retCursor = mOpenHelper.getReadableDatabase().query(
                    MovieContract.FavoriteMovieEntry.TABLE_NAME,
                    projection,
                    MovieContract.FavoriteMovieEntry.COLUMN_ID + "= ?",
                    new String[]{String.valueOf(ContentUris.parseId(uri))},
                    null,
                    null,
                    sortOrder);
            break;
        default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case FAVOURITE_WITH_ID:
                return MovieContract.FavoriteMovieEntry.CONTENT_ITEM_TYPE;
            case FAVOURITES:
                return MovieContract.FavoriteMovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri returnUri;
        db.beginTransaction();
        switch (match) {
            case FAVOURITES: {
                long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);
                if (_id > 0){
                    returnUri = MovieContract.FavoriteMovieEntry.buildFavouritesURI(_id);
                    db.setTransactionSuccessful();
                }else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                db.endTransaction();
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int retVal;
        if(selection==null)
            selection = "1";
        switch (match){
            case FAVOURITES:
                retVal = db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case FAVOURITE_WITH_ID:
                retVal = db.delete(MovieContract.FavoriteMovieEntry.TABLE_NAME, MovieContract.FavoriteMovieEntry.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(retVal!=0)
            getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return retVal;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int retVal;
        db.beginTransaction();
        switch (match){
            case FAVOURITES: {
                retVal = db.update(MovieContract.FavoriteMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                db.setTransactionSuccessful();
                db.endTransaction();
                break;
            }default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(retVal!=0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return retVal;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case FAVOURITES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.FavoriteMovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}

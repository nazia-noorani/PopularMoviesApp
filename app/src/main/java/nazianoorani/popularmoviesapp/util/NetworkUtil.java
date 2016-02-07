package nazianoorani.popularmoviesapp.util;


import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by nazianoorani on 01/02/16.
 */
public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo() != null){
            return true;
        }
        else{
            return false;
        }
    }
}

package nazianoorani.popularmoviesapp.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by nazianoorani on 06/02/16.
 */
public class AppUtil {

    public static boolean isAppInstalled(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}

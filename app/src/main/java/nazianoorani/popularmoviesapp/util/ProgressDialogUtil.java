package nazianoorani.popularmoviesapp.util;

import android.app.ProgressDialog;
import android.content.Context;


public class ProgressDialogUtil {

    private static ProgressDialog pDialog;

    public static void showDialog(Context context, String message) {
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);
        pDialog.setMessage(message);
        pDialog.show();
    }

    public static void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}

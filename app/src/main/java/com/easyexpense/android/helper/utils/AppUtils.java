package com.easyexpense.android.helper.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.easyexpense.android.R;
import com.easyexpense.android.helper.AppConstants;
import com.easyexpense.android.ledgerdetail.LedgerDetailActivity;
import com.easyexpense.commons.utils.StringUtils;
import com.geekmk.db.dto.Board;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Mani on 12/03/17.
 */

public class AppUtils {

    //It makes no sense in initializing a Utility class :)
    private AppUtils() {
    }

    public static void intentNavigate(String intentData,Class<?> targetClass,Context context) {
        Intent intent = new Intent(context, targetClass);
        if(StringUtils.isNotEmpty(intentData))
            intent.putExtra(AppConstants.IntentConstants.INTENT_DATA,intentData);
        context.startActivity(intent);
    }


    public static void intentNavigateWithResult(long id,Class<?> targetClass,Activity context,int reqCode) {
        Intent intent = new Intent(context, targetClass);
        intent.putExtra(AppConstants.IntentConstants.INTENT_DATA,id);
        context.startActivityForResult(intent, reqCode);
    }

    public static void intentNavigate(long id,Class<?> targetClass,Context context) {
        Intent intent = new Intent(context, targetClass);

            intent.putExtra(AppConstants.IntentConstants.INTENT_DATA,id);
        context.startActivity(intent);
    }

    public static void navigateTransaction(long id,String scenario,Class<?> targetClass,Context context) {
        Intent intent = new Intent(context, targetClass);

        intent.putExtra(AppConstants.IntentConstants.INTENT_DATA,id);
        intent.putExtra(AppConstants.IntentConstants.CURRENT_SCENARIO,scenario);
        context.startActivity(intent);
    }



    public static void closeKeyBoard(Context context) {
        try {
            if (context != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        } catch (Exception e) {
        }
    }

    public static void showKeyBoard(Context context) {
        try {
            if (context != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
        }
    }



    public static long getCurrentDate() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String convertDateFormat(long currentTimeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return sdf.format(currentTimeInMillis);
    }

    public static void createShortCutForLedger(Context context,Board ledgerBean) {
            Intent shortcutIntent = new Intent(context,
                    LedgerDetailActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);

            Intent addIntent = new Intent();
            addIntent
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, ledgerBean.getName());
            addIntent.putExtra(AppConstants.IntentConstants.INTENT_DATA,ledgerBean.getId());
            addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(context,
                            R.mipmap.ic_launcher));


            addIntent
                    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");

            context.sendBroadcast(addIntent);
    }

    public static void displayImage(Context context, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW,fileUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if(intent.resolveActivity(context.getPackageManager())!=null){
            context.startActivity(intent);
        }else{
            Toast.makeText(context,"Cannot Display Image",Toast.LENGTH_LONG).show();
        }


    }

    public static void displaySnackBar(View coordinatorLayout, String description) {
        Snackbar.make(coordinatorLayout, description, Snackbar.LENGTH_LONG).show();
    }

    public static void displayToast(String message,Context context){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void removeNotification(int syncNotifyId, Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(syncNotifyId);
    }


    public static void displayExportNotification(Context context,String title,String text,int notificationId){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

    public static void displayExcelCreatedNotification(Context context, File fileName) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Excel Created")
                        .setContentText("Check the created File Under Easy Expense Directory");

        Uri uri = FileProvider.getUriForFile(context,"com.easyexpense.commons",fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if(intent.resolveActivity(context.getPackageManager()) !=null){
            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(LedgerDetailActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // mId allows you to update the notification later on.
            mNotificationManager.notify(12, mBuilder.build());
        }else{

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // mId allows you to update the notification later on.
            mNotificationManager.notify(12, mBuilder.build());

        }

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }


}

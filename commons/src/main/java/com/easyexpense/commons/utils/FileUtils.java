package com.easyexpense.commons.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.easyexpense.commons.android.constants.GlobalConstants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mani on 24/03/17.
 */

public class FileUtils {


    private FileUtils(){}



    public static Uri getUniqueFileUri(Context context) throws IOException {
        File imageFile = createImageFile();
        return  FileProvider.getUriForFile(context,"com.easyexpense.commons",imageFile);
    }


    private static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File externalPictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                externalPictureDirectory      /* directory */
        );
    }


    public static File createExcelFile(String name) throws IOException{


        File directory = new File(Environment.getExternalStorageDirectory()+ GlobalConstants.BASE_DIRECTORY);

        return File.createTempFile(name,GlobalConstants.EXCEL_EXTENSION,directory);


    }


    public static String getNonConflictingFileName(String fileName){
        createAppDirectoryIfNotExists();
        File myFile = new File(Environment.getExternalStorageDirectory()+ GlobalConstants.BASE_DIRECTORY+"/"+fileName);
        if(myFile.exists()){
            return fileName+System.currentTimeMillis();
        }
        else{
            return fileName;
        }
    }

    public static void createAppDirectoryIfNotExists() {
        File f = new File(Environment.getExternalStorageDirectory()+ GlobalConstants.BASE_DIRECTORY);
        if(!f.exists()){
            // directory is not present so carry on with file creating a new directory
            try {
                f.mkdir();
            }
            catch (Exception error)
            {
                error.printStackTrace();
            }
        }
    }


    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState));
    }


    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }


}

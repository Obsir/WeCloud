package com.obser.wecloud.fixtures;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.obser.wecloud.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Obser on 2017/8/7.
 */

public class Avatars {

    private static Context context;

    public static void init(Context context){
        Avatars.context = context;
    }

    public static String getAvatar(){
        Resources resources = context.getResources();
        final Uri uri_1 =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.status_servant_1_94) + "/"
                + resources.getResourceTypeName(R.drawable.status_servant_1_94) + "/"
                + resources.getResourceEntryName(R.drawable.status_servant_1_94));
        final Uri uri_2 =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.status_servant_1_557) + "/"
                + resources.getResourceTypeName(R.drawable.status_servant_1_557) + "/"
                + resources.getResourceEntryName(R.drawable.status_servant_1_557));
        final Uri uri_3 =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.status_servant_1_1056) + "/"
                + resources.getResourceTypeName(R.drawable.status_servant_1_1056) + "/"
                + resources.getResourceEntryName(R.drawable.status_servant_1_1056));
        final Uri uri_4 =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.status_servant_1_1585) + "/"
                + resources.getResourceTypeName(R.drawable.status_servant_1_1585) + "/"
                + resources.getResourceEntryName(R.drawable.status_servant_1_1585));
        final Uri uri_5 =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.status_servant_2_449) + "/"
                + resources.getResourceTypeName(R.drawable.status_servant_2_449) + "/"
                + resources.getResourceEntryName(R.drawable.status_servant_2_449));
        final Uri uri_6 =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.status_servant_3_392) + "/"
                + resources.getResourceTypeName(R.drawable.status_servant_3_392) + "/"
                + resources.getResourceEntryName(R.drawable.status_servant_3_392));
        final Uri uri_7 =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.status_servant_3_548) + "/"
                + resources.getResourceTypeName(R.drawable.status_servant_3_548) + "/"
                + resources.getResourceEntryName(R.drawable.status_servant_3_548));
        ArrayList<String> avatars = new ArrayList<String>() {
            {
                add(uri_1.toString());
                add(uri_2.toString());
                add(uri_3.toString());
                add(uri_4.toString());
                add(uri_5.toString());
                add(uri_6.toString());
                add(uri_7.toString());
            }
        };

        Random random = new Random();
        int index = random.nextInt(7);
        return avatars.get(index);
    }


}

package com.skyware.droidbluetooth.utils;

import android.Manifest;
import android.Manifest.permission;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;
import java.util.UUID;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class Util {

    /**
     * Get last token from split string by separator.
     * For example, when given text is "abc/def|ghi/jkl/mno|pqr/stu" and separator is "|", result is "pqr/stu".
     * @param s
     * @param separator
     * @return
     */
    public static String getLastToken(String s, String separator) {
        return s.substring(s.lastIndexOf(separator) + 1);
    }

    /**
     * Logging 을 위한 app version code 가져오기
     * @param context
     * @return versionCode
     */
    public static int getAppVersion(Context context) {

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            String versionName = packageInfo.versionName;
            return packageInfo.versionCode;
//            return new StringBuffer().append(versionName).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 앱버전(versionName) 확인
     * @param mContext
     * @return
     */
    public static String getVersionName(Context mContext) {
        String versionNmae= "";
        try {
            PackageInfo mPackageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionNmae = mPackageInfo.versionName;
        } catch (Exception e) {
            Logging.i("getVersionName", e.toString());
        }

        return versionNmae;
    }

    /**
     * +82로 나오는 폰번호 0으로 시작하게 변경
     * @param phoneNumber
     * @return ex) +821012345678 => 01012345678
     */
    public static String subStrPhoneNumber(String phoneNumber) {
        if(!TextUtils.isEmpty(phoneNumber)) {
            if(phoneNumber.startsWith("+82")) {
                phoneNumber = phoneNumber.substring(phoneNumber.length() - 10, phoneNumber.length());
                phoneNumber = "0" + phoneNumber;
            }
        }
        return phoneNumber;
    }

    /**
     * Get app package name
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        String packageName = "";
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            packageName = i.packageName;
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    public static String getAppName(Context mContext) {
        String appName = "";
        try {
            appName = (String) mContext.getPackageManager().getApplicationLabel
                    (mContext.getPackageManager().getApplicationInfo(
                            getAppPackageName(mContext), PackageManager.GET_UNINSTALLED_PACKAGES));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appName;
    }

    /**
     * 앱구동 확인
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isRunningProcess(Context context, String packageName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();

        for(ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list) {
            if(runningAppProcessInfo.processName.equals(packageName)) {
                isRunning = true;
                break;
            }
        }

        return isRunning;
    }
}

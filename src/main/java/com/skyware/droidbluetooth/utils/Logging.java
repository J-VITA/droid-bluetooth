package com.skyware.droidbluetooth.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class Logging {

    /**
     * 로그 출력 여부
     */
    private static boolean isLoggingMode = Globals.DEFAULT_LOGGING_MODE;

    /**
     * 로그 출력 태그
     */
    private static final String TAG = "SkywareModule";

    private static String getFormattedLog(StackTraceElement ste, String preFix, String postFix) {
        String classPath = Util.getLastToken(ste.getClassName(), ".");
        String method = ste.getMethodName();

        return new StringBuffer().append(preFix).append(classPath).append("::").append(method).append(postFix).toString();
    }

    public static String getFormattedLog(String log) {
        StackTraceElement ste = (new Throwable()).getStackTrace()[2];
        return getFormattedLog(ste, "", "> " + log);
    }

    public static String getFormmatedTag() {
        StackTraceElement ste = (new Throwable()).getStackTrace()[2];
        return getFormattedLog(ste, TAG, "");
    }

    public static void setLoggingMode(boolean isLoggingMode) {
        Logging.isLoggingMode = isLoggingMode;
    }

    public static boolean getLoggingMode() {
        return Logging.isLoggingMode;
    }


    // verbose
    public static void v() {
        try {
            if(isLoggingMode) {
                Log.v(TAG, getFormattedLog(""));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void v(String str) {
        try {
            if(isLoggingMode) {
                Log.v(TAG, getFormattedLog(str));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void v(String str, Exception e) {
        try {
            if(isLoggingMode) {
                Log.v(TAG, getFormattedLog(str), e);
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void v(Object... objects) {
        try {
            if (isLoggingMode) {
                StringBuffer sb = new StringBuffer();
                for (Object obj : objects) {
                    sb.append(obj);
                }
                Log.v(TAG, getFormattedLog(sb.toString()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void pv(String preFix, Object... objects) {
        try {
            if (isLoggingMode) {
                StringBuffer sb = new StringBuffer();
                if (!TextUtils.isEmpty(preFix)) {
                    sb.append("[").append(preFix).append("] ");
                }
                for (Object obj : objects) {
                    sb.append(obj);
                }
                Log.v(TAG, getFormattedLog(sb.toString()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // info
    public static void i() {
        try {
            if (isLoggingMode) {
                Log.i(TAG, getFormattedLog(""));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void i(String str) {
        try {
            if (isLoggingMode) {
                Log.i(TAG, getFormattedLog(str));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void i(String str, Exception e) {
        try {
            if (isLoggingMode) {
                Log.i(TAG, getFormattedLog(str), e);
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void i(Object... objects) {
        try {
            if (Log.isLoggable(Globals.LOG_TAG, Log.INFO) || isLoggingMode) {
                StringBuffer sb = new StringBuffer();
                for (Object obj : objects) {
                    sb.append(obj);
                }
                Log.i(TAG, getFormattedLog(sb.toString()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void i(String priFix, Object... objects) {
        try {
            if (isLoggingMode) {
                StringBuffer sb = new StringBuffer();
                if (TextUtils.isEmpty(priFix)) {
                    sb.append("[").append(priFix).append("] ");
                }
                for (Object obj : objects) {
                    sb.append(obj);
                }
                Log.i(TAG, getFormattedLog(sb.toString()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // warning

    public static void w() {
        try {
            if (isLoggingMode) {
                Log.w(TAG, getFormattedLog(""));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void w(String str) {
        try {
            if (isLoggingMode) {
                Log.w(TAG, getFormattedLog(str));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void w(Exception e) {
        try {
            if (isLoggingMode) {
                Log.w(getFormattedLog(""), e);
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void w(String str, Exception e) {
        try {
            if (isLoggingMode) {
                Log.w(TAG, getFormattedLog(str), e);
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void w(String str, Throwable e) {
        try {
            if (isLoggingMode) {
                Log.w(TAG, getFormattedLog(str), e);
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void w(Object... objects) {
        try {
            if (isLoggingMode) {
                StringBuffer sb = new StringBuffer();
                for(Object obj : objects) {
                    sb.append(obj);
                }
                Log.w(TAG, getFormmatedTag());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void pw(String preFix, Object... objects) {
        try {
            StringBuffer sb = new StringBuffer();
            if ((!TextUtils.isEmpty(preFix))) {
                sb.append("[").append(preFix).append("] ");
            }
            for (Object obj : objects) {
                sb.append(obj);
            }
            Log.w(TAG, getFormattedLog(sb.toString()));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // debug
    public static void d() {
        try {
            if (isLoggingMode) {
                Log.d(TAG, getFormattedLog(""));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void d(String str) {
        try {
            if (isLoggingMode) {
                Log.d(TAG, getFormattedLog(str));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void d(String str, Exception e) {
        try {
            Log.d(TAG, getFormattedLog(str), e);
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void d(Object... objects) {
        try {
            if (isLoggingMode) {
                StringBuffer sb = new StringBuffer();
                for (Object obj : objects) {
                    sb.append(obj);
                }
                Log.d(TAG, getFormattedLog(sb.toString()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void d(String preFix, Object... objects) {
        try {
            if (isLoggingMode) {
                StringBuffer sb = new StringBuffer();
                if (!TextUtils.isEmpty(preFix)) {
                    sb.append("[").append(preFix).append("] ");
                }
                for (Object obj : objects) {
                    sb.append(obj);
                }
                Log.d(TAG, getFormattedLog(sb.toString()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // error

    public static void e(String str) {
        try {
            if (isLoggingMode) {
                Log.e(TAG, getFormattedLog(str));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void e(String str, Exception e) {
        try {
            if (isLoggingMode) {
                Log.e(TAG, getFormattedLog(str), e);
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void e(String str, Throwable e) {
        try {
            if (isLoggingMode) {
                Log.e(TAG, getFormattedLog(str), e);
            }
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
    }

    public static void printAppVersion(Context mContext) {
        if (isLoggingMode) {
            StringBuffer sb = new StringBuffer("\n");
            sb.append("*** ").append("\n");
            sb.append("*** Skyware Bluetooth Module Application Version : ").append(Util.getAppVersion(mContext)).append("\n");
            sb.append("*** ").append("\n");

            Logging.v(sb.toString());
        }
    }
}

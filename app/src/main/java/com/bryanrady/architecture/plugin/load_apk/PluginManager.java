package com.bryanrady.architecture.plugin.load_apk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2019/5/27.
 */

public class PluginManager {

    private static PluginManager mPluginManager = new PluginManager();

    private PluginManager(){}

    public static PluginManager getInstance(){
        return mPluginManager;
    }

    private PackageInfo mPackageInfo;
    private Resources mResources;
    private DexClassLoader mDexClassLoader;

    /**
     * 实例化 DexClassLoader 和 Resources
     * @param context
     */
    public boolean loadPluginApk(Context context){
        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String name = "taopiaopiao.apk";
        String dexPath = new File(filesDir, name).getAbsolutePath();

        PackageManager packageManager = context.getPackageManager();
        mPackageInfo = packageManager.getPackageArchiveInfo(dexPath,PackageManager.GET_ACTIVITIES);

        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        mDexClassLoader = new DexClassLoader(dexPath,dexOutFile.getAbsolutePath(),null,context.getClassLoader());
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,dexPath);
            mResources = new Resources(assetManager,context.getResources().getDisplayMetrics(),context.getResources().getConfiguration());
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public DexClassLoader getDexClassLoader() {
        return mDexClassLoader;
    }

    public Resources getResources() {
        return mResources;
    }

    public PackageInfo getPackageInfo() {
        return mPackageInfo;
    }
}

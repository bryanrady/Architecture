package com.bryanrady.architecture;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.bryanrady.architecture.plugin.hook.HookUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by Administrator on 2019/1/3.
 */

public class FrameApplication extends Application {

    private static final String TAG = "FrameApplication";

    public static FrameApplication INSTANCE = null;

    public static FrameApplication getInstance(){
        return INSTANCE;
    }

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG,"attachBaseContext");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        HookUtil hookUtil = new HookUtil();
        hookUtil.hookClipSystemService(this);

        //1.hook实现集中式登录
        hookUtil.hookIActivityManager(this);
        hookUtil.hookMHHandleMessage(this);


        //2.hook + 融合dexElements数组实现集中式登录
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin/dexelements.apk";
        String apkCachePath = getCacheDir().getAbsolutePath();
        //融合class
        injectPluginClass(apkPath, apkCachePath);
        //融合resource
        loadPluginResources(apkPath);

        //3.hook + 构建LoadedApk添加到map集合中实现集中式登录
        String apkPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin/loadedapk.apk";
        hookUtil.putPluginLoadedApkToArrayMap(this, apkPath2);

    }

    /**
     *
     * 合并宿主和插件的class的过程
     *
     * 加载插件中的class文件    如何加载呢？
     *      把插件apk的dex文件合并到宿主apk的dex文件中 ，即将插件中的 dexElements数组合并到宿主中的 dexElements
     *
     *      BaseDexClassLoader  DexPathList  Element[] dexElements
     * @param apkPath       插件apk目录，从网络下载后放在sd卡
     * @param cachePath     插件apk缓存目录
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void injectPluginClass(String apkPath, String cachePath){
        if(apkPath == null || cachePath == null){
            return;
        }
        try {
            //第一步：找到插件apk中的 dexElements数组
            DexClassLoader pluginDexClassLoader = new DexClassLoader(apkPath, cachePath,null, getClassLoader());
            Field pluginPathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pluginPathListField.setAccessible(true);
            Object pluginPathList = pluginPathListField.get(pluginDexClassLoader);

            Field pluginDexElementsField = pluginPathList.getClass().getDeclaredField("dexElements");
            pluginDexElementsField.setAccessible(true);
            Object pluginDexElements = pluginDexElementsField.get(pluginPathList);

            //第二步：找到宿主apk中的 dexElements数组
            PathClassLoader pathClassLoader = (PathClassLoader) getClassLoader();
            Field systemPathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            systemPathListField.setAccessible(true);
            Object systemPathList = systemPathListField.get(pathClassLoader);

            Field systemDexElementsField = systemPathList.getClass().getDeclaredField("dexElements");
            systemDexElementsField.setAccessible(true);
            Object systemDexElements = systemDexElementsField.get(systemPathList);

            //第三步：将上面的dexElements数组合并成新的dexElements，然后通过反射重新注入系统的Field（dexElements ）变量中

            //获取到两个数组的长度 然后合并成新的dexElements数组长度
            int pluginDexElementsLength = Array.getLength(pluginDexElements);
            int systemDexElementsLength = Array.getLength(systemDexElements);
            int dexElementsLength = pluginDexElementsLength + systemDexElementsLength;

            //找到dexElement数组中每一个成员的Class类型  Element
            Class elementClass = systemDexElements.getClass().getComponentType();
            //创建一个新的dexElements数组
            Object dexElements = Array.newInstance(elementClass, dexElementsLength);
            //将两个数组中的元素合并到这个新的dexElements数组中
            for (int i=0;i<dexElementsLength;i++){
                if(i < pluginDexElementsLength){
                    Array.set(dexElements, i, Array.get(pluginDexElements,i));
                }else{
                    Array.set(dexElements, i, Array.get(systemDexElements,i-pluginDexElementsLength));
                }
            }
            //然后将原来系统中的dexElements数组替换成新的dexElements数组
            systemDexElementsField.set(systemPathList,dexElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并Resource
     * @param apkPath
     */
    private void loadPluginResources(String apkPath){
        try {
            mAssetManager = AssetManager.class.newInstance();
            //addAssetPath(String path)
            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(mAssetManager,apkPath);

            //调用AssetManager的ensureStringBlocks()后，插件的StringBloack被实例化了
            Method ensureStringBlocksMethod = AssetManager.class.getDeclaredMethod("ensureStringBlocks");
            ensureStringBlocksMethod.setAccessible(true);
            ensureStringBlocksMethod.invoke(mAssetManager);

            mResources = new Resources(mAssetManager,getResources().getDisplayMetrics(),getResources().getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }
}

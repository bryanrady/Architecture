package com.bryanrady.architecture.plugin.insert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

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
        }catch (Exception e) {
            e.printStackTrace();
        }

        return parseReceiver(context,new File(dexPath));

    }

    /**
     * 注册插件中的广播
     * @param context
     * @param packageFile
     * @return
     */
    private boolean parseReceiver(Context context, File packageFile) {
        try {
            //解析apk包的入口在    PMS.main --> PMS构造函数 -> mAppInstallDir（/data/data目录下） --》scanDirLI();
            // --> scanPackageLI() --> PackageParser.parsePackage() -->PackageParser.Package -->ArrayList<Activity> receivers
            // -->Activity是一个组件Component-->

            //第一步：先拿到PackageParser对象
            Class packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object packageParser = packageParserClass.newInstance();
            //第二步: 拿到PackageParser中的parsePackage方法 执行 parsePackage 方法 得到 Package对象
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage",File.class,int.class);
            Object packageObj = parsePackageMethod.invoke(packageParser, packageFile, PackageManager.GET_ACTIVITIES);
            // Package 里面有 receivers 这个字段 public final ArrayList<Activity> receivers = new ArrayList<Activity>(0);
            // 拿到receivers  广播集合   插件app存在多个广播   集合  ArrayList<Activity>  name  ————》 ActivityInfo   className
            Field receiversField = packageObj.getClass().getDeclaredField("receivers");
            ArrayList receivers = (ArrayList) receiversField.get(packageObj);
            Class activityClass = Class.forName("android.content.pm.PackageParser$Activity");

            //第三步: 将得到Activity组件转换成ActivityInfo
            Class packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            //generateActivityInfo(Activity a, int flags,PackageUserState state, int userId)
            Method generateActivityInfoMethod = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    activityClass, int.class, packageUserStateClass, int.class);
            Object packageUserState = packageUserStateClass.newInstance();

            Class userHandlerClass = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandlerClass.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);
            //不明白userId为什么是0
            Log.d("wangqingbin","userId=="+userId);

            //第四步: 将得到Component组件中的intents字段
            Class componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClass.getDeclaredField("intents");

            //第五步: 将receivers集合进行遍历 得到每个receiver的name 和 intentFilter 然后注册广播
            for(Object activity : receivers){
                ActivityInfo activityInfo = (ActivityInfo) generateActivityInfoMethod
                        .invoke(packageParser, activity, 0, packageUserState, userId);

                //ActivityInfo extends ComponentInfo extends PackageItemInfo
                PackageItemInfo packageItemInfo = activityInfo;

                //packageItemInfo.name  -->  Public name of this item. From the "android:name" attribute. -->receiver的全类名
                Class receiverClassName = getDexClassLoader().loadClass(packageItemInfo.name);
                BroadcastReceiver receiver = (BroadcastReceiver) receiverClassName.newInstance();

                //得到每一个组件的intentFilter 一个Receicer可能对应多个intentFilter
                ArrayList<? extends IntentFilter> intents = (ArrayList<? extends IntentFilter>) intentsField.get(activity);

                for (IntentFilter intentFilter : intents) {
                    context.registerReceiver(receiver, intentFilter);
                }
            }
            return true;
        } catch (Exception e) {
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

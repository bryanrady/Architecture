package com.bryanrady.architecture.plugin.hook.loadedapk;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2019/6/6.
 */

public class LoadedPluginClassLoader extends DexClassLoader {

    public LoadedPluginClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

}

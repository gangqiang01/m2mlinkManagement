package com.m2m.management.utils;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.UseFeature;
import org.dom4j.Namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GetApkInfo {
    private static final Logger logger = LoggerFactory.getLogger(GetApkInfo.class);
    private static final Namespace NS = Namespace.get("http://schemas.android.com/apk/res/android");

    public static Map<String, Object> readApk(File apkUrl) {
        Map<String, Object> resMap = new HashMap<String, Object>();
        try (ApkFile apkFile = new ApkFile(apkUrl)) {
            ApkMeta apkMeta = apkFile.getApkMeta();
            resMap.put("filename", apkMeta.getName());
            resMap.put("pkgname", apkMeta.getPackageName());
            resMap.put("versioncode", apkMeta.getVersionCode());
            resMap.put("versionname", apkMeta.getVersionName());
            for (UseFeature feature : apkMeta.getUsesFeatures()) {
                System.out.println(feature.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return resMap;
    }
}

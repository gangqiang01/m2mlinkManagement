package com.m2m.management.utils;

import com.m2m.management.controller.RepoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static Boolean copyFile(File ofile,String newPath) throws IOException{
        logger.info("newcopypath:"+newPath);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {

            in = new FileInputStream(ofile);
            File file = new File(newPath);
            if (file.exists()) {
                return false;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            out = new FileOutputStream(file);
            FileChannel fcin = in.getChannel();
            FileChannel fcout = out.getChannel();
            fcin.transferTo(0, fcin.size(), fcout);
            in.close();
            out.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static Boolean createDir(String path){
        logger.info("createdirpath:"+path);
        File dir=new File(path);
        if(dir.exists()){
           return false;
        }
        return dir.mkdirs();
    }
    public static Boolean delFile(String path,String filename){
        File file;
        if (path.endsWith(File.separator)) {
            file = new File(path+filename);
        } else {
            file = new File(path+File.separator+filename);
        }
        if(file.exists() && file.isFile() && !file.canExecute()){
            return file.delete();
        }else{
            return false;
        }
    }

// delete dir and files in dir
    public static Boolean delDir(String dirPath) {
        try {
            delAllFiles(dirPath);
            File myFilePath = new File(dirPath);
            return  myFilePath.delete();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
// delete all file in dir
    public static boolean delAllFiles(String path) {
        System.out.println("path:"+path);
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                f.delete();
            }
            if (f.isDirectory()) {
                if (path.endsWith(File.separator)) {
                    delAllFiles(path  + f.getName());
                    delDir(path  + f.getName());
                } else {
                    delAllFiles(path + File.separator + f.getName());
                    delDir(path + File.separator + f.getName());
                }
                flag = true;
            }
        }
        return flag;
    }


    public static Boolean isOnlyChildDir(String path){
        File dir=new File(path);
        if(dir.exists() && dir.isDirectory() && dir.canExecute()){
            File[] files=dir.listFiles();
            if(files != null && files.length > 1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }
}

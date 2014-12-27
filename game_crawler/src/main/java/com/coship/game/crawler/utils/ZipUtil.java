package com.coship.game.crawler.utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


public class ZipUtil {

	    static final int BUFFER = 8192;  
	  
	    /**
	     * 压缩工具类
	     * @param srcPathName
	     * @param stream 可以为空
	     */
	    public static void compress(String srcPathName,OutputStream stream) {  
	        File file = new File(srcPathName);  
	        if (!file.exists())  
	            throw new RuntimeException(srcPathName + "不存在！");  
	        try {  
	        	if(stream==null){
	        		stream = new FileOutputStream(file);
	        	}
	            CheckedOutputStream cos = new CheckedOutputStream(stream,  
	                    new CRC32());  
	            ZipOutputStream out = new ZipOutputStream(cos);  
	            out.setEncoding("GBK");
	            String basedir = "";  
	            compress(file, out, basedir);  
	            out.close();  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        }  
	    }  
	  
	    private static void compress(File file, ZipOutputStream out, String basedir) {  
	        /* 判断是目录还是文件 */  
	        if (file.isDirectory()) {  
	            System.out.println("压缩：" + basedir + file.getName());  
	            compressDirectory(file, out, basedir);  
	        } else {  
	            System.out.println("压缩：" + basedir + file.getName());  
	            compressFile(file, out, basedir);  
	        }  
	    }  
	  
	    /** 压缩一个目录 */  
	    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {  
	        if (!dir.exists())  
	            return;  
	  
	        File[] files = dir.listFiles();  
	        for (int i = 0; i < files.length; i++) {  
	            /* 递归 */  
	            compress(files[i], out, basedir + dir.getName() + "/");  
	        }  
	    }  
	  
	    /** 压缩一个文件 */  
	    private static void compressFile(File file, ZipOutputStream out, String basedir) {  
	        if (!file.exists()) {  
	            return;  
	        }  
	        try {  
	            BufferedInputStream bis = new BufferedInputStream(  
	                    new FileInputStream(file));  
	            ZipEntry entry = new ZipEntry(basedir + file.getName());  
	            out.putNextEntry(entry);  
	            int count;  
	            byte data[] = new byte[BUFFER];  
	            while ((count = bis.read(data, 0, BUFFER)) != -1) {  
	                out.write(data, 0, count);  
	            }  
	            bis.close();  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        }  
	    }  
	
	public static void main(String[] args) {
		ZipUtil.compress("E:\\apk_test\\1419056939447\\", null);
	
	}

}



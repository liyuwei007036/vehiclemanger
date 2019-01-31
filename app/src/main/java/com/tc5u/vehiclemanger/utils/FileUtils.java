package com.tc5u.vehiclemanger.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by zhulx on 2016/10/23.
 */
public class FileUtils {

    public static String getSuffix(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return null;
    }


    public static String removePrefix(String src, String prefix) {
        if (src != null && src.startsWith(prefix)) {
            return src.substring(prefix.length());
        }
        return src;
    }


    private static void close(InputStream is, OutputStream os) {
        if (is != null)
            try {
                is.close();
            } catch (IOException e) {
            }
        if (os != null)
            try {
                os.close();
            } catch (IOException e) {
            }
    }

    public static void unzip(String zipFilePath) throws IOException {
        String targetPath = zipFilePath.substring(0, zipFilePath.lastIndexOf("."));
        unzip(zipFilePath, targetPath);
    }

    public static void unzip(String zipFilePath, String targetPath) throws IOException {
        ZipFile zipFile = new ZipFile(zipFilePath);
        try{
            Enumeration<?> entryEnum = zipFile.entries();
            if (null != entryEnum) {
                while (entryEnum.hasMoreElements()) {
                    OutputStream os = null;
                    InputStream is = null;
                    try {
                        ZipEntry zipEntry = (ZipEntry) entryEnum.nextElement();
                        if (!zipEntry.isDirectory()) {
                            File targetFile = new File(targetPath + File.separator + zipEntry.getName());
                            if (!targetFile.getParentFile().exists()) {
                                targetFile.getParentFile().mkdirs();
                            }
                            os = new BufferedOutputStream(new FileOutputStream(targetFile));
                            is = zipFile.getInputStream(zipEntry);
                            byte[] buffer = new byte[4096];
                            int readLen = 0;
                            while ((readLen = is.read(buffer, 0, 4096)) > 0) {
                                os.write(buffer, 0, readLen);
                            }
                        }
                    } finally {
                        if (is != null)
                            is.close();
                        if (os != null)
                            os.close();
                    }
                }
            }
        }finally{
            zipFile.close();
        }
    }
	 public static String extensionName(String filename) {   
	        if ((filename != null) && (filename.length() > 0)) {   
	            int dot = filename.lastIndexOf('.');   
	            if ((dot >-1) && (dot < (filename.length() - 1))) {   
	                return filename.substring(dot + 1);   
	            }   
	        }   
	        return filename;   
	    }

}

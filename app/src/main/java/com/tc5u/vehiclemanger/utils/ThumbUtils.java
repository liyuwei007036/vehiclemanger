package com.tc5u.vehiclemanger.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * https://blog.csdn.net/bravejiezai/article/details/51251815
 */
public class ThumbUtils {
    /**
     * 只压缩质量的方法
     *
     * @param file       源文件
     * @param targetPath 目标路径
     * @param quality    质量 取值为0-100 100表示按最大质量，此参数对PNG图片无效,即compress方法的第一个参数为
     *                   Bitmap.CompressFormat.PNG时，quality的设置是无效的。
     **/
    public static File scalFile(File file, String targetPath, int quality) {
        try {
            //将文件转换为字节数组
            byte[] bytes = getBytesFromFile(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            if (bitmap != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                baos.close();
                File targetFile = new File(targetPath);
                if (targetFile.exists()) {
                    boolean flag = targetFile.delete();
                    Log.i("ImageUtils.scalFile()", "flag: " + flag);
                }
                FileOutputStream fos = new FileOutputStream(targetFile);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
                return targetFile;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ImageUtils.scalFile()", e.getMessage());
        }
        return null;
    }


    /**
     * 压缩到指定分辨率的方法 以1280X960为例
     *
     * @param file       源文件
     * @param targetPath 目标路径
     **/
    public static File scalFile(@NonNull File file, @NonNull String targetPath, @NonNull int swidth, @NonNull int sheight) {
        long fileSize = file.length();
        final long fileMaxSize = 200 * 1024;//超过200K的图片需要进行压缩
        if (fileSize > fileMaxSize) {
            try {
                byte[] bytes = getBytesFromFile(file);//将文件转换为字节数组
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅仅解码边缘区域
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                //得到宽高
                int width = options.outWidth;
                int height = options.outHeight;
                float scaleWidth = 0f;
                float scaleHeight = 0f;
                Matrix matrix = new Matrix();
                if (width > height) {
                    scaleWidth = (float) swidth / width;
                    scaleHeight = (float) sheight / height;

                } else {
                    scaleWidth = (float) sheight / width;
                    scaleHeight = (float) swidth / height;
                }
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                matrix.postScale(scaleWidth, scaleHeight);//执行缩放
                Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
                if (resizeBitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int quality = 100;
                    resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    //限制压缩后图片最大为200K，否则继续压缩
                    while (baos.toByteArray().length > fileMaxSize) {
                        baos.reset();
                        quality -= 10;
                        resizeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                    }
                    baos.close();
                    File targetFile = new File(targetPath);
                    if (targetFile.exists()) {
                        boolean flag = targetFile.delete();
                        Log.i("ImageUtils.scalFile()", "flag: " + flag);
                    }
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                    return targetFile;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ImageUtils.scalFile()", e.getMessage());
            }
            return null;
        } else {
            return file;
        }
    }

    /**
     * 返回一个byte数组
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static byte[] getBytesFromFile(File file) {
        byte[] bytes = null;
        try {
            InputStream is = new FileInputStream(file);

            // 获取文件大小
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // 文件太大，无法读取
                throw new IOException("File is to large " + file.getName());
            }

            // 创建一个数据来保存文件数据

            bytes = new byte[(int) length];

            // 读取数据到byte数组中

            int offset = 0;

            int numRead = 0;

            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // 确保所有数据均被读取

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }

            // Close the input stream and return bytes
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}

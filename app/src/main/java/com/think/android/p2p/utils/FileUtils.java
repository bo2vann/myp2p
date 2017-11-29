package com.think.android.p2p.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Think on 2017/11/1.
 */

public class FileUtils {

    /**
     * Created by zhxu on 2017/11/1.
     */

    public static String readAssetsTxt(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open("bj.txt");
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

    /**
     * 将base64字符解码保存文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        if (targetPath == null) {
//            targetPath = Environment.getExternalStorageDirectory();
        }
        Log.d("", base64Code);
        byte[] pdfAsBytes = Base64.decode(base64Code, Base64.DEFAULT);
        saveFile(pdfAsBytes, targetPath, "contact.pdf");
    }

    //参数一、文件的byte流
//参数二、文件要保存的路径
//参数三、文件保存的名字
    public static void saveFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        File file = null;
        try {
            //通过创建对应路径的下是否有相应的文件夹。
//            File dir = new File(filePath);
//            if (!dir.exists()) {// 判断文件目录是否存在
//                //如果文件存在则删除已存在的文件夹。
//                dir.mkdirs();
//            }

            //如果文件存在则删除文件
            file = new File(Environment.getExternalStorageDirectory(), fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
//        File file = new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/renbao/pdf/contact.pdf/contact.pdf");
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = FileProvider.getUriForFile(getApplication(), "com.amarsoft.picccus.fileprovider",
//                file);
//        intent.setDataAndType(uri, "application/pdf");
//        startActivity(intent);

    }

    public static void openPdf(Context context) {
        try {
            decoderBase64File(readAssetsTxt(context, null), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = null;
        file = new File(Environment.getExternalStorageDirectory(), "contact.pdf");

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.think.android.p2p.fileprovider",
                file);
        intent.setDataAndType(uri, "application/pdf");
        context.startActivity(intent);
    }

}

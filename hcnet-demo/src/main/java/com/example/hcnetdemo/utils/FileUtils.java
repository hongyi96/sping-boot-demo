package com.example.hcnetdemo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: hongyi
 * @date: 2019/8/22 16:46
 */
public class FileUtils {

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    /**
     * 获取文件扩展名
     *
     * @param file
     * @return
     */
    public static String getExtention(File file) {
        if (file == null) {
            return null;
        }
        if (file.isDirectory()) {
            return "";
        }
        String fileName = file.getName();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        return prefix;
    }

    /**
     * 文件拷贝
     *
     * @param resourceFileName 源文件
     * @param targetFileName   目的文件
     * @return
     * @throws IOException
     */
    public static boolean copyFile(String resourceFileName, String targetFileName) throws IOException {
        return copyFile(new File(resourceFileName), new File(targetFileName));
    }

    /**
     * 文件拷贝
     *
     * @param resourceFileName 源文件
     * @param targetFile       目的文件
     * @return
     * @throws IOException
     */
    public static boolean copyFile(String resourceFileName, File targetFile) throws IOException {
        return copyFile(new File(resourceFileName), targetFile);
    }

    /**
     * 文件拷贝
     *
     * @param resourceFile   源文件
     * @param targetFileName 目的文件
     * @return
     * @throws IOException
     */
    public static boolean copyFile(File resourceFile, String targetFileName) throws IOException {
        return copyFile(resourceFile, new File(targetFileName));
    }

    /**
     * 文件拷贝
     *
     * @param resourceFile 源文件
     * @param targetFile   目的文件
     * @return
     * @throws IOException
     */
    public static boolean copyFile(File resourceFile, File targetFile) throws IOException {
        if (resourceFile == null || targetFile == null) {
            return false;
        }

        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            if (resourceFile.exists()) {
                if (!targetFile.exists()) {
                    File parentFile = targetFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    targetFile.createNewFile();
                }
                in = new FileInputStream(resourceFile);
                out = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1024 * 8];
                int i = 0;

                while ((i = in.read(buffer)) != -1) {
                    out.write(buffer, 0, i);
                }
                out.flush();
                return true;
            } else {
                return false;
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 要删除文件的全路径
     */
    public static void deleteFile(String fileName) {
        if (fileName != null) {
            deleteFile(new File(fileName));
        }
    }

    /**
     * 删除文件
     *
     * @param file 要删除的文件
     */
    public static void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }


    /**
     * @Description: 获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件
     */
    public static List<String> getFileNames(String path) {
        List<String> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                //path
                files.add(tempList[i].toString());
                //文件名，不包含路径
                String fileName = tempList[i].getName();

            }
        }
        return files;
    }

    /**
     * @return List<File>
     * @Description: 获取某个目录下所有直接下级文件，不包括目录下的子目录的下的文件
     */
    public static List<File> getFiles(String path) {
        List<File> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i]);
            }
        }

        return files;
    }

    public static String uploadSingleFile(String pathname, MultipartFile fileList) {
        try {
            String filename = fileList.getOriginalFilename();
            File dir = new File(pathname);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filepath = pathname + filename;
            File serverFile = new File(filepath);
            fileList.transferTo(serverFile);
            return filepath;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
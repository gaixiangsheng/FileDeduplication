package com.erlin;

import com.erlin.entity.FileEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

public class FileCalculateTask extends RecursiveTask<FileEntity> {
    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    private TreeMap<String, FileEntity> mFileDeduplicationMap;
    private boolean isCalculate;
    private FileEntity mFileEntity;

    public FileCalculateTask(TreeMap<String, FileEntity> deduplicationMap, boolean isCalculate, FileEntity entity) {
        this.mFileDeduplicationMap = deduplicationMap;
        this.isCalculate = isCalculate;
        this.mFileEntity = entity;
    }

    @Override
    protected FileEntity compute() {
        if (isCalculate) {
            long fileLen = mFileEntity.getLenth();
            if (fileLen <= 512) {//512字节
                byte[] buf = getFile512Byte(mFileEntity.getFile());
                mFileEntity.setUniquenessCode(getMD5(buf));
            } else {//大于512字节
                //TODO 整个文件长度/100,取2个字节,即共即200个字节，计算MD5
                byte[] buf = getFile200Byte(mFileEntity.getFile());
                mFileEntity.setUniquenessCode(getMD5(buf));
            }
            System.out.println("md5:" + mFileEntity.getUniquenessCode());
        } else {
            Iterator<String> keys = mFileDeduplicationMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                FileEntity value = mFileDeduplicationMap.get(key);
                FileCalculateTask task = new FileCalculateTask(null, true, value);
                invokeAll(task);
            }
        }

        return mFileEntity;
    }

    private static byte[] getFile200Byte(File file) {
        byte[] bytes = new byte[200];
        long lenth = file.length();
        long step = lenth / 100;
        try {
            InputStream inputStream = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
            int index = 0;
            for (long i = 0; i < lenth; i += step) {
                inputStream.skip(i);
                byte[] twoBytes = new byte[2];
                inputStream.read(twoBytes);
                if (index >= bytes.length) {
                    break;
                }
                bytes[index] = twoBytes[0];
                bytes[index + 1] = twoBytes[1];
                index += 2;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    private static byte[] getFile512Byte(File file) {
        byte[] bytes = new byte[512];
        try {
            InputStream inputStream = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
            inputStream.read(bytes);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    private static String getMD5(byte[] bytes) {
        String md5 = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            md5 = toHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    private static String toHexString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}

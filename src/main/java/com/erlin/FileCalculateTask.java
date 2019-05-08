package com.erlin;

import com.erlin.entity.FileEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

public class FileCalculateTask extends RecursiveTask<FileEntity> {
    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    private TreeMap<String, FileEntity> mFileDeduplicationMap;
    private boolean isCalculate;
    private FileEntity mFileEntity;
    private static final int READ_FILE_BLOCK = 512;

    public FileCalculateTask(TreeMap<String, FileEntity> deduplicationMap, boolean isCalculate, FileEntity entity) {
        this.mFileDeduplicationMap = deduplicationMap;
        this.isCalculate = isCalculate;
        this.mFileEntity = entity;
    }

    @Override
    protected FileEntity compute() {
        if (isCalculate) {
            mFileEntity.setUniquenessCode(getFileMd5(mFileEntity.getFile()));
            System.out.println(mFileEntity.getPath()+",md5:"+mFileEntity.getUniquenessCode());
        } else {
            Iterator<String> keys = mFileDeduplicationMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                FileEntity value = mFileDeduplicationMap.get(key);
                FileCalculateTask task = new FileCalculateTask(null, true, value);
                invokeAll(task);
            }
        }
        System.out.println("-");
        return mFileEntity;
    }

    private static String getFileMd5(File file) {
        String md5 = "";
        try {
            int length = (int) file.length();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            InputStream inputStream = Files.newInputStream(file.toPath(), StandardOpenOption.READ);
            if (length <= READ_FILE_BLOCK * 2) {
                byte[] bs1 = new byte[length];
                while ((length = inputStream.read(bs1)) != -1) {
                    messageDigest.update(bs1, 0, length);
                }
            } else {
                byte[] bs1 = new byte[READ_FILE_BLOCK];
                int len = -1;
                boolean isFirst = true;
                while ((len = inputStream.read(bs1, 0, READ_FILE_BLOCK)) != -1) {
                    messageDigest.update(bs1, 0, len);
                    if (isFirst) {
                        inputStream.skip(length / 2);
                        isFirst = false;
                    } else {
                        inputStream.skip(length - READ_FILE_BLOCK);
                    }
                }
            }

            inputStream.close();
            md5 = toHexString(messageDigest.digest());
        } catch (IOException e) {
            e.printStackTrace();
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

package com.erlin;

import com.erlin.entity.FileEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;

public class FileDeduplication {
    private ForkJoinPool mThreadPool;
    private TreeMap<Long, ArrayList<FileEntity>> mAllFilsMap;
    public FileDeduplication() {
        mThreadPool = ForkJoinPool.commonPool();
        mAllFilsMap = new TreeMap<>();
    }

    public static void main(String[] args){
        long time = System.currentTimeMillis();
        FileDeduplication mFileDeduplication = new FileDeduplication();
        FileTask mTask = new FileTask(new File("/home/erlin/Documents/webmagic/"),mFileDeduplication.mAllFilsMap);
        mFileDeduplication.mThreadPool.invoke(mTask);
        mFileDeduplication.mThreadPool.shutdown();
        System.out.println(mFileDeduplication.mAllFilsMap.size());
        System.out.println("耗时："+(System.currentTimeMillis()-time));
    }
}

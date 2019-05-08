package com.erlin;

import com.erlin.entity.FileEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;

public class FileDeduplication {
    private ForkJoinPool mThreadPool;
    private TreeMap<Long, ArrayList<FileEntity>> mAllFilsMap;
    private TreeMap<String,FileEntity> mFileDeduplicationMap;
    public FileDeduplication() {
        mThreadPool = ForkJoinPool.commonPool();
        mAllFilsMap = new TreeMap<>();
    }

    public static void main(String[] args){
        long time = System.currentTimeMillis();
        FileDeduplication mFileDeduplication = new FileDeduplication();

        AllFileTask mTask = new AllFileTask(new File("/mnt/HHD-2TB-1/webmagic"),mFileDeduplication.mAllFilsMap);
        mFileDeduplication.mThreadPool.invoke(mTask);


        DeduplicationFileTask mDeduplicationTask = new DeduplicationFileTask(mFileDeduplication.mAllFilsMap);
        mFileDeduplication.mFileDeduplicationMap = mFileDeduplication.mThreadPool.invoke(mDeduplicationTask);

        if(mFileDeduplication.mFileDeduplicationMap!=null){
            System.out.println("文件总数："+(mFileDeduplication.mAllFilsMap.size()+mFileDeduplication.mFileDeduplicationMap.size()));
            System.out.println("文件长度相同的有："+mFileDeduplication.mFileDeduplicationMap.size());
            System.out.println("计算长度相同文件的唯一值：");

            FileCalculateTask mCalculateTask = new FileCalculateTask(mFileDeduplication.mFileDeduplicationMap,false,null);
            mFileDeduplication.mThreadPool.invoke(mCalculateTask);

            System.out.println("计算长度相同文件的唯一值：完成");
        }
        mFileDeduplication.mThreadPool.shutdown();
        System.out.println(mFileDeduplication.mAllFilsMap.size());
        System.out.println("耗时："+(System.currentTimeMillis()-time));
    }
}

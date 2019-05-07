package com.erlin;

import com.erlin.entity.FileEntity;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

public class FileCalculateTask extends RecursiveTask<FileEntity> {
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
            if(fileLen<=512){//512字节
                //TODO 计算MD5
            }else{//大于512字节
                //TODO 整个文件长度/100,取2个字节,即共即200个字节，计算MD5
            }
        } else {
            Iterator<String> keys = mFileDeduplicationMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                FileEntity value = mFileDeduplicationMap.get(key);
                FileCalculateTask task = new FileCalculateTask(null,true,value);
                invokeAll(task);
            }
        }

        return mFileEntity;
    }


}

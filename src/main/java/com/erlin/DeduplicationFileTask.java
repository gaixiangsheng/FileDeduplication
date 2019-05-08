package com.erlin;

import com.erlin.entity.FileEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

public class DeduplicationFileTask extends RecursiveTask<TreeMap<String,FileEntity>> {
    private TreeMap<Long, ArrayList<FileEntity>> mAllFileMap;
    private TreeMap<String, FileEntity> mDeduplicationMap;

    public DeduplicationFileTask(TreeMap<Long, ArrayList<FileEntity>> fileMap){
        this.mDeduplicationMap = new TreeMap<>();
        this.mAllFileMap = fileMap;
    }

    @Override
    protected TreeMap<String,FileEntity> compute() {
        Iterator<Long> keys = mAllFileMap.keySet().iterator();
        while (keys.hasNext()){
            long key = keys.next();
            ArrayList<FileEntity> value = mAllFileMap.get(key);
            if(value.size()>1){
                for(FileEntity e:value){
                    mDeduplicationMap.put(e.getPath(),e);
                }
            }
        }
        return mDeduplicationMap;
    }
}

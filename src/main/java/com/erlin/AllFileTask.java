package com.erlin;

import com.erlin.entity.FileEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.RecursiveTask;

public class AllFileTask extends RecursiveTask<Long> {
    File fileDir;
    TreeMap<Long, ArrayList<FileEntity>> mFileTreeMap;

    public AllFileTask(File dir, TreeMap<Long, ArrayList<FileEntity>> map) {
        this.fileDir = dir;
        mFileTreeMap = map;
    }


    @Override
    protected Long compute() {
        File[] files = fileDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                AllFileTask task = new AllFileTask(file, mFileTreeMap);
                invokeAll(task);
            } else {
                addFileEntity(file);
            }
        }

        return 0L;
    }

    public void addFileEntity(File fileEntity) {

        ArrayList<FileEntity> entities = null;
        FileEntity entity = new FileEntity();
        entity.setFile(fileEntity);

        long key = fileEntity.length();
        if (mFileTreeMap.containsKey(key)) {
            entities = mFileTreeMap.get(key);
            entities.add(entity);
        }else{
            entities = new ArrayList<>();
            entities.add(entity);
            mFileTreeMap.put(key, entities);
        }
    }
}

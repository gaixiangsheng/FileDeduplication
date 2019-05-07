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
                addFileEntity(file.length(), file.getAbsolutePath());
            }
        }

        return 0L;
    }

    public void addFileEntity(Long len, String path) {
        ArrayList<FileEntity> entities = new ArrayList<>();
        FileEntity entity = new FileEntity();

        if (mFileTreeMap.containsKey(len)) {
            entities = mFileTreeMap.get(len);
        }

        entity.setLenth(len);
        entity.setPath(path);
        entities.add(entity);

        mFileTreeMap.put(len, entities);
    }
}

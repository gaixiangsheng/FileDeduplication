package com.erlin.entity;

import java.io.File;

public class FileEntity {
    private File mFile;
    private String uniquenessCode;//只有多个文件长度相同时，才会计算该码

    public String getPath() {
        return mFile.getAbsolutePath();
    }

    public long getLenth() {
        return mFile.length();
    }

    public String getUniquenessCode() {
        return uniquenessCode;
    }

    public void setUniquenessCode(String uniquenessCode) {
        this.uniquenessCode = uniquenessCode;
    }

    public File getFile() {
        return mFile;
    }

    public void setFile(File mFile) {
        this.mFile = mFile;
    }
}

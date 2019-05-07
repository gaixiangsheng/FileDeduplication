package com.erlin.entity;

public class FileEntity {
    private String path;//文件绝对路径
    private long lenth;//文件长度
    private String uniquenessCode;//只有多个文件长度相同时，才会计算该码

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLenth() {
        return lenth;
    }

    public void setLenth(long lenth) {
        this.lenth = lenth;
    }

    public String getUniquenessCode() {
        return uniquenessCode;
    }

    public void setUniquenessCode(String uniquenessCode) {
        this.uniquenessCode = uniquenessCode;
    }
}

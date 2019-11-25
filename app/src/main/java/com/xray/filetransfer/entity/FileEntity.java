package com.xray.filetransfer.entity;

/**
 * Created by cfp on 2019-11-25.
 */
public class FileEntity {

    public String fileName;

    public long fileSize;

    public String fileThumbnail;

    @Override
    public String toString() {
        return "FileEntity{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileThumbnail='" + fileThumbnail + '\'' +
                '}';
    }
}

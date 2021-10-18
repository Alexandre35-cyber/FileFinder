package org.maison.filefinder.model;

public class ResultLine {

    private String date;
    private String file;
    private long size;

    public ResultLine(String date, String file, long size) {
        this.date = date;
        this.file = file;
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public String getFile() {
        return file;
    }

    public long getSize() {
        return size;
    }
}

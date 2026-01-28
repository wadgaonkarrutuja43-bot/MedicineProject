package com.example.medicineexpiryapp;

public class History {
    private String qrContent;
    private String type;
    private long timestamp;

    public History(String qrContent, String type, long timestamp) {
        this.qrContent = qrContent;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getQrContent() {
        return qrContent;
    }

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

package com.lombardrisk.arproduct.pojo;

public class ExportToVal {
    private String type;
    private String id;
    private String level;
    private String instances;
    private String extendGridId;
    private String status;
    private String message;
    private String checkStatus;
    private String arVersion;
    private int rowIndex;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(final String level) {
        this.level = level;
    }

    public String getInstances() {
        return instances;
    }

    public void setInstances(final String instances) {
        this.instances = instances;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(final String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getArVersion() {
        return arVersion;
    }

    public void setArVersion(final String arVersion) {
        this.arVersion = arVersion;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(final int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getExtendGridId() {
        return extendGridId;
    }

    public void setExtendGridId(final String extendGridId) {
        this.extendGridId = extendGridId;
    }
}

package controllers;

import java.util.Arrays;

public enum NoticeStatus {

    ALL(-1, "ALL"), SENT(1, "SENT"), DRAFT(2, "DRAFT"), READ(3, "READ"), UNREAD(4, "UNREAD");

    private int statusId = 0;
    private String statusName = "";

    NoticeStatus(int statusId,String statusName){
        this.statusId = statusId;
        this.statusName = statusName;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public static NoticeStatus valueOfName(String statusName){
        return Arrays.stream(values()).filter(e -> e.getStatusName().equalsIgnoreCase(statusName)).findFirst().orElse(null);
    }

    public static NoticeStatus valueOfId(Integer id){
        return Arrays.stream(values()).filter(e -> e.getStatusId() == id).findFirst().orElse(null);
    }

    public static boolean isDraft(int statusId){
        return DRAFT.getStatusId() == statusId;
    }

    public static boolean isSent(int statusId){
        return SENT.getStatusId() == statusId;
    }

}

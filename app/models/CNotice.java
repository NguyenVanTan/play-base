package models;

import play.libs.Json;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the c_notices database table.
 */
@Entity
@Table(name = "c_notices")
@NamedQueries({
        @NamedQuery(
                name = "CNotice.findAll",
                query = "SELECT c FROM CNotice c"
        ),
        @NamedQuery(
                name = "CNotice.findAllCreatedNotice",
                query = "select p from CNotice p where p.createdBy = :createdById order by p.creationTime DESC"
        ),
        @NamedQuery(
                name = "CNotice.findCreatedNoticeByStatus",
                query = "select p from CNotice p where p.createdBy = ? and p.status = ? order by p.creationTime DESC"
        ),
        @NamedQuery(
                name = "CNotice.findNoticeById",
                query = "select p from CNotice p where p.noticeId = ?"
        )
})

public class CNotice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int noticeId;

    @Column(name = "created_by")
    private int createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_time")
    private Date creationTime;

    @Column(name = "notice_message")
    private String noticeMessage;

    @Column(name = "notice_type")
    private int noticeType;

    private int status;

    public String getReceiver() {
        return receiver;
    }

    @Column
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Transient
    private String receiver;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    public CNotice() {
    }

    public int getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getNoticeMessage() {
        return this.noticeMessage;
    }

    public String getShortMessage() {
        if (noticeMessage == null || noticeMessage.length() < 50) {
            return noticeMessage;
        }
        return noticeMessage.substring(0, 50) + " ...";
    }

    public void setNoticeMessage(String noticeMessage) {
        this.noticeMessage = noticeMessage;
    }

    public int getNoticeType() {
        return this.noticeType;
    }

    public void setNoticeType(int noticeType) {
        this.noticeType = noticeType;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String toString(){
        return "Notice " + Json.toJson(this).toString();
    }
}
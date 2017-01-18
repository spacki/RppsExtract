package com.ge.hc.gpps.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/26/12
 * Time: 8:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Image {
    int id;
    String sopInstanstanceUid;
    String reason;
    int rejectorId;
    String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRejectorId() {
        return rejectorId;
    }

    public void setRejectorId(int rejectorId) {
        this.rejectorId = rejectorId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSopInstanstanceUid() {
        return sopInstanstanceUid;
    }

    public void setSopInstanstanceUid(String sopInstanstanceUid) {
        this.sopInstanstanceUid = sopInstanstanceUid;
    }

    public Image() {
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", sopInstanstanceUid='" + sopInstanstanceUid + '\'' +
                ", reason='" + reason + '\'' +
                ", rejectorId=" + rejectorId +
                ", date='" + date + '\'' +
                '}';
    }
}

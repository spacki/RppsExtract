package com.ge.hc.gpps.domain;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/20/12
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Study {
    int id;
    String accessionNumber;
    String studyInstanceUid;
    byte[] rppsData;
    List<String> sopInstanceUids;
    String patName;
    String birthDate;
    String patId;
    String procedurerCode;
    String studyDate;
    String modality;

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public String getProcedurerCode() {
        return procedurerCode;
    }

    public void setProcedurerCode(String procedurerCode) {
        this.procedurerCode = procedurerCode;
    }


    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getSopInstanceUids() {
        return sopInstanceUids;
    }

    public void setSopInstanceUids(List<String> sopInstanceUids) {
        this.sopInstanceUids = sopInstanceUids;
    }

    public byte[] getRppsData() {
        return rppsData;
    }

    public void setRppsData(byte[] rppsData) {
        this.rppsData = rppsData;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getStudyInstanceUid() {
        return studyInstanceUid;
    }

    public void setStudyInstanceUid(String studyInstanceUid) {
        this.studyInstanceUid = studyInstanceUid;
    }

    @Override
    public String toString() {
        return "Study{" +
                "id=" + id +
                ", patientIdentifier='" + patId + '\'' +
                ", patientName='" + patName + '\'' +
                ", patientBirthDate='" + birthDate + '\'' +
                ", accessionNumber='" + accessionNumber + '\'' +
                ", studyInstanceUid='" + studyInstanceUid + '\'' +
                ", studyDate='" + studyDate + '\'' +
                ", modalityCode='" + modality + '\'' +
                ", procedureCode='" + procedurerCode + '\'' +
                '}';
    }
}

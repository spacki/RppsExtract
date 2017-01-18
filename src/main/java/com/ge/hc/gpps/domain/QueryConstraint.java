package com.ge.hc.gpps.domain;

/**
 * Created with IntelliJ IDEA.
 * User: 100026806
 * Date: 6/17/13
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryConstraint {

    private int authorityCkey;
    private String updateTS;

    public int getAuthorityCkey() {
        return authorityCkey;
    }

    public void setAuthorityCkey(int authorityCkey) {
        this.authorityCkey = authorityCkey;
    }

    public String getUpdateTS() {
        return updateTS;
    }

    public void setUpdateTS(String updateTS) {
        this.updateTS = updateTS;
    }
}

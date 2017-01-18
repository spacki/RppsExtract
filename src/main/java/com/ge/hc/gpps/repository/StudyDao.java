package com.ge.hc.gpps.repository;

import com.ge.hc.gpps.domain.Study;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/20/12
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StudyDao {

    public Study getStudy(int id, int authorityCkey);

    public List<Study> getStudies();

    public List<Integer> getKeys(String updateTS);
}

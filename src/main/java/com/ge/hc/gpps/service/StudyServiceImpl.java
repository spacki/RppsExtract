package com.ge.hc.gpps.service;

import com.ge.hc.gpps.domain.Study;
import com.ge.hc.gpps.repository.StudyDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/20/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudyServiceImpl implements StudyService  {

    private StudyDao studyDao;


    public void setStudyDao(StudyDao studyDao) {
        this.studyDao = studyDao;
    }

    @Override
    public Study getStudy(int id, int authorityCkey) {
        return this.studyDao.getStudy(id, authorityCkey);
    }

    @Override
    public List<Study> getStudies() {
       return  this.studyDao.getStudies();
    }

    @Override
    public List<Integer> getKeys(String updateTs) {
        return this.studyDao.getKeys(updateTs);
    }


}




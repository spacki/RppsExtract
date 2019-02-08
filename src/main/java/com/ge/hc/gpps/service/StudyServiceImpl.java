package com.ge.hc.gpps.service;

import com.ge.hc.gpps.domain.Study;
import com.ge.hc.gpps.repository.StudyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/20/12
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudyServiceImpl implements StudyService  {

    /* The application logger */
    private static final Logger logger = LoggerFactory.getLogger(StudyServiceImpl.class);

    private StudyDao studyDao;


    public void setStudyDao(StudyDao studyDao) {
        this.studyDao = studyDao;
    }

    @Override
    public Study getStudy(int id, int authorityCkey) {
        Study study = null;
        try {
            study = this.studyDao.getStudy(id, authorityCkey);
        } catch (Exception e) {
            logger.warn("could not find a valid rpps entry for " + id);
            logger.error(e.getMessage());

        }
        return study;
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




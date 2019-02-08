package com.ge.hc.gpps;

import com.ge.hc.gpps.domain.Image;
import com.ge.hc.gpps.domain.QueryConstraint;
import com.ge.hc.gpps.domain.Study;
import com.ge.hc.gpps.service.ImageService;
import com.ge.hc.gpps.service.StudyService;
import com.ge.hc.gpps.tools.Helpers;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class App {

    private static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        Calendar cal = Calendar.getInstance();
        Date start = cal.getTime();
        ApplicationContext beanFactory = new ClassPathXmlApplicationContext("applicationContext.xml");

        logger.warn("GPPS Service started " + start);
        StudyService studyService = (StudyService) beanFactory.getBean("studyService");
        ImageService imageService = (ImageService) beanFactory.getBean("imageService");
        // get list of exam_ckey which have annotation stored
        QueryConstraint constraint = (QueryConstraint) beanFactory.getBean("queryConstraint");
        List<Integer> keys = studyService.getKeys(constraint.getUpdateTS());
        logger.debug("processing " + keys.size() + " studies");
        // iterate over that list


        try {
            FileWriter fstreamCsv = new FileWriter("listOfAllHiddenImages.txt", true);
            BufferedWriter outCsv = new BufferedWriter(fstreamCsv);
            outCsv.write("PatientID|PatientName|BirthDate|AccessionNumber|Modality|StudyDate|ProcedureCode|StudyInstanceUID|SOPInstanceUID");
            outCsv.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (Integer key : keys) {
            logger.debug("getting study for key " + key);
            Study study = studyService.getStudy(key, constraint.getAuthorityCkey());
            if (study == null) {
                continue;
            }
            List<String> sopInstanceUids = new ArrayList<String>();
            study.setSopInstanceUids(sopInstanceUids);
            logger.debug("process study : " + study);

            try {
                //Helpers.writeFile(study.getRppsData(), study.getId() + ".zip");
                // get a list of SOP InstanceUIDs
                // calling help method to store the annotation on the disk
                Helpers.writeFile(study);

                sopInstanceUids = Helpers.unzip(study);
                //sopInstanceUids = Helpers.unzip(study.getId());
                if (sopInstanceUids.size() == 0) {
                    logger.debug(" no hidden images for study " + study);
                } else {
                    logger.debug("add the hidden images to the conversion procedure call");
                    FileWriter fstreamSql = new FileWriter("callStoredProcedure.txt", true);
                    BufferedWriter outSql = new BufferedWriter(fstreamSql);
                    FileWriter fstreamCsv = new FileWriter("listOfAllHiddenImages.txt", true);
                    BufferedWriter outCsv = new BufferedWriter(fstreamCsv);
                    //outCsv.write("PatientID|PatientName|BirthDate|AccessionNumber|Modality|StudyDate|ProcedureCode|StudyInstanceUID|SOPInstanceUID");
                    study.setSopInstanceUids(sopInstanceUids);
                    for (String sopInstanceUid : study.getSopInstanceUids()) {
                        logger.debug("get information for SOP Instance UID " + sopInstanceUid);
                        Image image = null;
                        try {
                            image = imageService.findImage(sopInstanceUid);
                        } catch (Exception e) {
                            logger.debug("Image for sop: " + sopInstanceUid + " not found ");
                            continue;
                        }
                        logger.debug("Image: " + image);
                        // wir muessen die stored procedure calls speichern
                        //FileWriter fstream = new FileWriter("callStoredProcedure.txt", true);
                        //BufferedWriter out = new BufferedWriter(fstream);
                        StringBuffer sb = new StringBuffer();
                        sb.append("exec reject_image @image_ckey=" + image.getId()
                                + ", @reason_desc=\'" + image.getReason()
                                + "\', @rejector_stid_c2c=" + image.getRejectorId()
                                + ", @reject_dttm_c2c=\'" + image.getDate() + "\'");
                        outSql.write(sb.toString() + "\r\n");
                        //Close the output stream
                        //out.close();
                        // special solution for QEHB (Queen Elizabeth Hospital Birmingham)
                        //fstream = new FileWriter("listOfAllHiddenImages.txt", true);
                        //out = new BufferedWriter(fstream);
                        sb = new StringBuffer();
                        if (study.getAccessionNumber() == null || study.getAccessionNumber().trim().isEmpty()) {
                            study.setAccessionNumber("unknown");
                        }
                        logger.debug("Accession Number: " + study.getAccessionNumber());
                        sb.append(study.getPatId() + "|" + study.getPatName() + "|" + study.getBirthDate() + "|" +
                                study.getAccessionNumber() + "|" + study.getModality() + "|" +
                                study.getStudyDate() + "|" +
                                study.getProcedurerCode() + "|" + study.getStudyInstanceUid() + "|" +
                                image.getSopInstanstanceUid());
                        outCsv.write(sb.toString() + "\r\n");
                        //Close the output stream
                        //out.close();
                    }
                    outSql.close();
                    outCsv.close();
                }

            } catch (IOException e) {
                logger.warn("could not store GPPS data as zip");
                e.getMessage();
            }


        }
        //logger.warn("found " + studies.size() + " studies");
        Calendar calEnd = Calendar.getInstance();
        Date end = calEnd.getTime();
        logger.warn("GPPS Service finished " + end);

    }
}

package com.ge.hc.gpps.tools;

import com.ge.hc.gpps.domain.Study;
import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: 100026806
 * Date: 28.11.12
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public class AccessionNumber {

    private static Logger logger = Logger.getLogger(AccessionNumber.class);

    public static void addAccessionNumber(Study study, String dicomFile) {

        DicomInputStream dis = null;
        try {
            dis = new DicomInputStream(new File(dicomFile));

            DicomObject dicomObject = dis.readDicomObject();
            dicomObject.putString(Tag.AccessionNumber, VR.SH, study.getAccessionNumber());
            String accessionNumber = dicomObject.getString(Tag.AccessionNumber);
            logger.debug("Accession Number: " + accessionNumber);
            dis.close();
            FileOutputStream fos = new FileOutputStream(new File(dicomFile));
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DicomOutputStream dos = new DicomOutputStream(bos);
            dos.writeDicomFile(dicomObject);
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}

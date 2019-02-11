package com.ge.hc.gpps.tools;

import com.ge.hc.gpps.domain.Study;
import org.apache.log4j.Logger;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.util.TagUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.ge.hc.gpps.tools.AccessionNumber.addAccessionNumber;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 6/21/12
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Helpers {


    private static Logger logger = Logger.getLogger(Helpers.class);

    public static void writeFile(byte[] data, String fileName) throws IOException {
        File zipDir = new File("zip");
        if (!zipDir.exists()) {
            logger.warn("create zip directory");
            if (zipDir.mkdir()) logger.debug("zip directory created");
        }
        FileOutputStream out = new FileOutputStream(zipDir.getName() + "/" + fileName);
        out.write(data);
        out.close();
    }

    public static void writeFile(Study study) throws IOException {
        File zipDir = new File("zip");
        if (!zipDir.exists()) {
            logger.warn("create zip directory");
            if (zipDir.mkdir()) logger.debug("zip directory created");
        }
        FileOutputStream out = new FileOutputStream(zipDir.getName() + "/" + study.getStudyInstanceUid() + "_" + study.getId() + ".zip");
        out.write(study.getRppsData());
        out.close();
    }

    static public List<String> unzip(int zipFile) {
        List<String> sopInstanceUids = new ArrayList<String>();
        try {
            String zipDir = "zip";
            File dcmDir = new File("dcm");
            if (!dcmDir.exists()) {
                logger.warn("create dcm directory");
                if (dcmDir.mkdir()) logger.debug("dcm directory created");
            }
            File hiddenDir = new File("hidden");
            if (!hiddenDir.exists()) {
                logger.warn("create hidden directory");
                if (hiddenDir.mkdir()) logger.debug("hidden directory created");
            }

            BufferedOutputStream out = null;
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipDir + "/" + zipFile + ".zip")));
            ZipEntry entry;
            String dicomFile = null;
            int k = 0;
            while ((entry = in.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[1000];
                dicomFile = "dcm/" + zipFile + "_" + k++ + ".dcm";
                logger.debug("unzip file " + dicomFile);
                out = new BufferedOutputStream(new FileOutputStream(dicomFile), 1000);
                while ((count = in.read(data, 0, 1000)) != -1) {
                    out.write(data, 0, count);
                }
                out.flush();
                out.close();
                if (dicomFile != null) {
                    logger.debug("check for hidden files");
                    //String displayMode = getDisplayMode(dicomFile);
                    sopInstanceUids = getDisplayMode(dicomFile);
                    //logger.debug("DisplayMode: hidden");
                    if (sopInstanceUids == null) System.out.println("not possible");


                    // if (displayMode.equalsIgnoreCase("NONDISPLAYLIST"))
                    if (sopInstanceUids.size() != 0) {
                        logger.debug("found " + sopInstanceUids.size() + " hidden images");
                        boolean success = moveFile("hidden", dicomFile);
                        if (!success) {
                            logger.debug("could not move hidden file");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sopInstanceUids;
    }


    static public List<String> unzip(Study study) {
        List<String> sopInstanceUids = new ArrayList<String>();
        try {
            String zipDir = "zip";
            File dcmDir = new File("dcm/" + study.getStudyInstanceUid() + "_" + study.getId());
            if (!dcmDir.exists()) {
                logger.warn("create dcm directory " + dcmDir);
                if (dcmDir.mkdirs()) logger.debug("dcm directory created " + dcmDir);
            }
            /*
            File hiddenDir = new File("hidden/" + study.getStudyInstanceUid() + "_"  + study.getId());
            if (!hiddenDir.exists()) {
                logger.warn("create hidden directory " + hiddenDir);
                if (hiddenDir.mkdirs()) logger.debug("hidden directory created " + hiddenDir);
            }
            */

            BufferedOutputStream out = null;
            //ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipDir + "/" + zipFile + ".zip")));
            ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipDir + "/" + study.getStudyInstanceUid() + "_" + study.getId() + ".zip")));
            ZipEntry entry;
            String dicomFile = null;
            int k = 0;
            while ((entry = in.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[1000];
                dicomFile = "dcm/" + study.getStudyInstanceUid() + "_" + study.getId() + "/" + study.getStudyInstanceUid() + "_" + k++ + ".dcm";
                logger.debug("unzip file " + dicomFile);
                out = new BufferedOutputStream(new FileOutputStream(dicomFile), 1000);
                while ((count = in.read(data, 0, 1000)) != -1) {
                    out.write(data, 0, count);
                }
                out.flush();
                out.close();
                if (dicomFile != null) {
                    logger.debug("check for hidden files");
                    // add accesion Niumber
                    addAccessionNumber(study, dicomFile);
                    //String displayMode = getDisplayMode(dicomFile);
                    sopInstanceUids = getDisplayMode(dicomFile);
                    //logger.debug("DisplayMode: hidden");
                    if (sopInstanceUids == null) System.out.println("sop instance uid is null, not possible !!");


                    // if (displayMode.equalsIgnoreCase("NONDISPLAYLIST"))
                    if (sopInstanceUids.size() != 0) {
                        logger.debug("found " + sopInstanceUids.size() + " hidden images");
                        File hiddenDir = new File("hidden/" + study.getStudyInstanceUid() + "_" + study.getId());
                        if (!hiddenDir.exists()) {
                            logger.warn("create hidden directory " + hiddenDir);
                            if (hiddenDir.mkdirs()) logger.debug("hidden directory created " + hiddenDir);
                        }
                        boolean success = moveFile("hidden/" + study.getStudyInstanceUid() + "_" + study.getId(), dicomFile);
                        if (!success) {
                            logger.debug("could not move hidden file");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("cannnot unzip object for " + study.getStudyInstanceUid());
            writeZipError(study);
            return new ArrayList<String>();
        }
        return sopInstanceUids;
    }


    static public List<String> getDisplayMode(String inFile) {
        String mode = null;
        List<String> sopInstanceUids = new ArrayList<String>();
        try {
            //DicomInputStream dis = new DicomInputStream(new File("dcm/" + inFile + ".dcm"));
            DicomInputStream dis = new DicomInputStream(new File(inFile));
            int displayMode = 7409680; //0x0071,0x1010
            DicomObject dicomObject = dis.readDicomObject();
            DicomElement dicomElement = dicomObject.get(displayMode);
            mode = dicomObject.getString(displayMode);
            logger.debug("Display mode from element 0x0071,0x1010:  " + mode);
            if (mode.equalsIgnoreCase("NONDISPLAYLIST")) {
                // now get tie referenced SOP Instance UID as well
                // we need first the sequence 0x0008,0x1110
                dicomElement = dicomObject.get(528656);
                logger.debug("DicomSequence 0x0008,0x1110" + dicomElement);
                DicomObject dicomObject1 = dicomElement.getDicomObject();
                //now we take 0x0008,0x1155
                String refSOPInstanceUID = dicomObject1.getString(528725);
                logger.debug("Reference SOP Instance UID from 0x0008,0x1155: " + refSOPInstanceUID);

                //now we need 0x0008,0x1115
                DicomElement dicomElementSeries = dicomObject.get(528661);
                //dicomElement = dicomObject.get(528661);
                logger.debug("DicomSequence 0x0008,0x1115" + dicomElement);
                logger.debug("Sequence contains " + dicomElement.countItems() + " items");
                for (int i = 0; i < dicomElementSeries.countItems(); i++) {
                    logger.error("+++++++++++++++++++++++++++++ with is the " + i + " Series iteration " );
                    DicomObject dicomObject2 = dicomElementSeries.getDicomObject(i);
                    // now we need 0x0008,0x1140
                    dicomElement = dicomObject2.get(528704);
                    logger.debug("DicomSequence 0x0008,0x1140: " + dicomElement);
                /* some test code
                listHeader(dicomObject2);
                if (dicomElement.hasDicomObjects()) {
                    for (int j=0; j<dicomElement.countItems();j++) {
                        DicomObject dob = dicomElement.getDicomObject(j);
                        logger.debug(dob.getString(528725));
                    }
                }

                // end of testcode
                */
                    logger.debug("Sequence contains " + dicomElement.countItems() + " items");
                    for (int j = 0; j < dicomElement.countItems(); j++) {
                        logger.debug("this is the " + i + "." +  j + " Iteration");
                        DicomObject dicomObject3 = dicomElement.getDicomObject(j);
                        Iterator<DicomElement> hiddenImages = dicomObject3.datasetIterator();
                        while (hiddenImages.hasNext()) {
                            //System.out.println("next tag ");
                            DicomElement de = hiddenImages.next();
                            //logger.debug("################################### " + de);
                            if (de.tag() == 528725) {
                                logger.debug("found another SOP Instance UID : " + dicomObject3.getString(528725));
                                sopInstanceUids.add(dicomObject3.getString(528725));
                            }
                        }
                        logger.debug("found " + sopInstanceUids.size() + " hidden images");
                    }

                }
            }
            dis.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return sopInstanceUids;
        }
        return sopInstanceUids;
    }


    public static boolean moveFile(String directory, String fileName) {
        File file = new File(fileName);
        File dir = new File(directory);
        System.out.println(fileName);
        System.out.println(directory);
        //System.out.println(file.getName());
        //System.out.println(new File(".").getAbsolutePath());
        //if (file.exists()) System.out.println("wir koennen kopieren");
        //if (dir.exists()) System.out.println("wir koennen  kopieren");
        File output = new File(dir, file.getName());
        logger.debug("destination file :" + output.getAbsolutePath());
        return file.renameTo(output);


    }

    public static void listHeader(DicomObject object) {
        Iterator iter = object.datasetIterator();
        while (iter.hasNext()) {
            DicomElement element = (DicomElement) iter.next();
            int tag = element.tag();
            try {
                String tagName = object.nameOf(tag);
                String tagAddr = TagUtils.toString(tag);
                String tagVR = object.vrOf(tag).toString();
                if (tagVR.equals("SQ")) {
                    System.out.println("called");
                    if (element.hasItems()) {
                        System.out.println(tagAddr + " [" + tagVR + "] " + tagName);
                        listHeader(element.getDicomObject());
                        continue;
                    }
                }
                String tagValue = object.getString(tag);
                System.out.println(tagAddr + " [" + tagVR + "] " + tagName + " [" + tagValue + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeZipError(Study study) {

        BufferedWriter bufferedWriter = null;
        try {
            String strContent = "This example shows how to write string content to a file";
            File myFile = new File("corruptzip.txt");
            // check if file exist, otherwise create the file before writing
            if (!myFile.exists()) {
                myFile.createNewFile();
            }
            Writer writer = new FileWriter(myFile);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.append("Study: "  + study.getStudyInstanceUid() +" zip file coruupt" );
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(bufferedWriter != null) bufferedWriter.close();
            } catch(Exception ex){

            }
        }
    }

}


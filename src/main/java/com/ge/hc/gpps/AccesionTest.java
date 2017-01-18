package com.ge.hc.gpps;

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
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class AccesionTest {

    public static void main(String[] args) {
        System.out.println("hallo welt");
        String inFile  = "c:/tmp/1.2.840.113619.2.94.1232351413172.02253129365.18335.14657237_0.dcm";

        try {
            DicomInputStream dis = new DicomInputStream(new File(inFile));
            int accNbr = 524368; //0x0008,0050
            int mod = 524384; //0x0008,0x0060
            DicomObject dicomObject = dis.readDicomObject();
            dicomObject.putString(Tag.AccessionNumber, VR.SH, "ISO_IR 100");
            //dicomObject.putString(accNbr, dicomObject.vrOf(accNbr),"007");
            String modality = dicomObject.getString(mod);
            System.out.println("Modality: " + modality);
            dicomObject.putString(mod, dicomObject.vrOf(mod),"CT");
            modality = dicomObject.getString(mod);
            System.out.println("Modality: " + modality);
            String accessionNumber = dicomObject.getString(Tag.AccessionNumber);
            System.out.println("Accession Number: " + accessionNumber);
            dis.close();
            FileOutputStream fos = new FileOutputStream(new File(inFile));
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            DicomOutputStream dos = new DicomOutputStream(bos);
            dos.writeDicomFile(dicomObject);
            dos.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

package ro.cuzma.xmp.jpeg;

import java.io.IOException;

import org.jempbox.xmp.XMPSchemaBasic;
import org.jempbox.xmp.XMPSchemaDublinCore;
import org.jempbox.xmp.XMPSchemaPhotoshop;

import ro.cuzma.jpeg.JpegPicture;

import com.drew.imaging.jpeg.JpegProcessingException;

public class XMPTest {

    public static void main(String[] args) {
        try {

            JpegPicture picture = new JpegPicture("E:\\2.jpg");
            XMPManager xm = new XMPManager(picture);
            XMPSchemaBasic tmp = xm.getXmpXML().getBasicSchema();
            if (tmp == null) {
                tmp = xm.getXmpXML().addBasicSchema();
            }
            tmp.setRating(5);
            tmp.setRating(4);

            XMPSchemaDublinCore dc = xm.getXmpXML().getDublinCoreSchema();
            if (dc == null) {
                dc = xm.getXmpXML().addDublinCoreSchema();
            }
            // String subj = null;
            // int size = dc.getSubjects().size();
            // System.out.println(""+dc.getSubjects().size());
            dc.removeSubjectTag();
            dc.addSubject("bbb");
            dc.addSubject("ccc1");
            dc.setTitle("Larry1");
            XMPSchemaPhotoshop ps = xm.getXmpXML().getPhotoshopSchema();
            if (ps == null) {
                ps = xm.getXmpXML().addPhotoshopSchema();
            }
            ps.setCity("Timisoara");
            ps.setState("Timis");
            ps.setCountry("Romania");
            xm.saveXMPintoAPP1();
            picture.saveFile("e:\\2_xmp3.jpg");
            byte[] aaaa = xm.getXMPasBytes();
            if (aaaa != null) {
                for (int i = 0; i < aaaa.length; i++) {
                    System.out.print((char) aaaa[i]);
                }
            } else {
                System.out.print("No data.");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JpegProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

package ro.cuzma.xmp.jpeg;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentReader;

public class TestRead {

    /**
     * Constructor which executes multiple sample usages, each of which return the same output. This
     * class showcases multiple usages of this metadata class library.
     * 
     * @param fileName
     *            path to a jpeg file upon which to operate
     */
    public TestRead(String fileName) {
    }

    public static void main(String[] args) {
        // new SampleUsage("e:\\1.jpg");
        try {
            JpegPicture aa = new JpegPicture("e:\\1.jpg");
            aa.saveFile("e:\\tstseg.jpg");
            byte aaaa[] = aa.getJpegSegmentData().getSegment(JpegSegmentReader.SEGMENT_APP1, 1);
            for (int i = 0; i < aaaa.length; i++) {
                System.out.print((char) aaaa[i]);
            }
            // aa.getJpegSegmentData().ToFile(new File("e:\\tstseg.jpg"),aa.getJpegSegmentData());
        } catch (JpegProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveAs(String newFile) {

    }

}

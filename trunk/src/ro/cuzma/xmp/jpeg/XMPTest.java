package ro.cuzma.xmp.jpeg;

import java.io.IOException;

import com.drew.imaging.jpeg.JpegProcessingException;

import ro.cuzma.jpeg.JpegPicture;

public class XMPTest {

	public static void main(String[] args) {
		try {

			JpegPicture picture = new JpegPicture("e:\\rez.jpg");

			// xmpTest.readfromAPP1(picture.getXMPdata());
			XMPManager xm = new XMPManager(picture);
			xm.getXmpXML().addBasicSchema().addAdvisory("cucu");
			xm.saveXMPintoPicture();
			picture.saveFile("e:\\rez2.jpg");
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

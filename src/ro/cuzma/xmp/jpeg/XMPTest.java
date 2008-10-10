package ro.cuzma.xmp.jpeg;

import java.io.IOException;

import com.drew.imaging.jpeg.JpegProcessingException;

import ro.cuzma.jpeg.JpegPicture;

public class XMPTest {

	public static void main(String[] args) {
		try {
			
			JpegPicture picture = new JpegPicture("e:\\1.jpg");
			XMPManager xmpTest = new XMPManager();
			xmpTest.readfromAPP1(picture.getXMPdata());
			byte[] aaaa =picture.getXMPdata();
			for (int i=0;i<aaaa.length;i++){
				System.out.print((char)aaaa[i]);
			}
			xmpTest.getXMPasBytes();
			char[] aaa =xmpTest.getXMPasBytes();
			for (int i=0;i<aaa.length;i++){
				System.out.print(aaa[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JpegProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package ro.cuzma.jpeg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentData;
import com.drew.imaging.jpeg.JpegSegmentReader;

public class JpegPicture {

	private File jpegFile;
	private JpegSegmentData segmentData;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public JpegPicture(String fileName) throws JpegProcessingException {
		jpegFile = new File(fileName);
		JpegSegmentReader sgmReader = new JpegSegmentReader(jpegFile);
		segmentData = sgmReader.getSegmentData();
	}

	public JpegSegmentData getJpegSegmentData() {
		return segmentData;
	}

	public void saveFile(String newPicture) throws Exception {
		File newFile = new File(newPicture);
		BufferedOutputStream outStream = null;
		boolean writed = false;
		boolean writeXMP = false;
		try {
			if (newFile.getParentFile() != null) {
				newFile.getParentFile().mkdirs();
			}
			outStream = new BufferedOutputStream(new FileOutputStream(newFile));
			int offset = 0;
			byte[] header = new byte[2];
			header[0] = (byte) 0xFF;
			header[1] = (byte) 0xD8;

			
			outStream.write(header);
			writeSegment(JpegSegmentReader.SEGMENT_APP0, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_DQT, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_DHT, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_SOF0, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP1, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP2, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP3, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP4, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP5, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP6, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP7, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP8, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APP9, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APPA, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APPB, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APPC, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APPD, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APPE, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_APPF, outStream);
			writeSegment(JpegSegmentReader.SEGMENT_SOS, outStream);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void writeSegments() {

	}

	private void writeSegment(byte segmentMarker, BufferedOutputStream outStream)
			throws IOException {

		int segmentsNr = segmentData.getSegmentCount(segmentMarker);
		if ((segmentMarker & 0xFF) == (JpegSegmentReader.SEGMENT_SOS & 0xFF)) {
			outStream.write(0xFF);
			outStream.write(segmentMarker);
			byte[] content = segmentData.getSegment(segmentMarker, 0);
			outStream.write(content);
		} else {
			for (int i = 0; i < segmentsNr; i++) {
				byte[] content = segmentData.getSegment(segmentMarker, i);
				if (content != null) {
					outStream.write(0xFF);
					outStream.write(segmentMarker);
					if (content.length + 2 < 256) {
						outStream.write(new byte[1]);
						outStream.write(new Integer(content.length + 2)
								.byteValue());
					} else {

						outStream.write(new Integer(
								((content.length + 2) / 256)).byteValue());
						outStream.write(new Integer(content.length + 2)
								.byteValue());
					}
					outStream.write(content);
				}
			}
		}
	}
	public byte[] getXMPdata(){
		return segmentData.getSegment(JpegSegmentReader.SEGMENT_APP1, 1);
	}
}

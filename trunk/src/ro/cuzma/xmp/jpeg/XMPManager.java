package ro.cuzma.xmp.jpeg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jempbox.xmp.XMPMetadata;
import org.w3c.dom.Document;

import com.drew.imaging.jpeg.JpegSegmentReader;


public class XMPManager {
	private String encoding = "UTF-8";

	Document xmp = null;

	XMPMetadata xmpMeta = null;
	JpegPicture picture = null;
	public final static int NO_OCCURENCE = -1;
	int seggmentOccurence = XMPManager.NO_OCCURENCE;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public XMPManager(JpegPicture picture) throws IOException {
		this.picture = picture;
		readfromAPP1();
	}

	private void readfromAPP1() throws IOException {
		int nr_of_app1 = picture.getJpegSegmentData().getSegmentCount(
				JpegSegmentReader.SEGMENT_APP1);
		for (int k = 0; k < nr_of_app1; k++) {
			byte[] content = picture.getJpegSegmentData().getSegment(
					JpegSegmentReader.SEGMENT_APP1, k);
			try {
				int lenghtSeg = content.length;
				byte[] magic = "W5M0MpCehiHzreSzNTczkc9d".getBytes();
				int j = 0;
				int i = 0;
				while (lenghtSeg > i && j < magic.length) {
					if (content[i] == magic[j]) {
						j++;
					} else {
						j = 0;
					}
					i++;
				}
				if (j == magic.length) {
					int startByte = i + 3;
					byte[] magicEnd = "<?xpacket".getBytes();
					j = 0;
					while (lenghtSeg > i && j < magicEnd.length) {
						if (content[i] == magicEnd[j]) {
							j++;
						} else {
							j = 0;
						}
						i++;
					}
					if (j == magicEnd.length) {
						int endByte = i - magicEnd.length;
						ByteArrayInputStream bis = new ByteArrayInputStream(
								content, startByte, endByte - startByte);
						DocumentBuilderFactory factory = DocumentBuilderFactory
								.newInstance();
						factory.setNamespaceAware(true);
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document document = builder.parse(bis);
						xmpMeta = new XMPMetadata(document);
						this.xmp = document;

					}

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (xmpMeta != null) {
				seggmentOccurence = k;
				break;
			}
		}
		if (xmpMeta == null) {
			xmpMeta = new XMPMetadata();
			seggmentOccurence = XMPManager.NO_OCCURENCE;
		}

	}

	public void saveXMPintoAPP1() {
		if (seggmentOccurence != XMPManager.NO_OCCURENCE) {
			picture.getJpegSegmentData().removeSegmentOccurrence(
					JpegSegmentReader.SEGMENT_APP1, seggmentOccurence);
		}
		picture.getJpegSegmentData().addSegment(JpegSegmentReader.SEGMENT_APP1,
				getXMPasBytes());

	}

	public XMPMetadata getXmpXML() {
		return xmpMeta;
	}

	public byte[] getXMPasBytes() {
		byte[] rez = null;
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			transformer.setOutputProperty(OutputKeys.METHOD,"xml");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			// initialize StreamResult with File object to save to file
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			Result result = new StreamResult(ba);
			DOMSource source = new DOMSource(this.getXmpXML().getXMPDocument());
			transformer.transform(source, result);
			String strange = "" + (char) 0xEF + (char) 0xBB + (char) 0xBF;
			String rezS = "http://ns.adobe.com/xap/1.0/" + (char) 0x00;
			rezS += "<?xpacket begin=\'" + strange
					+ "\' id=\"W5M0MpCehiHzreSzNTczkc9d\"?>";
			rezS += ba.toString() + "<?xpacket end='w'?>";
			rez = new byte[rezS.length()];
			for (int i = 0; i < rezS.length(); i++)
				rez[i] = (byte) rezS.charAt(i);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return rez;

	}
}

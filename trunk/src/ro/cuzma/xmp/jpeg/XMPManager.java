package ro.cuzma.xmp.jpeg;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

import org.jempbox.xmp.Thumbnail;
import org.jempbox.xmp.XMPMetadata;
import org.jempbox.xmp.XMPSchemaBasic;
import org.jempbox.xmp.XMPSchemaDublinCore;
import org.jempbox.xmp.XMPSchemaPDF;
import org.w3c.dom.Document;

public class XMPManager {
	private String encoding = "UTF-8";

	private static final byte SEGMENT_SOS = (byte) 0xDA;

	private static final byte MARKER_EOI = (byte) 0xD9;

	/** APP0 Jpeg segment identifier -- Jfif data. */
	public static final byte SEGMENT_APP0 = (byte) 0xE0;

	/** APP1 Jpeg segment identifier -- where Exif data is kept. */
	public static final byte SEGMENT_APP1 = (byte) 0xE1;

	/** APP2 Jpeg segment identifier. */
	public static final byte SEGMENT_APP2 = (byte) 0xE2;

	/** APP3 Jpeg segment identifier. */
	public static final byte SEGMENT_APP3 = (byte) 0xE3;

	/** APP4 Jpeg segment identifier. */
	public static final byte SEGMENT_APP4 = (byte) 0xE4;

	/** APP5 Jpeg segment identifier. */
	public static final byte SEGMENT_APP5 = (byte) 0xE5;

	/** APP6 Jpeg segment identifier. */
	public static final byte SEGMENT_APP6 = (byte) 0xE6;

	/** APP7 Jpeg segment identifier. */
	public static final byte SEGMENT_APP7 = (byte) 0xE7;

	/** APP8 Jpeg segment identifier. */
	public static final byte SEGMENT_APP8 = (byte) 0xE8;

	/** APP9 Jpeg segment identifier. */
	public static final byte SEGMENT_APP9 = (byte) 0xE9;

	/** APPA Jpeg segment identifier. */
	public static final byte SEGMENT_APPA = (byte) 0xEA;

	/** APPB Jpeg segment identifier. */
	public static final byte SEGMENT_APPB = (byte) 0xEB;

	/** APPC Jpeg segment identifier. */
	public static final byte SEGMENT_APPC = (byte) 0xEC;

	/** APPD Jpeg segment identifier -- IPTC data in here. */
	public static final byte SEGMENT_APPD = (byte) 0xED;

	/** APPE Jpeg segment identifier. */
	public static final byte SEGMENT_APPE = (byte) 0xEE;

	/** APPF Jpeg segment identifier. */
	public static final byte SEGMENT_APPF = (byte) 0xEF;

	/** Start Of Image segment identifier. */
	public static final byte SEGMENT_SOI = (byte) 0xD8;

	/** Define Quantization TableData segment identifier. */
	public static final byte SEGMENT_DQT = (byte) 0xDB;

	/** Define Huffman TableData segment identifier. */
	public static final byte SEGMENT_DHT = (byte) 0xC4;

	/** Start-of-Frame Zero segment identifier. */
	public static final byte SEGMENT_SOF0 = (byte) 0xC0;

	/** Comment segment identifier */
	public static final byte SEGMENT_COM = (byte) 0xFE;

	private File picture;

	Document xmp = null;

	XMPMetadata xmpMeta = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String file = "manu";
		XMPManager xmpM = null;
		try {
			xmpM = new XMPManager();
			// "E:\\IPTC\\IPTCCore-Full\\AgencyPhotographer-Example.jpg"));

			List schemas;
			schemas = xmpM.getXmpXML().getSchemas();
			XMPSchemaDublinCore dc = null;
			XMPSchemaBasic ba = null;
			for (int i = 0; i < schemas.size(); i++) {
				if (schemas.get(i) instanceof XMPSchemaDublinCore) {
					dc = (XMPSchemaDublinCore) schemas.get(i);
				} else if (schemas.get(i) instanceof XMPSchemaBasic) {
					// System.out.println("basic");
					ba = (XMPSchemaBasic) schemas.get(i);
				}
			}
			if (ba == null) {
				xmpM.getXmpXML().addBasicSchema();
			}
			if (dc == null) {
				xmpM.getXmpXML().addDublinCoreSchema();
			}
			schemas = xmpM.getXmpXML().getSchemas();
			for (int i = 0; i < schemas.size(); i++) {
				if (schemas.get(i) instanceof XMPSchemaDublinCore) {
					dc = (XMPSchemaDublinCore) schemas.get(i);
				} else if (schemas.get(i) instanceof XMPSchemaBasic) {
					// System.out.println("basic");
					ba = (XMPSchemaBasic) schemas.get(i);
				}
			}
			ba.setRating(new Integer(2));
			ba.setRating(new Integer(3));
			// ba.setRating(null);
			ba.setLabel("1");
			ba.setNickname("larryN");
			dc.setAbout("larry dc_about");
			dc.setTitle("larry dc_title");
			// ba.setAbout("Larry");
			// ba.setLabel("Larry label");
			dc.removeBagValue("dc:subject", "land");
			dc.addBagValue("dc:subject", "larry keyword");
			dc.addBagValue("dc:subject", "1");
			dc.addBagValue("dc:subject", "2");
			dc.addBagValue("dc:subject", "3");
			dc.addCreator("larry creator");
			System.out.println(dc.getDescription());
			System.out.println(ba.getRating());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			xmpM.saveFile("e:\\" + file + "XMP.jpg");
			// xmpM.writeXMP("aaa");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public XMPManager() throws IOException {
		// this.picture = picture;
		// readXMP();
	}

	/*
	 * private void readXMP() throws IOException { BufferedInputStream inStream =
	 * null; try { inStream = new BufferedInputStream( new
	 * FileInputStream(this.picture)); int offset = 0; byte[] header = new
	 * byte[2]; inStream.read(header, 0, 2); if ((header[0] & 0xFF) == 0xFF &&
	 * (header[1] & 0xFF) == 0xD8) { // write header offset += 2; int k = 0; do { //
	 * next byte is 0xFF byte segmentIdentifier = (byte) (inStream.read() &
	 * 0xFF); if ((segmentIdentifier & 0xFF) != 0xFF) { throw new Exception(
	 * "expected jpeg segment start identifier 0xFF at offset " + offset + ",
	 * not 0x" + Integer .toHexString(segmentIdentifier & 0xFF)); } offset++; //
	 * next byte is <segment-marker> byte thisSegmentMarker = (byte)
	 * (inStream.read() & 0xFF);
	 * 
	 * offset++; byte[] segmentLengthBytes = new byte[2];
	 * inStream.read(segmentLengthBytes, 0, 2); offset += 2; int segmentLength =
	 * ((segmentLengthBytes[0] << 8) & 0xFF00) | (segmentLengthBytes[1] &
	 * 0xFF); // segment length includes size bytes, so subtract two
	 * segmentLength -= 2; if (segmentLength > inStream.available()) { throw new
	 * Exception( "segment size would extend beyond file stream length"); }
	 * byte[] segmentBytes = new byte[segmentLength];
	 * inStream.read(segmentBytes, 0, segmentLength); offset += segmentLength;
	 * if ((thisSegmentMarker & 0xFF) == (XMPTest.SEGMENT_APPD & 0xFF)) { ; }
	 * else { // next 2-bytes are <segment-size>: [high-byte] // [low-byte] if
	 * ((thisSegmentMarker & 0xFF) == (SEGMENT_SOS & 0xFF)) { break; } else if
	 * ((thisSegmentMarker & 0xFF) == (0xE1 & 0xFF)) { int lenghtSeg =
	 * segmentBytes.length; byte[] magic = "W5M0MpCehiHzreSzNTczkc9d"
	 * .getBytes(); int j = 0; int i = 0; while (lenghtSeg > i && j <
	 * magic.length) { if (segmentBytes[i] == magic[j]) { j++; } else { j = 0; }
	 * i++; } if (j == magic.length) { int startByte = i + 4; byte[] magicEnd = "<?xpacket".getBytes();
	 * j = 0; while (lenghtSeg > i && j < magicEnd.length) { //
	 * System.out.println(j); if (segmentBytes[i] == magicEnd[j]) { j++; } else {
	 * j = 0; } i++; } if (j == magicEnd.length) { int endByte = i -
	 * magicEnd.length; // for (int qq = startByte; qq < endByte; // qq++)
	 * System.out.print((char) // segmentBytes[qq]); ByteArrayInputStream bis =
	 * new ByteArrayInputStream( segmentBytes, startByte, endByte - startByte);
	 * DocumentBuilderFactory factory = DocumentBuilderFactory .newInstance();
	 * factory.setNamespaceAware(true); DocumentBuilder builder = factory
	 * .newDocumentBuilder(); Document document = builder.parse(bis); xmpMeta =
	 * new XMPMetadata(document); this.xmp = document; break; } } } else { ; } } //
	 * didn't find the one we're looking for, loop through to // the next
	 * segment k++; } while (true); } } catch (FileNotFoundException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { try { if (inStream != null)
	 * inStream.close(); } catch (IOException e) { e.printStackTrace(); } } if
	 * (xmpMeta == null) { xmpMeta = new XMPMetadata(); }
	 *  }
	 */

	public void readfromAPP1(byte[] content) throws IOException {

		try {
			int offset = 0;

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
				int startByte = i + 4;
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
					// for (int qq = startByte; qq < endByte;
					// qq++) System.out.print((char)
					// segmentBytes[qq]);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (xmpMeta == null) {
			xmpMeta = new XMPMetadata();
		}

	}

	public XMPMetadata getXmpXML() {
		return xmpMeta;
	}

	public char[] getXMPasBytes() {
		char[] rez = null;
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			// initialize StreamResult with File object to save to file
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			Result result = new StreamResult(ba);
			DOMSource source = new DOMSource(this.getXmpXML().getXMPDocument());
			transformer.transform(source, result);
			String rezS = "http://ns.adobe.com/xap/1.0/ ";
			// rezS += "<?xpacket begin=\""+ strange +"\"
			// id=\"W5M0MpCehiHzreSzNTczkc9d\"?>";
			rezS += ba.toString();
			/*rez = new char[4 + rezS.length()];
			rez[0] = (char) 0xFF;
			rez[1] = (char) 0xE1;
			int len = rezS.length() + 2;
			rez[2] = (char) ((len & 0xFF00) >> 8);
			rez[3] = (char) (len & 0x00FF);
			for (int i = 0; i < rezS.length(); i++)
				rez[4 + i] = rezS.charAt(i);*/
			return rez;
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rez;

	}

	public void saveFile(String newPicture) throws Exception {
		File newFile = new File(newPicture);
		BufferedInputStream inStream = null;
		BufferedOutputStream outStream = null;
		boolean writed = false;
		boolean writeXMP = false;
		try {
			if (newFile.getParentFile() != null) {
				newFile.getParentFile().mkdirs();
			}
			inStream = new BufferedInputStream(
					new FileInputStream(this.picture));
			outStream = new BufferedOutputStream(new FileOutputStream(newFile));
			// int rb = inStream.read();
			int offset = 0;
			byte[] header = new byte[2];
			inStream.read(header, 0, 2);
			if ((header[0] & 0xFF) == 0xFF && (header[1] & 0xFF) == 0xD8) {
				// write header
				outStream.write(header);
				offset += 2;
				do {
					// next byte is 0xFF
					byte segmentIdentifier = (byte) (inStream.read() & 0xFF);
					if ((segmentIdentifier & 0xFF) != 0xFF) {
						throw new Exception(
								"expected jpeg segment start identifier 0xFF at offset "
										+ offset
										+ ", not 0x"
										+ Integer
												.toHexString(segmentIdentifier & 0xFF));
					}
					offset++;
					// next byte is <segment-marker>
					byte thisSegmentMarker = (byte) (inStream.read() & 0xFF);
					offset++;
					byte[] segmentLengthBytes = new byte[2];
					inStream.read(segmentLengthBytes, 0, 2);
					offset += 2;
					int segmentLength = ((segmentLengthBytes[0] << 8) & 0xFF00)
							| (segmentLengthBytes[1] & 0xFF);
					// segment length includes size bytes, so subtract two
					segmentLength -= 2;
					if (segmentLength > inStream.available()) {
						throw new Exception(
								"segment size would extend beyond file stream length");
					}
					byte[] segmentBytes = new byte[segmentLength];
					inStream.read(segmentBytes, 0, segmentLength);
					offset += segmentLength;
					if ((thisSegmentMarker & 0xFF) == (XMPManager.SEGMENT_APPD & 0xFF)) {
						outStream.write(segmentIdentifier);
						outStream.write(thisSegmentMarker);
						outStream.write(segmentLengthBytes);
						outStream.write(segmentBytes);
					} else {
						// next 2-bytes are <segment-size>: [high-byte]
						// [low-byte]
						if ((thisSegmentMarker & 0xFF) == (SEGMENT_SOS & 0xFF)) {
							if (!writed) {
								char[] xmp = getXMPasBytes();
								if (xmp != null) {
									for (int z = 0; z < xmp.length; z++) {
										outStream.write(xmp[z]);
									}
								}
							}
							outStream.write(segmentIdentifier);
							outStream.write(thisSegmentMarker);
							outStream.write(segmentLengthBytes);
							outStream.write(segmentBytes);
							int rb = inStream.read();
							while (rb != -1) {
								outStream.write(rb);
								rb = inStream.read();
							}
							break;
						} else if ((thisSegmentMarker & 0xFF) == (0xE1 & 0xFF)) {
							int lenghtSeg = segmentBytes.length;
							byte[] magic = "W5M0MpCehiHzreSzNTczkc9d"
									.getBytes();
							int j = 0;
							int i = 0;
							while (lenghtSeg > i && j < magic.length) {
								if (segmentBytes[i] == magic[j]) {
									j++;
								} else {
									j = 0;
								}
								i++;
							}
							if (j == magic.length) {
								int startByte = i + 4;
								byte[] magicEnd = "<?xpacket".getBytes();
								j = 0;
								while (lenghtSeg > i && j < magicEnd.length) {
									// System.out.println(j);
									if (segmentBytes[i] == magicEnd[j]) {
										j++;
									} else {
										j = 0;
									}
									i++;
								}
								if (j == magicEnd.length) {
									// outStream.write(getXMPasBytes());
									writed = true;
									char[] xmp = getXMPasBytes();
									if (xmp != null) {
										for (int z = 0; z < xmp.length; z++) {
											outStream.write(xmp[z]);
										}
									}
								} else {
									outStream.write(segmentIdentifier);
									outStream.write(thisSegmentMarker);
									outStream.write(segmentLengthBytes);
									outStream.write(segmentBytes);
								}
							} else {
								outStream.write(segmentIdentifier);
								outStream.write(thisSegmentMarker);
								outStream.write(segmentLengthBytes);
								outStream.write(segmentBytes);
							}
						} else {
							outStream.write(segmentIdentifier);
							outStream.write(thisSegmentMarker);
							outStream.write(segmentLengthBytes);
							outStream.write(segmentBytes);
						}
					}
				} while (true);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inStream != null)
					inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

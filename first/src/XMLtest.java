import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.internal.txw2.Document;



public class XMLtest {
	
	public Vector Data = new Vector<Paper>();
	public Vector<Paper> extractXML(String fileName) {
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		try {

			String title = null;
			String author = null;
			String year = null;
			String id = null;
			String pdf = null;
			String proceeding = null;
			String pabstract = null;
			Vector citeList = null;
			String citePdf = null;
			String citeTitle = null;
			String citeProceeding = null;
			String citeYear = null;
			String citeAbstract = null;
			String citeAuthor = null;


			DocumentBuilder dombuild = domfac.newDocumentBuilder();
			org.w3c.dom.Document doc = dombuild.parse(new File(fileName));
			Element root = doc.getDocumentElement();
			NodeList nl = root.getElementsByTagName("result");
			
			for (int i = 0; i < nl.getLength(); i++) {
				Node nn = nl.item(i);
				//id = nn.getAttributes().item(0).getNodeValue();
				id = String.valueOf(i);
				if (nn.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) nn;
					NodeList nnl = el.getElementsByTagName("title");
					Element ell = null;
					ell = (Element) nnl.item(0);
					if (ell != null) {
						title = ell.getChildNodes().item(0).getNodeValue();
					} else {
						title = null;
					}

					nnl = el.getElementsByTagName("pdf");
					ell = (Element) nnl.item(0);
					if (ell != null) {
						pdf = ell.getChildNodes().item(0).getNodeValue();
					} else {
						pdf = null;
					}

					nnl = el.getElementsByTagName("author");
					ell = (Element) nnl.item(0);
					if (ell != null) {
						author = ell.getChildNodes().item(0).getNodeValue();
					} else {
						author = null;
					}

					nnl = el.getElementsByTagName("proceeding");

					ell = (Element) nnl.item(0);
					if (ell != null)
						proceeding = ell.getChildNodes().item(0).getNodeValue();

					else {
						proceeding = null;
					}
					nnl = el.getElementsByTagName("year");
					ell = (Element) nnl.item(0);
					if (ell != null) {
						year = ell.getChildNodes().item(0).getNodeValue();
					} else {
						year = null;
					}
					nnl = el.getElementsByTagName("abstract");
						ell = (Element) nnl.item(0);
						if (ell != null) {
						pabstract = ell.getChildNodes().item(0).getNodeValue();
					} else {
						pabstract = null;
					}
					nnl = el.getElementsByTagName("citation");
					// get the citation information
					citeList = new Vector<Paper>();
					NodeList citennl = null;
					for (int j = 0; j < nnl.getLength(); j++) {
					    citePdf = null;
					    citeTitle = null;
						citeProceeding = null;
						citeYear = null;
						citeAbstract = null;
						citeAuthor = null;
						
						Element citeElement = (Element)nnl.item(j);
						citennl = citeElement.getElementsByTagName("cite_title");
						ell = (Element)citennl.item(0);
						if(ell!=null){
							citeTitle = ell.getChildNodes().item(0).getNodeValue();
						}
						
						citennl = citeElement.getElementsByTagName("cite_pdf");
						ell = (Element)citennl.item(0);
						if(ell!=null){
							citePdf = ell.getChildNodes().item(0).getNodeValue();
						}
						
						citennl = citeElement.getElementsByTagName("cite_author");
						ell = (Element)citennl.item(0);
						if(ell!=null){
							citeAuthor = ell.getChildNodes().item(0).getNodeValue();
						}
						
						citennl = citeElement.getElementsByTagName("cite_proceeding");
						ell = (Element)citennl.item(0);
						if(ell!=null){
							citeProceeding = ell.getChildNodes().item(0).getNodeValue();
						}
						
						citennl = citeElement.getElementsByTagName("cite_year");
						ell = (Element)citennl.item(0);
						if(ell!=null){
							citeYear = ell.getChildNodes().item(0).getNodeValue();
						}
						
						citennl = citeElement.getElementsByTagName("cite_abstract");
						ell = (Element)citennl.item(0);
						if(ell!=null){
							citeAbstract = ell.getChildNodes().item(0).getNodeValue();
						}
						//System.out.println(j);
						citeList.add(new Paper(String.valueOf(j),citeAbstract,citeTitle,citeProceeding,citeYear,citeAuthor,null));
					}
					Data.add(new Paper(String.valueOf(i),pabstract,title,proceeding,year,author,citeList));
				}
			}
			//System.out.println(((Paper)Data.get(0)).getAuthors());
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Data;
	}

	public static void main(String[] args) {
		XMLtest sl = new XMLtest();
		Vector<Paper> result = sl.extractXML("test.xml");
		System.out.println(((Paper)result.get(0)).getAuthors());
	}
}


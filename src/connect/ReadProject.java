package connect;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ReadProject {

    public static String read_() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("webapps/Project.xml"));
            doc.getDocumentElement().normalize();
            NodeList listOfRow = doc.getElementsByTagName("row");
            Node firstNode = listOfRow.item(0);

            if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
                Element firstElement = (Element) firstNode;
                NodeList DriverList = firstElement.getElementsByTagName("Project");
                Element DriverElement = (Element) DriverList.item(0);
                NodeList textDriverList = DriverElement.getChildNodes();
                return ((Node) textDriverList.item(0)).getNodeValue().trim();
            } else {
                return null;
            }

        } catch (SAXParseException err) {
        } catch (SAXException e) {
        } catch (Throwable t) {
        }

        return null;
    }

    public static String read() {
        return "PHC";
    }
}

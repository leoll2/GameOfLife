import com.thoughtworks.xstream.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class EventoUtilizzoGUI {
    private final String evento;
    private final String IPClient;
    private final String data;
    private static final String PERCORSO_SCHEMA_EVENTO_XSD = "src/schemaEventoGUI.xsd";
    
    public EventoUtilizzoGUI(String etichettaEvento, String IPClient) {
        evento = etichettaEvento;
        this.IPClient = IPClient;
        data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());       
    }
    
    public String serializzaXML() {
        XStream xs = new XStream();
        String s = xs.toXML(this);
        return s;
    }
    
    public static boolean validaXML(String eventoXML) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = db.parse(new InputSource(new StringReader(eventoXML)));
            Schema s = sf.newSchema(new StreamSource(new File(PERCORSO_SCHEMA_EVENTO_XSD)));
            s.newValidator().validate(new DOMSource(d));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            return false;
        }
        return true;
    }
}

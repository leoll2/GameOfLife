import java.io.*;
import java.nio.file.*;
import com.thoughtworks.xstream.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;


public class ParametriConfigurazione {
    
    private final String PERCORSO_FILE_CONF_XML = "config/configurazioneXML.xml",
            PERCORSO_SCHEMA_CONF_XSD = "config/schemaConfigurazione.xsd";
    public ParametriDimensioni dimensioni;          //(01)
    public ParametroInIntervallo<Double> velocita;
    public ParametriConnettivita connettivita;
    
    public class ParametriDimensioni {
        public ParametroInIntervallo<Integer> larghezza;
        public ParametroInIntervallo<Integer> altezza;
    }
    
    public class ParametriConnettivita {
        public String nomeUtente;
        public String IPClient;
        public String IPServer;
        public int portaServer;
    }
    
    private void deserializzaParametriXML() throws ParametriConfigurazioneInvalidiException {
        ParametriConfigurazione pc;
        String s;
        XStream xs = new XStream();
        xs.registerConverter(new ConvertitoreParametriXStream());   //(02)
        xs.alias("dimensioni", ParametriDimensioni.class);          //(03)
        xs.alias("velocita", ParametroInIntervallo.class);
        xs.alias("connettivita", ParametriConnettivita.class);
        xs.aliasField("default", ParametroInIntervallo.class, "def");//(04)
        xs.aliasField("minima", ParametroInIntervallo.class, "min");
        xs.aliasField("massima", ParametroInIntervallo.class, "max");
        try{
            s = new String(Files.readAllBytes(Paths.get(PERCORSO_FILE_CONF_XML)));
            if (validaParametriXML()) {
                pc = (ParametriConfigurazione)xs.fromXML(s);
                dimensioni = pc.dimensioni;
                velocita = pc.velocita;
                connettivita = pc.connettivita;
            } else {
                throw new ParametriConfigurazioneInvalidiException(
                    "La validazione dei parametri di configurazione ha avuto esito negativo.");
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            throw new ParametriConfigurazioneInvalidiException(
                "Errore durante la lettura del file XML.");
        }
    }
    
    private boolean validaParametriXML() {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Document d = db.parse(new File(PERCORSO_FILE_CONF_XML));
            Schema s = sf.newSchema(new StreamSource(new File(PERCORSO_SCHEMA_CONF_XSD)));
            s.newValidator().validate(new DOMSource(d));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            if (e instanceof SAXException)
                System.err.println("Errore di validazione: " + e.getMessage());
            else
                System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    public ParametriConfigurazione() throws ParametriConfigurazioneInvalidiException {
        deserializzaParametriXML();
    }
}

/*
(01)
I parametri di configurazione sono accessibili come attributi pubblici di
questa classe.

(02)
registerConverter() permette di personalizzare il comportamento di XStream
quando deve serializzare/deserializzare alcune particolari classi.
http://x-stream.github.io/converter-tutorial.html

(03)
alias() è un metodo messo a disposizione da XStream per disaccoppiare il nome
logico di una classe da quello dell'elemento XML.
http://x-stream.github.io/alias-tutorial.html

(04)
aliasField() è l'equivalente di alias() per gli attributi della classe.
http://x-stream.github.io/alias-tutorial.html
*/
import java.io.*;
import java.net.*;

public class LoggerEventiUtilizzoGUI {
    
    private final String IPClient;
    private Socket s;
    private ObjectOutputStream oos;
     
    public LoggerEventiUtilizzoGUI(String IPClient, String IPServer, int portaServer) {
        this.IPClient = IPClient;
        try {
            s = new Socket(IPServer, portaServer);
            oos = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException ioe) {
            System.err.println("Errore durante l'inizializzazione del logger degli eventi\n" +
                    "Dettagli dell'errore:\n" +
                    ioe.getMessage());
            System.exit(1);
        }
    }
    
    private void invia(String eventoSerializzato) throws IOException {
            oos.writeUTF(eventoSerializzato);
            oos.flush();    // (01)
    }
    
    public void registraEvento(String etichettaEvento) {
        EventoUtilizzoGUI eug = new EventoUtilizzoGUI(etichettaEvento, IPClient);
        try {
            invia(eug.serializzaXML());
        } catch (IOException ioe) {
            System.err.println("Non è stato possibile registrare un evento di log\n" +
                    "Dettagli: \n" +
                    ioe.getMessage());
        }
    }
}

/*
(01)
Poichè l'ObjectOutputStream rimane sempre aperto, è possibile che writeUTF()
bufferizzi il messaggio senza inviarlo effettivamente. flush() forza l'invio.
https://docs.oracle.com/javase/8/docs/api/java/io/ObjectOutputStream.html#flush
*/
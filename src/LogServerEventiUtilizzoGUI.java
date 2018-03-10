import java.io.*;
import java.net.*;

public class LogServerEventiUtilizzoGUI {
    
    private final static int PORTA_RICEZIONE = 11997;
    private final static String PERCORSO_FILE_LOG = "./logs/log_eventi_GUI.xml";
    
    private static class ComportamentoThreadLog implements Runnable {
        
        private final Socket sock;
        
        public ComportamentoThreadLog(Socket s) {
            sock = s;
        }
        
        @Override
        public void run() {
            try (
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                FileOutputStream fos = new FileOutputStream(PERCORSO_FILE_LOG, true);
            ) {
                while (true) {
                    String stringaEventoXML = ois.readUTF();
                    if (EventoUtilizzoGUI.validaXML(stringaEventoXML)) {
                        fos.write(stringaEventoXML.getBytes());
                        fos.write('\n');
                    }
                }
            } catch (SocketException se) {
                System.out.println("Client disconnesso");
            } catch (IOException ioe) {
                System.err.println("Errore di I/O.\n" +
                    "Dettagli dell'errore:\n" +
                    ioe.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        try (ServerSocket ss = new ServerSocket(PORTA_RICEZIONE);) {
            System.out.println("Server di log avviato");
            while(true) {
                Socket s = ss.accept();
		System.out.println("Client connesso");
                ComportamentoThreadLog comp = new ComportamentoThreadLog(s);
                Thread t = new Thread(comp);
                t.start();
            }
        } catch (IOException ioe) {
            System.err.println("Errore di I/O.\n" +
                    "Dettagli dell'errore:\n" +
                    ioe.getMessage());
        }
    }
}

import java.util.*;
import java.sql.*;
import java.text.*;
import javafx.collections.*;

public class ArchivioModelliSalvati {
    
    private static Connection connessione;
    private static PreparedStatement statementOttieniCelleDelModello,
            statementOttieniDimensioniDelModello,
            statementPrevieniNomiDuplicati,
            statementArchiviaCella,
            statementArchiviaModello,
            statementOttieniListaModelli;
    
    static {
        try {
            connessione = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/giocodellavita", "root", "");
            statementOttieniCelleDelModello = connessione.prepareStatement(
                    "SELECT numeroCella FROM celle WHERE nomeModello = ?");
            statementOttieniDimensioniDelModello = connessione.prepareStatement(
                    "SELECT larghezza, altezza FROM modelli WHERE nomeModello = ?");
            statementPrevieniNomiDuplicati = connessione.prepareStatement(
                    "SELECT COUNT(*) AS numeroduplicati FROM modelli WHERE nomeModello = ?");
            statementArchiviaCella = connessione.prepareStatement(
                    "INSERT INTO celle VALUES (?, ?)");
            statementArchiviaModello = connessione.prepareStatement(
                    "INSERT INTO modelli VALUES (?, ?, ?, ?, ?)");
            statementOttieniListaModelli = connessione.prepareStatement(
                    "SELECT * FROM modelli");
        } catch (SQLException e) {
            System.err.println("Errore: non e' stato possibile stabilire una " +
                    "connessione con l'archivio dei modelli.\n" +
                    "Dettagli dell'errore: " +
                    e.getMessage());
            System.exit(1);
        }
    }
    
    public static void archiviaModello(Mondo m, String nomeAutore, String nomeModello) 
            throws SQLException, NomeModelloDuplicatoException, NomeModelloNonConsentitoException {
        ResultSet rs;
        if (nomeModello.equals(""))
            throw new NomeModelloNonConsentitoException(
                "La tua configurazione non e' stata archiviata perche' il nome non e' valido.");  
        statementPrevieniNomiDuplicati.setString(1, nomeModello);
        rs = statementPrevieniNomiDuplicati.executeQuery();
        rs.first();
        if(rs.getInt("numeroduplicati") != 0)
            throw new NomeModelloDuplicatoException(
                "La tua configurazione non e' stata archiviata perche' il nome scelto esiste gia'!");
        int lar = m.getLarghezza();
        int alt = m.getAltezza();
        Calendar cal = Calendar.getInstance();  // (01)
        java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis()); // (02)
        statementArchiviaModello.setString(1, nomeModello);
        statementArchiviaModello.setString(2, nomeAutore);
        statementArchiviaModello.setTimestamp(3, timestamp);
        statementArchiviaModello.setInt(4, lar);
        statementArchiviaModello.setInt(5, alt);
        statementArchiviaModello.executeUpdate();            
        for (int i = 0; i < alt; i++)
            for (int j = 0; j < lar; j++) {
                if (m.getVitaCella(i, j)) {
                    statementArchiviaCella.setString(1, nomeModello);
                    statementArchiviaCella.setInt(2, i * lar + j);
                    statementArchiviaCella.executeUpdate();
                }
            }
    }
    
    public static Mondo recuperaModello(String nomeModello) throws SQLException {
        Mondo m;
        int larghezza, altezza;
        ResultSet rs;
        
        statementOttieniDimensioniDelModello.setString(1, nomeModello);
        rs = statementOttieniDimensioniDelModello.executeQuery();
        rs.first();
        larghezza = rs.getInt("larghezza");
        altezza = rs.getInt("altezza");            
        statementOttieniCelleDelModello.setString(1, nomeModello);
        rs = statementOttieniCelleDelModello.executeQuery();
        m = new Mondo(larghezza, altezza);
        while(rs.next()) {
            int numeroCella = rs.getInt("numeroCella");
            m.impostaCella(numeroCella/larghezza, numeroCella%larghezza, true, true);
        }
        return m;
    }
    
    public static ObservableList<ModelloSalvato> ottieniListaModelliSalvati() {
        ObservableList<ModelloSalvato> ol = FXCollections.observableArrayList();
        try {
            ResultSet rs = statementOttieniListaModelli.executeQuery();
            while (rs.next())
                ol.add(new ModelloSalvato(rs.getString("nomeModello"),
                        rs.getString("nomeAutore"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( // (03)
                                rs.getTimestamp("dataSalvataggio"))));
        } catch (SQLException sqle) {
            ol.clear();
        }
        return ol;
    }
}

/*
(01)
Calendar è una classe per manipolare date e ore.
https://docs.oracle.com/javase/8/docs/api/java/util/Calendar.html

(02)
java.sql.Timestamp consente di adattare il formato delle date a quello usato
dalle API JDBC.
https://docs.oracle.com/javase/8/docs/api/java/sql/Timestamp.html

(03)
format permette di convertire un oggetto Date in String.
https://docs.oracle.com/javase/8/docs/api/java/text/DateFormat.html#format
*/
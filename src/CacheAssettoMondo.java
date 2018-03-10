import java.io.*;

public class CacheAssettoMondo {
    
    private static final String PERCORSO_FILE_CACHE = "./cache/mondo.bin";
    
    public static void conservaAssetto(Mondo m) throws IOException {
        FileOutputStream fos = new FileOutputStream(PERCORSO_FILE_CACHE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(m);
    }
    
    public static Mondo recuperaAssetto() throws IOException {
        try (
            FileInputStream fos = new FileInputStream(PERCORSO_FILE_CACHE);
            ObjectInputStream oos = new ObjectInputStream(fos);
        ) {
            return (Mondo)oos.readObject();
        } catch(ClassNotFoundException cnfe) {
            System.err.println("La classe 'Mondo' non è stata trovata!\n" +
                "Dettagli dell'errore: \n" +
                cnfe.getMessage());
            System.exit(1);
        }
        return null;
    }
}

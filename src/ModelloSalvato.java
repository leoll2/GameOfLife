import javafx.beans.property.*;

public class ModelloSalvato {
    
    private final SimpleStringProperty nomeModello;
    private final SimpleStringProperty nomeAutore;
    private final SimpleStringProperty dataSalvataggio;
    
    public ModelloSalvato(String modello, String autore, String data) {
        nomeModello = new SimpleStringProperty(modello);
        nomeAutore = new SimpleStringProperty(autore);
        dataSalvataggio = new SimpleStringProperty(data);        
    }
    
    public String getNomeModello() {
        return nomeModello.get();
    }
    
    public String getNomeAutore() {
        return nomeAutore.get();
    }
    
    public String getDataSalvataggio() {
        return dataSalvataggio.get();
    }
}

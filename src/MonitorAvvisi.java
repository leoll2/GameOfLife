import javafx.scene.control.*;
import javafx.scene.paint.*;

public class MonitorAvvisi extends Label {
    
    public static enum Livello {
        INFORMAZIONE,
        AVVERTIMENTO,
        ERRORE
    };
    
    public void resetta() {
        setText("");
    }
    
    public void notifica(Livello liv, String messaggio) {
        String s = "";
        switch (liv) {
            case INFORMAZIONE:
                setTextFill(Color.DARKGREEN);
                break;
            case AVVERTIMENTO:
                setTextFill(Color.ORANGERED);
                s = s + "Attenzione: ";
                break;
            case ERRORE:
                setTextFill(Color.RED);
                s = s + "Errore: ";
        }
        setText(s + messaggio);
    }
    
    public MonitorAvvisi(String s) {
        super(s);
    }
    
    public MonitorAvvisi() {
        super();
    }
}

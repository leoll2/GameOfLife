import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

public class MondoVisuale extends Group {
    
    private int larghezza,
            altezza;
    private Mondo mondo;
    private final GridPane griglia; //(05)
    private Rectangle[][] celle;    //(06)
    private final Label etichettaContatoreVive,
            etichettaContatoreTick;
    private final TextField contatoreCelleVive,
            contatoreTick;
    private boolean coloriAttivi,
            clickCelleAbilitati,    //(02)
            prontoPerAggiornamento; //(01)
    
    private synchronized void setProntoPerAggiornamento(boolean val) { //(01)
        prontoPerAggiornamento = val;
    }
    
    private synchronized boolean getProntoPerAggiornamento() {  //(01)
        return prontoPerAggiornamento;
    }
    
    private void cliccaCella(final int i, final int j) {
        if (!clickCelleAbilitati)   //(02)
            return;
        mondo.impostaCella(i, j, !mondo.getVitaCella(i, j), true);
        aggiornaColoreCella(i, j);
        Platform.runLater(() -> {   //(03)
            contatoreCelleVive.setText(Integer.toString(mondo.getTotaleCelleVive()));
        });
    }
    
    private void impostaDimensioni(int lar, int alt) {
        altezza = alt;
        larghezza = lar;
        int latoQuadrato = (7*alt > 3*lar) ? (300/alt - 1) : (700/lar - 1); //(04)
        griglia.getChildren().clear();
        celle = new Rectangle[alt][lar];    //(06)
        for (int i = 0; i < altezza; i++) {
            final int iLambda = i;
            for (int j = 0; j < larghezza; j++) {
                final int jLambda = j;
                celle[i][j] = new Rectangle();
                celle[i][j].setOnMouseClicked(ev -> cliccaCella(iLambda, jLambda));
                celle[i][j].setWidth(latoQuadrato);
                celle[i][j].setHeight(latoQuadrato);
                celle[i][j].setFill(Color.WHITE);
                celle[i][j].setStroke(Color.web("#98a07d"));
                celle[i][j].setStyle("-fx-stroke-width: 1px;");
                GridPane.setConstraints(celle[i][j], j, i);
                griglia.getChildren().add(celle[i][j]);
            }
        }
    }
    
    private void aggiornaColoreCella(int i, int j) {
            if (mondo.getVitaCella(i, j) && mondo.getModificataCella(i, j))
                        celle[i][j].setFill(coloriAttivi ? Color.GREEN : Color.BLACK);
                    else if (mondo.getVitaCella(i, j) && !mondo.getModificataCella(i, j))
                        celle[i][j].setFill(Color.BLACK);
                    else if (!mondo.getVitaCella(i, j) && mondo.getModificataCella(i, j))
                        celle[i][j].setFill(coloriAttivi ? Color.RED : Color.WHITE);
                    else
                        celle[i][j].setFill(Color.WHITE);
    }
    
    private void rendiContatoreNonEditabile(TextField contatore) {
        contatore.setEditable(false);       //(07)
        contatore.setMouseTransparent(true);
        contatore.setFocusTraversable(false);
    }
    
    public void abilitaClickCelle(boolean val) {    //(02)
        clickCelleAbilitati = val;
    }
    
    public void attivaColori(boolean val) {
        coloriAttivi = val;
    }
    
    public void cambiaMondo(Mondo m) {
        mondo = m;
        impostaDimensioni(m.getLarghezza(), m.getAltezza());
    }
    
    public final void aggiorna() {
        if (getProntoPerAggiornamento()) {
            setProntoPerAggiornamento(false);   //(01)
            Platform.runLater(() -> {   //(03)
                for (int i = 0; i < altezza; i++) {
                    for (int j = 0; j < larghezza; j++) {
                        aggiornaColoreCella(i, j);
                    }
                }
                contatoreCelleVive.setText(Integer.toString(mondo.getTotaleCelleVive()));
                contatoreTick.setText(Integer.toString(mondo.getTickTrascorsi()));
                setProntoPerAggiornamento(true);    //(01)
            });
        }
    }
    
    public MondoVisuale(Mondo m) {
        mondo = m;
        coloriAttivi = false;
        clickCelleAbilitati = true;     //(02)
        setProntoPerAggiornamento(true);    //(01)
        griglia = new GridPane();   //(05)
        impostaDimensioni(m.getLarghezza(), m.getAltezza());
        griglia.setLayoutX(270); griglia.setLayoutY(70);
        super.getChildren().add(griglia);
        etichettaContatoreVive = new Label("Celle vive: ");
        etichettaContatoreTick = new Label("Tick: ");
        contatoreCelleVive = new TextField(Integer.toString(m.getTotaleCelleVive()));
        contatoreTick = new TextField(Integer.toString(m.getTickTrascorsi()));
        etichettaContatoreVive.setLayoutX(270); etichettaContatoreVive.setLayoutY(385);
        etichettaContatoreTick.setLayoutX(450); etichettaContatoreTick.setLayoutY(385);
        contatoreCelleVive.setLayoutX(345); contatoreCelleVive.setLayoutY(385);
        contatoreTick.setLayoutX(490); contatoreTick.setLayoutY(385);
        rendiContatoreNonEditabile(contatoreCelleVive);
        rendiContatoreNonEditabile(contatoreTick);
        super.getChildren().addAll(
            etichettaContatoreVive,
            etichettaContatoreTick,
            contatoreCelleVive,
            contatoreTick
        );
        aggiorna();
    }
}

/*
(01)
L'attributo 'prontoPerAggiornamento' serve ad evitare che un aggiornamento
grafico di MondoVisuale inizi prima che sia terminato quello della precedente
iterazione. Senza questo controllo, potrebbero verificarsi problemi di
concorrenza all'interno di JavaFX e gravi malfunzionamenti nell'applicazione.

(02)
L'attributo 'clickCelleAbilitati' consente di differenziare la gestione
di un evento di click su una cella a seconda che l'evoluzione sia in corso o
meno. Nel primo caso non deve succedere niente, nel secondo caso la cella
cliccata deve cambiare stato (viva/morta).

(03)
Il metodo Platform.runLater() comanda al thread principale dell'applicazione
JavaFX di eseguire le azioni specifica non appena quest'ultimo può.
L'obiettivo è evitare di modificare componenti grafici direttamente da thread
esterni, che potrebbe essere fonte di problemi di concorrenza per JavaFX.
https://docs.oracle.com/javase/8/javafx/api/javafx/application/Platform.html#runLater-java.lang.Runnable

(04)
Qui si cerca di cerca di trovare la giusta proporzione per le dimensioni della
griglia di gioco, in modo che le celle siano sufficientemente grandi da essere
visibili ma non abbastanza da sovrapporsi ad altri elementi grafici.

(05)
GridPane è una classe di JavaFX che consente di disporre i suoi figli secondo
una configurazione a matrice di righe e colonne.
In questo caso serve a rappresentare la griglia di gioco.
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html

(06)
La classe Rectangle definisce un rettangolo, personalizzabile nelle dimensioni
e nella posizione.
In questo caso rappresenta una singola cella del mondo.
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/shape/Rectangle.html

(07)
I metodi setEditable(), setMouseTransparent() e setFocusTraversable() servono ad
impedire all'utente di modificare il contenuto di un elemento grafico testuale.
Rispettivamente disabilitano la possibilità di scriverci, ricevere eventi di
click ed essere selezionato tramite TAB.
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TextInputControl.html#setEditable-boolean-
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#setMouseTransparent-boolean-
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#setFocusTraversable-boolean-
*/
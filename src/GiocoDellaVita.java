import java.io.*;
import java.sql.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class GiocoDellaVita extends Application {
    private Button avvia,
            svuota,
            randomizza,
            archivia,
            recupera,
            ridimensiona;
    private Slider velocita;    //(01)
    private Label titolo,
            etichettaLarghezza,
            etichettaAltezza,
            etichettaVelocita,
            etichettaColori,
            etichettaNomeConfigurazione,
            etichettaNomeUtente;            
    private TextField inputLarghezza,
            inputAltezza,
            nomeConfigurazione;
    private MonitorAvvisi monitorAvvisi;
    private CheckBox casellaColori; //(02)
    private ParametriConfigurazione config;
    private Mondo mondo;
    private MondoVisuale mondovisuale;
    private ProspettoModelliSalvati prospetto;
    private TimerEvoluzioneMondo timer;
    private LoggerEventiUtilizzoGUI logger;
    private boolean partitaInCorso;
    
    private void istanziaOggettiLogici() {
        try {
            config = new ParametriConfigurazione();
            mondo = CacheAssettoMondo.recuperaAssetto();
        } catch (ParametriConfigurazioneInvalidiException pcie) {
            System.err.println(pcie.getMessage());
            System.exit(1);
        } catch (IOException ioe) {
            if (!(ioe instanceof FileNotFoundException))
                System.err.println("Errore di lettura dal file di cache.\n" +
                    "Dettagli:\n" +
                    ioe.getMessage());
            mondo = new Mondo(config.dimensioni.larghezza.def, //(03)
                              config.dimensioni.altezza.def);
        }
        logger = new LoggerEventiUtilizzoGUI(config.connettivita.IPClient,
                                            config.connettivita.IPServer,
                                            config.connettivita.portaServer);
    }
    
    private void istanziaOggettiGrafici() {
        avvia = new Button("AVVIA");
        svuota = new Button("SVUOTA");
        randomizza = new Button("RANDOMIZZA");
        archivia = new Button("ARCHIVIA");
        recupera = new Button("RECUPERA");
        ridimensiona = new Button("RIDIMENSIONA");
        velocita = new Slider();    //(01)
        titolo = new Label("Game of Life");
        etichettaLarghezza = new Label("Larghezza");
        etichettaAltezza = new Label("Altezza");
        etichettaVelocita = new Label("Velocita' (fps)");
        etichettaColori = new Label("Evidenzia cambiamenti");
        etichettaNomeConfigurazione = new Label("Nome configurazione");
        etichettaNomeUtente = new Label("Utente: " + config.connettivita.nomeUtente);
        inputLarghezza = new TextField(Integer.toString(mondo.getLarghezza()));
        inputAltezza = new TextField(Integer.toString(mondo.getAltezza()));
        nomeConfigurazione = new TextField("partita_di_" + config.connettivita.nomeUtente);
        casellaColori = new CheckBox();     //(02)
        mondovisuale = new MondoVisuale(mondo);
        prospetto = new ProspettoModelliSalvati();
        prospetto.aggiornaListaModelli(ArchivioModelliSalvati.ottieniListaModelliSalvati());
        monitorAvvisi = new MonitorAvvisi();
    }
    
    private void attivaTimerEvoluzione() {
        timer = new TimerEvoluzioneMondo(mondovisuale, config.velocita.def);
        timer.start();
    }
    
    private void impostaStilePulsanti() {
        avvia.setLayoutX(20); avvia.setLayoutY(60);
        svuota.setLayoutX(110); svuota.setLayoutY(60);
        randomizza.setLayoutX(20); randomizza.setLayoutY(105);
        ridimensiona.setLayoutX(20); ridimensiona.setLayoutY(225);
        archivia.setLayoutX(20); archivia.setLayoutY(430);
        recupera.setLayoutX(120); recupera.setLayoutY(430);
    }
    
    private void impostaStileEtichette() {
        titolo.setLayoutX(270); titolo.setLayoutY(25);
        titolo.getStyleClass().add("titolo");
        etichettaLarghezza.setLayoutX(20); etichettaLarghezza.setLayoutY(160);
        etichettaAltezza.setLayoutX(110); etichettaAltezza.setLayoutY(160);
        etichettaVelocita.setLayoutX(20); etichettaVelocita.setLayoutY(280);
        etichettaColori.setLayoutX(45); etichettaColori.setLayoutY(355);
        etichettaNomeConfigurazione.setLayoutX(20); etichettaNomeConfigurazione.setLayoutY(480);
        etichettaNomeUtente.setLayoutX(20); etichettaNomeUtente.setLayoutY(550);
    }
    
    private void impostaStileInput() {
        inputLarghezza.setLayoutX(20); inputLarghezza.setLayoutY(185);
        inputAltezza.setLayoutX(110); inputAltezza.setLayoutY(185);
        nomeConfigurazione.setLayoutX(20); nomeConfigurazione.setLayoutY(505);
        nomeConfigurazione.getStyleClass().add("input-lungo");
    }
    
    private void impostaStileAltro() {
        velocita.setLayoutX(20); velocita.setLayoutY(310);
        velocita.setMin(1); velocita.setMax(10); velocita.setValue(config.velocita.def);
        velocita.setShowTickLabels(true);
        casellaColori.setLayoutX(20); casellaColori.setLayoutY(355);
        monitorAvvisi.setLayoutX(20); monitorAvvisi.setLayoutY(585);
    }
    
    private void impostaStile(Scene scena) {
        impostaStilePulsanti();
        impostaStileEtichette();
        impostaStileInput();
        impostaStileAltro();
        scena.getStylesheets().add("stile.css");
    }
    
    private void aggiungiOggettiAlGruppo(Group g) {
        g.getChildren().addAll(
                avvia,
                svuota,
                randomizza,
                ridimensiona,
                archivia,
                recupera,
                velocita,
                titolo,
                etichettaLarghezza,
                etichettaAltezza,
                etichettaVelocita,
                etichettaColori,
                etichettaNomeConfigurazione,
                etichettaNomeUtente,
                inputLarghezza,
                inputAltezza,
                nomeConfigurazione,
                casellaColori,
                mondovisuale,
                prospetto,
                monitorAvvisi
        );
    }
    
    private void abilitaComandiDiModifica(boolean val) {
        svuota.setDisable(!val);
        randomizza.setDisable(!val);
        ridimensiona.setDisable(!val);
        archivia.setDisable(!val);
        recupera.setDisable(!val);
        mondovisuale.abilitaClickCelle(val);
    }
    
    private void premiPulsanteAvvia() {
        monitorAvvisi.resetta();
        avvia.setText((partitaInCorso) ? "AVVIA" : "PAUSA");
        partitaInCorso = !partitaInCorso;
        if (partitaInCorso) {
            logger.registraEvento("Avvio Simulazione");
            abilitaComandiDiModifica(false);
            timer.avvia(mondo);
        } else {
            logger.registraEvento("Pausa Simulazione");
            abilitaComandiDiModifica(true);
            timer.pausa();
        }
    }
    
    private void premiPulsanteSvuota() {
        monitorAvvisi.resetta();
        mondo.resettaCelle();
        mondovisuale.aggiorna();
    }
    
    private void premiPulsanteRandomizza() {
        monitorAvvisi.resetta();
        mondo.randomizzaCelle();
        mondovisuale.aggiorna();
    }
    
    private void premiPulsanteRidimensiona() {
        monitorAvvisi.resetta();
        int nuovaLarghezza, nuovaAltezza;
        String contenuto;
        contenuto = inputLarghezza.getText();
        if (contenuto.isEmpty() || !contenuto.matches("\\d*")) {
            inputLarghezza.setText(Integer.toString(mondo.getLarghezza()));
            inputAltezza.setText(Integer.toString(mondo.getAltezza()));
            return;
        } else
            nuovaLarghezza = Integer.parseInt(contenuto);
        contenuto = inputAltezza.getText();
        if (contenuto.isEmpty() || !contenuto.matches("\\d*")) {
            inputLarghezza.setText(Integer.toString(mondo.getLarghezza()));
            inputAltezza.setText(Integer.toString(mondo.getAltezza()));
            return;
        } else
            nuovaAltezza = Integer.parseInt(contenuto);
        nuovaLarghezza = Math.min(Math.max(nuovaLarghezza,config.dimensioni.larghezza.min),
                                  config.dimensioni.larghezza.max);
        nuovaAltezza = Math.min(Math.max(nuovaAltezza, config.dimensioni.altezza.min),
                                config.dimensioni.altezza.max);
        inputLarghezza.setText(Integer.toString(nuovaLarghezza));
        inputAltezza.setText(Integer.toString(nuovaAltezza));
        if ((nuovaLarghezza != mondo.getLarghezza()) || (nuovaAltezza != mondo.getAltezza())) {
            mondo.impostaDimensioni(nuovaLarghezza, nuovaAltezza);
            mondovisuale.cambiaMondo(mondo);
            mondovisuale.aggiorna();
        }            
    }
    
    private void premiPulsanteArchivia() {
        monitorAvvisi.resetta();
        logger.registraEvento("Archivio");
        try {
            ArchivioModelliSalvati.archiviaModello(mondo,
                                                   config.connettivita.nomeUtente,
                                                   nomeConfigurazione.getText());
            prospetto.aggiornaListaModelli(ArchivioModelliSalvati.ottieniListaModelliSalvati());
            monitorAvvisi.notifica(MonitorAvvisi.Livello.INFORMAZIONE,
                    "La tua configurazione e' stata salvata con successo!");
        } catch (NomeModelloDuplicatoException | NomeModelloNonConsentitoException e) {
            monitorAvvisi.notifica(MonitorAvvisi.Livello.AVVERTIMENTO,
                    e.getMessage());
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
            monitorAvvisi.notifica(MonitorAvvisi.Livello.ERRORE,
                    "Non e' stato possibile archiviare la tua configurazione.");
        }
    }
    
    private void premiPulsanteRecupera() {
        monitorAvvisi.resetta();
        logger.registraEvento("Recupero");
        try {
            String nomeModelloSelezionato = prospetto.restituisciModelloSelezionato();
            mondo = ArchivioModelliSalvati.recuperaModello(nomeModelloSelezionato);
            mondovisuale.cambiaMondo(mondo);
            mondovisuale.aggiorna();
            nomeConfigurazione.setText(nomeModelloSelezionato);
            inputLarghezza.setText(Integer.toString(mondo.getLarghezza()));
            inputAltezza.setText(Integer.toString(mondo.getAltezza()));
        } catch (NessunModelloSelezionatoException nmse) {
            monitorAvvisi.notifica(MonitorAvvisi.Livello.AVVERTIMENTO,
                nmse.getMessage());
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
            monitorAvvisi.notifica(MonitorAvvisi.Livello.ERRORE,
                "Non e' stato possibile recuperare il modello selezionato.");
        }
    }
    
    private void aggiungiGestoriComandiGUI() {
        avvia.setOnAction((ActionEvent ev) -> {premiPulsanteAvvia();});
        svuota.setOnAction((ActionEvent ev) -> {premiPulsanteSvuota();});
        randomizza.setOnAction((ActionEvent ev) -> {premiPulsanteRandomizza();});
        ridimensiona.setOnAction((ActionEvent ev) -> {premiPulsanteRidimensiona();});
        archivia.setOnAction((ActionEvent ev) -> {premiPulsanteArchivia();});
        recupera.setOnAction((ActionEvent ev) -> {premiPulsanteRecupera();});
        velocita.setOnMouseReleased((MouseEvent ev) -> {timer.cambiaVelocita(velocita.getValue());});
        casellaColori.setOnAction((ActionEvent ev) -> {mondovisuale.attivaColori(casellaColori.isSelected());});
    }
    
    private void spegniTimerEvoluzione() {
        timer.interrupt();
    }
    
    private void memorizzaStatoInCache() {
        try {
            CacheAssettoMondo.conservaAssetto(mondo);
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            monitorAvvisi.notifica(MonitorAvvisi.Livello.ERRORE,
                "Errore durante il salvataggio in cache.");
        }
    }
    
    @Override
    public void start(Stage stage) {
        stage.setOnCloseRequest((WindowEvent we) -> {
            spegniTimerEvoluzione();
            memorizzaStatoInCache();
            logger.registraEvento("Chiusura App");});
        Group gruppo = new Group();
        partitaInCorso = false;
        istanziaOggettiLogici();
        istanziaOggettiGrafici();
        aggiungiGestoriComandiGUI();
        aggiungiOggettiAlGruppo(gruppo);
        attivaTimerEvoluzione();
        Scene scena = new Scene(gruppo, 700, 625, Color.web("#badb90", 0.7));
        impostaStile(scena);
        stage.setTitle("Gioco della vita");
        stage.setScene(scena);
        stage.show();
        logger.registraEvento("Avvio App");
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

/*
(01)
La classe Slider di JavaFX è un elemento dell'interfaccia che permette di
rappresentare una determinata grandezza in un intervallo di valori. L'utente
può modificare il valore spostando un "pomello" all'interno di una "traccia".
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Slider.html

(02)
La classe CheckBox di JavaFX è un elemento dell'interfaccia che rappresenta una
casella selezionabile dall'utente.
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/CheckBox.html

(03)
Se non è possibile leggere dal file di cache o questo non è presente (ad esempio
quando l'applicazione viene avviata per la prima volta), la griglia di gioco
viene inizializzata con valori di default.
*/
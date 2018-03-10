import java.util.*;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

public class ProspettoModelliSalvati extends TableView<ModelloSalvato> {
    
    private final ObservableList<ModelloSalvato> ol;
    
    public void aggiornaListaModelli(List<ModelloSalvato> modelli) {
        ol.clear();
        ol.addAll(modelli);
    }
    
    public String restituisciModelloSelezionato() throws NessunModelloSelezionatoException{
        if (getSelectionModel().isEmpty())      //(01)
            throw new NessunModelloSelezionatoException(
                "Devi prima selezionare un modello da recuperare!\n" +
                "Puoi farlo selezionando una riga della tabella");
        return getSelectionModel().getSelectedItem().getNomeModello();  //(01)
    }
    
    public ProspettoModelliSalvati() {
        setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        TableColumn colonnaNome = new TableColumn("Modello");
        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nomeModello"));
        TableColumn colonnaAutore = new TableColumn("Autore");
        colonnaAutore.setCellValueFactory(new PropertyValueFactory<>("nomeAutore"));
        TableColumn colonnaData = new TableColumn("Data");
        colonnaData.setCellValueFactory(new PropertyValueFactory<>("dataSalvataggio"));
        colonnaData.setMinWidth(60);
        ol = FXCollections.observableArrayList();
        setItems(ol);
        getColumns().addAll(colonnaNome, colonnaAutore, colonnaData);
        setLayoutX(270);
        setLayoutY(430);
        setMaxHeight(150);
        setMinWidth(300);
    }
}

/*
(01)
Il metodo getSelectionModel() di TableView permette di recuperare le
informazioni associate alla riga selezionata nella tabella.
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableView.html#getSelectionModel--
*/
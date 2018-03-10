public class NessunModelloSelezionatoException extends Exception {  //(01)
    public NessunModelloSelezionatoException() {
        super();
    }
    
    public NessunModelloSelezionatoException(String messaggio) {
        super(messaggio);
    }
}

/*
(01)
Questo tipo di eccezione viene lanciato quando l'utente preme il tasto Recupera
senza aver prima selezionato un modello dalla tabella.
*/
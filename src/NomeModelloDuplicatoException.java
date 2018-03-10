public class NomeModelloDuplicatoException extends Exception {  //(01)
    public NomeModelloDuplicatoException() {
        super();
    }
    
    public NomeModelloDuplicatoException(String messaggio) {
        super(messaggio);
    }
}

/*
(01)
Questo tipo di eccezione viene lanciato quando l'utente prova ad archiviare un
modello con lo stesso nome di uno già presente nel database.
*/
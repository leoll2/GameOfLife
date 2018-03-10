public class NomeModelloNonConsentitoException extends Exception {  //(01)
    public NomeModelloNonConsentitoException() {
        super();
    }
    
    public NomeModelloNonConsentitoException(String messaggio) {
        super(messaggio);
    }
}

/*
(01)
Questo tipo di eccezione viene lanciato quando l'utente prova ad archiviare un
modello assegnandogli un nome non valido (per esempio vuoto).
*/

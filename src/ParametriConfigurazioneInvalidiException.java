public class ParametriConfigurazioneInvalidiException extends Exception { //(01)
    public ParametriConfigurazioneInvalidiException() {
        super();
    }
    
    public ParametriConfigurazioneInvalidiException(String messaggio) {
        super(messaggio);
    }
}

/*
(01)
Questo tipo di eccezione viene lanciato quando si verifica un problema nel
recupero dei parametri di configurazione dal relativo file, per esempio perchè
il file è assente o i dati non rispettano il formato giusto.
*/

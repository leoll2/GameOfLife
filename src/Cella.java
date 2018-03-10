import java.io.*;

public class Cella implements Serializable {
    
    private boolean viva;
    private boolean modificata;
    
    public void impostaVita(boolean nuovaVita, boolean tracciaModifiche) { //(01)
        modificata = tracciaModifiche && (viva != nuovaVita);
        viva = nuovaVita;
    }
    
    public boolean getVita() {
        return viva;
    }
    
    public boolean getModificata() {
        return modificata;
    }
    
    public Cella() {
        viva = modificata = false;
    }
}

/*
(01)
Il flag 'tracciaModifiche' determina se impostare o meno l'attributo
'modificata'. Può essere utile non settarlo quando l'utente modifica manualmente
la cella: in tal caso non avrebbe molto senso colorarla.
*/
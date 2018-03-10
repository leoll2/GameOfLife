import java.io.*;
import java.util.*;

public class Mondo implements Serializable {
    
    private int altezza,
            larghezza,
            tickTrascorsi,
            totaleCelleVive;
    private Cella[][] celle;
    
    private synchronized void resettaContatori() {
        tickTrascorsi = 0;
        totaleCelleVive = 0;
    }
    
    private int contaVicini(int riga, int col) {
        int vicini = 0;
        int[][] celleGiaAggiornate = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}}; // (01)
        int[][] celleNonAggiornate = {{0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] i: celleGiaAggiornate) { // (02)
            vicini += ((getVitaCella(riga + i[0], col + i[1])
                       && !getModificataCella(riga + i[0], col + i[1]))
                    ||(!getVitaCella(riga + i[0], col + i[1])
                       && getModificataCella(riga + i[0], col + i[1])))
                    ? 1 : 0 ;
        }
        for (int[] i: celleNonAggiornate) {
            vicini += (getVitaCella(riga + i[0], col + i[1]) ? 1 : 0);
        }
        return vicini;
    }
    
    public synchronized void impostaCella(int riga, int col, boolean val, boolean ignoraModificata) {
        totaleCelleVive += (celle[riga][col].getVita() ? -1 : 0)
                            + (val ? 1 : 0);
        celle[riga][col].impostaVita(val, !ignoraModificata);
    }
    
    public synchronized final void impostaDimensioni(int lar, int alt) {
        resettaContatori();
        altezza = alt;
        larghezza = lar;
        celle = new Cella[alt][lar];
        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < larghezza; j++)
                celle[i][j] = new Cella();
        }
    }
    
    public synchronized int getAltezza() {
        return altezza;
    }
    
    public synchronized int getLarghezza() {
        return larghezza;
    }
    
    public synchronized boolean getVitaCella(int riga, int col) {
        if ((riga >= 0) && (riga < altezza) && (col >= 0) && (col < larghezza))
            return celle[riga][col].getVita();
        else 
            return false;
    }
    
    public synchronized boolean getModificataCella(int riga, int col) {
        if ((riga >= 0) && (riga < altezza) && (col >= 0) && (col < larghezza))
            return celle[riga][col].getModificata();
        else
            return false;
    }
    
    public synchronized int getTotaleCelleVive() {
        return totaleCelleVive;
    }
    
    public synchronized int getTickTrascorsi() {
        return tickTrascorsi;
    }
    
    public void resettaCelle() {
        for (int i = 0; i < altezza; i++)
            for (int j = 0; j < larghezza; j++)
                impostaCella(i, j, false, true);
        tickTrascorsi = 0;
    }
    
    public void randomizzaCelle() {
        Random r = new Random();
        for (int i = 0; i < altezza; i++)
            for (int j = 0; j < larghezza; j++) {
                impostaCella(i, j, r.nextBoolean(), true);
            }
        tickTrascorsi = 0;
    }
    
    public synchronized void evolviCelle() {
        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < larghezza; j++) {
                boolean vivaAdesso = getVitaCella(i, j);
                int viciniVivi = contaVicini(i, j);
                if (vivaAdesso && (viciniVivi == 2 || viciniVivi == 3)) {
                    impostaCella(i, j, true, false);
                } else if (vivaAdesso) {
                    impostaCella(i, j, false, false);
                } else if (!vivaAdesso && (viciniVivi == 3)) {
                    impostaCella(i, j, true, false);
                } else
                    impostaCella(i, j, false, false);
            }
        }
        tickTrascorsi++;
    }
    
    public Mondo(int lar, int alt) {
        impostaDimensioni(lar, alt);
    }
}

/*
(01) 
Poichè la scansione delle celle avviene sequenzialmente, procedendo dall'alto
verso il basso e da sinistra verso destra, bisogna distinguere tra le celle già
ispezionate (e quindi potenzialmente già aggiornate) e non.
Rispetto alla cella considerata, i vicini già analizzati sono le tre celle in
alto e quella a sinistra.

(02)
Attraverso le informazioni contenute negli attributi 'viva' e 'modificata' è
possibile risalire allo stato immediatamente precedente di una cella già
ispezionata.
*/
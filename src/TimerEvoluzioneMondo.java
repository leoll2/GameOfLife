public class TimerEvoluzioneMondo extends Thread {
    private Mondo mondo;
    private final MondoVisuale mondovisuale;
    private int millisecondiAttesa;
    private boolean partitaInCorso;
    
    private synchronized void attendiPartitaInCorso() throws InterruptedException {
        while (!partitaInCorso)
            wait();
    }
    
    public final void cambiaVelocita(double v) {
        millisecondiAttesa = (int)(1000/v);
    }
    
    public synchronized void avvia(Mondo m) {
        mondo = m;
        partitaInCorso = true;
        notify();
    }
    
    public synchronized void pausa() {
        partitaInCorso = false;
    }
    
    public TimerEvoluzioneMondo(MondoVisuale mv, double v) {
        partitaInCorso = false;
        mondovisuale = mv;
        cambiaVelocita(v);
    }
    
    @Override
    public void run() {
        try {
            while(true) {
                attendiPartitaInCorso();    //(01)
                long istanteInizio = System.currentTimeMillis();
                mondo.evolviCelle();
                mondovisuale.aggiorna();
                long tempoOverhead = System.currentTimeMillis() - istanteInizio;
                long tempoDaAttendere = millisecondiAttesa - tempoOverhead; //(02)
                if (tempoDaAttendere > 0)   //(03)
                    Thread.sleep(tempoDaAttendere);
            }
        } catch (InterruptedException ie) {}    //(04)
    }
}

/*
(01)
Si sospende finchè l'utente non preme il tasto Invia. Se l'utente preme Pausa si
sospende nuovamente.

(02)
Il tempo da attendere prima della prossima iterazione tiene conto dell'overhead
introdotto dall'aggiornamento delle celle e soprattutto della grafica.
Tale overhead dipende dalla macchina e dal carico di lavoro, ma può arrivare
facilmente ad essere dell'ordine della decina di millisecondi.

(03)
Se l'aggiornamento ha impiegato più tempo del dovuto, ovvero ha sforato la
deadline (che è data dall'iterazione successiva), il timer evita di attendere
ulteriormente.

(04)
L'eccezione sembrerebbe non essere gestita, in realtà si tratta di un 
comportamento previsto. Semplicemente, quando viene sollevata una 
InterruptedException (per esempio in fase di chiusura dell'app) il timer esce
dal loop infinito e quindi termina.
*/

# Guardia e Ladro

Si vuole sviluppare un’applicazione per la simulazione di un gioco denominato guardia e ladro. Un utente (giocatore) si identifica tramite il nome e cognome. Si suppone di avere una stanza pavimentata a tasselli quadrati (caselle), dotata di pareti esterne ed interne. Nella stanza sono presenti una guardia e un ladro. Il giocatore deve permettere al ladro di uscire dalla stanza. Sia il ladro che la guardia si muovono di una casella alla volta tra le otto caselle vicine.

Devono essere previsti diversi scenari. Per ogni scenario le strategie con cui si muove la guardia sono (K è la probabilità):

    nel K% dei casi la guardia si muove a caso in una delle otto caselle vicine possibili (parete permettendo);
    nel (100 − K)% dei casi la direzione della guardia viene calcolata usando l’algoritmo di ottimizzazione ant colony.

Inoltre, nella stanza ci sono oggetti di diversi colori a disposizione del ladro:

    verde - la guardia va nella direzione opposta a quella del ladro per 10 secondi.

    giallo - la guardia va in una direzione casuale per 10 secondi.

    rosso - la guardia va nella direzione dell’uscita (ottimizzazione ant colony).

Scrivere un programma per la gestione del gioco che permette di visualizzare, ad ogni inizio e fine partita, la classifica dei risultati migliori ottenuti, da tutti i giocatori, in tutte le partite (minore numero di passi per raggiungere l’uscita).

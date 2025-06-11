# Chat4Me MessagingService
Progetto di esercitazione sulla comunicazione tra API.

Il sistema si articola in tre servizi principali: l’API Gateway, il Servizio di Autenticazione e il Servizio di Messaggistica, ciascuno con un ruolo chiave.

---

## Servizio di messaggistica
Il Servizio di Messaggistica si occupa della gestione delle comunicazioni tra utenti.
Ogni richiesta, come l’invio o la lettura dei messaggi, viene effettuata dopo la verifica del token JWT da parte dell’API Gateway, garantendo così l’autenticità dell’utente.
Il servizio permette quindi agli utenti di consultare la propria cronologia in modo sicuro.

Il servizio espone le seguenti api:
- Lettura dei messaggi associati alla propria utenza
- Scrittura di un nuovo messaggio autenticato* per un’utenza specifica
- Lettura degli utenti disponibili

*Messaggio autenticato: un messaggio di cui il server può garantire che è stato scritto da un utente
specifico.

---

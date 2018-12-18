package server;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/**
 * Crea una connessione tramite un thread
 * @author Leonardo Equestre
 * @author Mark Joseph Quinonez
 * @author Giorgia Grassini
 * 
 */

//creo l'oggetto WorkerRunnable (TRHEAD) con un socket come parametro
public class WorkerRunnable extends Thread {
	protected Socket connessione = null;
	//creo un vettore di utenti con dentro tutti gli account connessi
	static Utente user[] = new Utente[5];
	OutputStreamWriter uscita;
	ListView<String> l;
	public WorkerRunnable(Socket connessione, ListView<String> lista) {
		l=lista;
		this.connessione = connessione;
	}

	String b;
	public void run() {
		try {
			user[0] = new Utente("leonardo", "abcd");
			user[1] = new Utente("mj", "0000");
			user[2] = new Utente("giorgia", "gio");
			user[3] = new Utente("nanni", "tudor");
			user[4] = new Utente("biba", "albania");

			//leggo
			InputStream entrataByte;
			entrataByte = connessione.getInputStream();
			InputStreamReader entrata;
			entrata = new InputStreamReader(entrataByte, "UTF-8");
			BufferedReader bEntrata;
			bEntrata = new BufferedReader(entrata);
			String messaggioEntrata = bEntrata.readLine();
			
			// separo il messaggio in pezzi dall'# e prendo il primo pezzo
			String[] vettoreMessaggi = messaggioEntrata.split("#");
			String pezzoUno = vettoreMessaggi[0];

			b=messaggioEntrata;
			Platform.runLater( ()->{
			       l.getItems().add("arrivato messaggio \"" + b + "\"");
			    });
			
			OutputStream uscitaByte;
			uscitaByte = connessione.getOutputStream();
			uscita = new OutputStreamWriter(uscitaByte, "UTF-8");
			// Confronto il primo pezzo se è una Richista di Login(R)
			if (pezzoUno.equals("R")) {
				String pezzoUtente = vettoreMessaggi[1];
				String pezzoPsw = vettoreMessaggi[2];

				boolean tf = false;
				for (int i = 0; i < user.length; i++) {
					if ((user[i].username).equals(pezzoUtente) && (user[i].psw).equals(pezzoPsw)) {
						tf = true;
					}
				}
				//Se il login è corretto mando il messaggio 
				if (tf == true) {
					uscita.write("L#Si\n");
					uscita.flush();

					while ( (messaggioEntrata = bEntrata.readLine()) != null) {
						b=messaggioEntrata;
						Platform.runLater( ()->{
						       l.getItems().add("messaggio: "+b);
						    });
						vettoreMessaggi = messaggioEntrata.split("#");
						pezzoUno = vettoreMessaggi[0];
						if (pezzoUno.equals("M")) {
							String usernameM = vettoreMessaggi[1];
							String m = vettoreMessaggi[2];
							String dataOraM = vettoreMessaggi[3];
							for (int i = 0; i < Server.pcConnessi.size(); i++) {
								Server.pcConnessi.get(i).uscita.write(pezzoUno + "#" + usernameM + "#" + m + "#" + dataOraM + "\n");
								Server.pcConnessi.get(i).uscita.flush();
							}
							Platform.runLater( ()->{
							       l.getItems().add("messaggio mandato");
							    });
							}
					}
				} else {
					//il login è sbagliato
					uscita.write("L#No\n");
					Platform.runLater( ()->{
					       l.getItems().add("No");
					    });
				}
				uscita.flush();
			}
			Server.pcConnessi.remove(this);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
}
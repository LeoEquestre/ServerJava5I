package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javafx.application.Platform;
import javafx.scene.control.ListView;

/**
 * @author Leonardo Equestre
 * @author Mark Joseph Quinonez
 * @author Giorgia Grassini
 */
public class Server extends Thread{
	
	ListView<String> l;
	
	public static Vector<WorkerRunnable> pcConnessi=new Vector<WorkerRunnable>();
	
	public Server(ListView<String> lista) {
		l=lista;
	}
	String x; // FIXME: trovare strada migliore
	public void run() {
		Platform.runLater( ()->{
	       l.getItems().add("avvio server");
	    });
		
		ServerSocket server;
		try {
			// crea il server sulla porta indicata
			server = new ServerSocket(7813);
			Socket connessione;
			while (true) {
				// aspetta la connessione con il client
				Platform.runLater( ()->{
					l.getItems().add("mi metto in ascolto...");
				    });
				connessione = server.accept();
				x = connessione.getInetAddress().toString();
				Platform.runLater( ()->{
				       l.getItems().add("client connesso " + x);
				    });
				WorkerRunnable ok=new WorkerRunnable(connessione, l);
				pcConnessi.add(ok);
				ok.start();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
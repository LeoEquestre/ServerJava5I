package server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Interfaccia extends Application {
	Button bSpegni=new Button("Spegni");
	Label lIp=new Label();
	ListView<String> lista=new ListView<String>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		GridPane gr=new GridPane();
		gr.add(lIp, 0, 0);
		gr.add(lista, 0, 1);
		gr.add(bSpegni, 0, 2);
		gr.setPadding(new Insets(5, 5, 5, 5));
		gr.setHgap(5); 
		gr.setVgap(5);
		Scene scene=new Scene(gr);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		InetAddress ia = InetAddress.getLocalHost();		
		lIp.setText("IP del Server : "+ia.getHostAddress());
		
		Server s=new Server(lista);
		s.start();
		bSpegni.setOnAction(e->spegniServer());
	}
	private void spegniServer() {
		
	}
}

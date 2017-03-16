package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//gnome-keyring

public class Main extends Application {

	private static Scene scene2;
	private static Scene scene1;

	@Override
	public void start(final Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VistaLog.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		LogController logController = loader.getController();

		//SCENE LOG
		this.scene1 = new Scene(root, 300, 500);
		Button boton1 = logController.buttonA();
		AnchorPane anchor2 = FXMLLoader.load(getClass().getResource("VistaMenu.fxml"));

		this.scene2 = new Scene(anchor2, 1150, 900);

		boton1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					boolean log = logController.acceder();
					if (log) stage.setScene(scene2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		stage.setScene(scene1);
		stage.setResizable(false);
		stage.show();
	}

	public static Scene getScene1(){return scene1;}
	public static Scene getScene2(){return scene2;}

	public static void main(String[] args) {
		launch(args);
	}
}
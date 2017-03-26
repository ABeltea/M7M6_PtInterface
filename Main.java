package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

	private static Scene scene;

	@Override
	public void start(final Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("VistaLog.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		//SCENE LOG
		scene = new Scene(root, 300, 500);
		scene.getStylesheets().add(getClass().getResource("default.css").toExternalForm());

		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public static Scene getScene(){
		return Main.scene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
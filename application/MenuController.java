package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MenuController implements Initializable {
	@FXML Pane paneVista;
	@FXML Button logout;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.carregarIncidencies();

		logout.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Stage stage = (Stage) logout.getScene().getWindow();
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("VistaLog.fxml"));
					AnchorPane root = (AnchorPane) loader.load();
					LogController logController = loader.getController();
					Stage stage = new Stage();
					stage.setScene(Main.getScene1());
					stage.setResizable(false);
					stage.show();

					Node source = (Node) event.getSource();
					Stage stage2 = (Stage) source.getScene().getWindow();
					stage2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {}
			}
		});
	}

	@FXML
	public void carregarAjustes() {
		//////////////////
		//String css = this.getClass().getResource("dark.css").toExternalForm();

		List<String> choices = new ArrayList<>();
		choices.add("Dark");
		choices.add("Classic");
		choices.add("Default");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Default", choices);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Look, a Choice Dialog");
		dialog.setContentText("Choose your letter:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.get().equals("Dark")){
			System.out.println("Your choice: " + result.get());

			Main.getScene2().getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
		} else if (result.get().equals("Classic")) {
			Main.getScene2().getStylesheets().add(getClass().getResource("classic.css").toExternalForm());
		} else {
			Main.getScene2().getStylesheets().add(getClass().getResource("default.css").toExternalForm());
		}
	}

	@FXML
	protected void carregarIncidencies(){
		try {
			BorderPane loader = FXMLLoader.load(getClass().getResource("VistaGestorIncidencies.fxml"));
			carregarVista(loader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	protected void carregarUsuaris(){
		try {
			AnchorPane loader = FXMLLoader.load(getClass().getResource("VistaGestorUsuaris.fxml"));
			carregarVista(loader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void carregarVista(Pane vista) {
		if (vista == null) return;
		if (checkSiVistaEstaCarregada(vista.getId())) return;
		this.paneVista.getChildren().clear();
		this.paneVista.getChildren().add(vista);
		AnchorPane.setTopAnchor(vista,0.0);
		AnchorPane.setBottomAnchor(vista,0.0);
		AnchorPane.setLeftAnchor(vista, 0.0);
		AnchorPane.setRightAnchor(vista, 0.0);
	}

	private boolean checkSiVistaEstaCarregada(String id) {
		if (id == null) return false;
		Iterator<Node> fills = this.paneVista.getChildren().iterator();
		while (fills.hasNext()) {
			Node aux = fills.next();
			if (id.equals(aux.getId())) return true;
		}
		return false;
	}
}

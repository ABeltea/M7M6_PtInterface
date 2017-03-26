package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Usuaris;

public class MenuController implements Initializable {
	@FXML private Pane paneVista;

	@FXML private Button logout;
	@FXML private Button ajustes;
	@FXML private Button fbB;
	@FXML private Button ghB;
	@FXML private Button gmB;

	@FXML private Label idUsuari;
	@FXML private ImageView logo;
	@FXML private ImageView fb;
	@FXML private ImageView gh;
	@FXML private ImageView gm;

	private Usuaris user = LogController.getPerfil();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		//Insertar elementos gráficos al menú
		loadGraphics();

		//Añadir enlaces a los imageview sobre el autor de la app
		fb.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					Desktop.getDesktop().browse(new URI("https://www.facebook.com/Aikrhel"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		gh.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/ABeltea"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		gm.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					Desktop.getDesktop().browse(new URI("https://plus.google.com/u/0/118222956262211331931"));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});

		idUsuari.setText("Conectado como:\n" + user.getCognoms() + ", " + user.getNom());
		this.carregarIncidencies();

		//Acciones al pulsar el botón de ajustes.
		ajustes.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				carregarAjustes();
			}
		});

		//Acciones al pulsar el botón de cerrar sesión.
		logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("VistaLog.fxml"));
					AnchorPane root = (AnchorPane) loader.load();
					LogController logController = loader.getController();
					Stage stage = new Stage();

					stage.setScene(Main.getScene());
					stage.setResizable(false);
					stage.show();

					Node source = (Node) event.getSource();
					Stage stage2 = (Stage) source.getScene().getWindow();
					stage2.close();
				} catch (IOException e) {}
			}
		});
	}

	@FXML
	public void carregarAjustes() {
		List<String> choices = new ArrayList<>();
		choices.add("Dark");
		choices.add("Classic");
		choices.add("Default");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Default", choices);
		dialog.setTitle("Choice Dialog");
		dialog.setHeaderText("Look, a Choice Dialog");
		dialog.setContentText("Choose your letter:");

		Optional<String> result = dialog.showAndWait();
		Scene scene = paneVista.getScene();

		if (result.get().equals("Dark")){
			System.out.println("Your choice: " + result.get());
			Main.getScene().getStylesheets().add(getClass().getResource("dark.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("dark.css").toExternalForm());

		} else if (result.get().equals("Classic")) {
			Main.getScene().getStylesheets().add(getClass().getResource("classic.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("classic.css").toExternalForm());
		} else {
			Main.getScene().getStylesheets().add(getClass().getResource("default.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("default.css").toExternalForm());
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

	protected void loadGraphics(){
		File file = new File("./img/logo.png");
		logo.setImage(new Image(file.toURI().toString()));

		file = new File("./img/adj.png");
		ImageView adj = new ImageView();
		adj.setImage(new Image(file.toURI().toString()));
		ajustes.setGraphic(adj);

		//Sobre el autor
		file = new File("./img/fb.png");
		fb.setImage(new Image(file.toURI().toString()));

		file = new File("./img/gh.png");
		gh.setImage(new Image(file.toURI().toString()));

		file = new File("./img/gplus.png");
		gm.setImage(new Image(file.toURI().toString()));
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

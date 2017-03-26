package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import dao.GestionConexiones;
import dao.GestionDAO;
import dao.UsuarisDAOImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Usuaris;

public class LogController implements Initializable {
	@FXML private AnchorPane paneVista;
	@FXML private PasswordField password;
	@FXML private TextField user;
	@FXML private Button login;
	@FXML private ImageView logo;

	private static Usuaris perfil = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		File file = new File("./img/logo.png");
		logo.setImage(new Image(file.toURI().toString()));

		login.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				try {
					boolean log = acceder();
					if (log){
						FXMLLoader loader = new FXMLLoader(getClass().getResource("VistaMenu.fxml"));
						paneVista = (AnchorPane) loader.load();
						Stage stage = new Stage();

						Scene scene = new Scene(paneVista, 1150, 900);
						scene.getStylesheets().add(getClass().getResource("default.css").toExternalForm());

						stage.setScene(scene);
						stage.setResizable(false);
						stage.show();

						Node source = (Node) event.getSource();
						Stage stage2 = (Stage) source.getScene().getWindow();
						stage2.close();
					} else {
						Notifications.create().title("ACCESO DENEGADO").text("Datos erróneos. Por favor, inténtelo otra vez.").showError();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@FXML
	private boolean acceder() {
		String usercheck = "";
		String pwcheck = "";

		try{
			usercheck = user.getText();
			pwcheck = password.getText();

			UsuarisDAOImpl usuaris = GestionDAO.getUsuarisDAOImpl();
			Usuaris cercarUsuari = usuaris.getUsuaris(usercheck);
			if (cercarUsuari == null) return false;

			String pw = cercarUsuari.getPassword();

			if (!pw.equals(pwcheck)) return false;
			this.setPerfil(cercarUsuari);
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	public static Usuaris getPerfil() {return perfil;}
	public void setPerfil(Usuaris perfil) {this.perfil = perfil;}
}

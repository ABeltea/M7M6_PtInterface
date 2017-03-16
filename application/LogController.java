package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

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

public class LogController implements Initializable {
	//@FXML private AnchorPane paneVista;
	@FXML private PasswordField password;
	@FXML private TextField user;
	@FXML private Button login;
    @FXML private ImageView logo;

	public boolean acceder() throws IOException{
		////////////////// CAMBIAR POR CHECK USER + PW //////////////////
		String usercheck = "";
		String pwcheck = "";

		//////////////////
		if (usercheck.equals(user.getText()) && pwcheck.equals(password.getText())){
			System.out.println("Menu gestionar incidencias");
			return true;
		} else {
			Notifications.create().title("ACCESO DENEGADO").text("Datos erróneos. Por favor, inténtelo otra vez.").showError();
			//Notifications.create().title("ERROR").darkStyle().showInformation();
			return false;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		File file = new File("./img/logo.png");
		logo.setImage(new Image(file.toURI().toString()));
	}

	public Button buttonA(){
		return this.login;
	}
}

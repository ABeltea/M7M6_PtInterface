package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dao.GestionDAO;
import dao.UsuarisDAOImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.Usuaris;

public class UsuarisController implements Initializable  {

	@FXML private Pane paneVista;
	@FXML private AnchorPane usuariPane1;

	@FXML private Button gestor;
	@FXML private Button guardar;

	@FXML private TextField emailUser;
	@FXML private TextField cognomUser;
	@FXML private TextField nomUser;
	@FXML private TextField rol;

	@FXML private PasswordField newPW;
	@FXML private PasswordField newPWConfirm;
	@FXML private PasswordField pwUser;

	private Usuaris user = LogController.getPerfil();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Mostrar todos los datos del perfil en la vista.
		initFormulario();

		//En caso de ser NO ser administrador, ocultar el botón que gestiona usuarios.
		if (user.getPerfil().getCodi() == 2){
			gestor.setVisible(false);
		}

		//Acciones sobre el botón guardar. Solo permite modificar la contraseña.
		guardar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int update = updatePassword();
				if (update == 1){
					Notifications.create().title("PETICIÓN ACEPTADA").text("Datos correctos. Se han hecho los cambios.").showConfirm();
				} else if (update == -2) {
					Notifications.create().title("PETICIÓN DENEGADA").text("Datos erróneos. Verifica las contraseñas.").showError();
				} else {
					Notifications.create().title("PETICIÓN DENEGADA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
				}
			}
		});

		//Botón que lleva a la vista donde se gestionan todos los usuarios de la BBDD.
		gestor.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				carregarGUsuaris();
			}
		});
	}

	//Inicializa los valores de los textfield
	protected void initFormulario(){
		emailUser.setText(user.getCorreu());
		cognomUser.setText(user.getCognoms());
		nomUser.setText(user.getNom());
		rol.setText(user.getPerfil().getDescripcio());
		emailUser.setEditable(false);
		cognomUser.setEditable(false);
		nomUser.setEditable(false);
		rol.setEditable(false);
	}

	//Función para cambiar la contraseña del usuario al que se está conectado
	protected int updatePassword(){
		try{
			if (newPW.getText().equals("") || newPWConfirm.getText().equals("") || pwUser.getText().equals("")) return -2;

			if (!user.getPassword().equals(pwUser.getText())) return -2;
			if (!newPW.getText().equals(newPWConfirm.getText())) return -2;

			UsuarisDAOImpl usuaris = GestionDAO.getUsuarisDAOImpl();

			user.setPassword(newPW.getText());
			usuaris.updateUsuaris(user);

			return 1;
		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("PETICIÓN DENEGADA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}

		return -1;
	}

	//Cargar la vista de gestión de usuarios
	protected void carregarGUsuaris(){
		try {
			AnchorPane loader = FXMLLoader.load(getClass().getResource("VistaGestorUsuarisAdministrar.fxml"));
			carregarVista(loader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/////////////////////////////////// DUPLICADO DE CÓDIGO, CAMBIAR
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

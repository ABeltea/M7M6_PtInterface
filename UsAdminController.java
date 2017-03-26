package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dao.GestionDAO;
import dao.UsuarisDAOImpl;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import model.Perfils;
import model.Usuaris;

public class UsAdminController implements Initializable {

	@FXML private Pane paneVista;
	@FXML private AnchorPane usuariPane;
	@FXML private AnchorPane formulario;
	@FXML private AnchorPane formUpdate;


	@FXML private TableView<Usuaris> tabla;

	@FXML private Button back;
	@FXML private Button eliminar;
	@FXML private Button add;
	@FXML private Button modificar;
	@FXML private Button mostrarAdd;
	@FXML private Button cancelarAdd;
	@FXML private Button update;
	@FXML private Button cancelarUp;

	@FXML private ComboBox<String> permiso;
	@FXML private TextField nomAdd;
	@FXML private TextField emailAdd;
	@FXML private TextField cogAdd;


	@FXML private ComboBox<String> permisoUp;
	@FXML private TextField nomUp;
	@FXML private TextField emailUp;
	@FXML private TextField cogUp;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Inicializar los valores del tableview
		addDataInit();

		//Acciones al pulsar el botón "retroceder"
		back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				retroceder();
			}
		});

		//Acciones al pulsar el botón "+"
		mostrarAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (formUpdate.isVisible()){
					formUpdate.setVisible(false);
				}

				//Inicializar los valores del comboBox
				permiso.getItems().addAll("Administrador", "Sin permisos");
				permiso.setValue("Sin permisos");
				mostrarAdd.setVisible(false);
				formulario.setVisible(true);
			}
		});

		//Acciones al pulsar el botón "cancelar" en el formulario de crear un usuario
		cancelarAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				emptyForm();
				mostrarAdd.setVisible(true);
				formulario.setVisible(false);
			}
		});

		//Acciones al pulsar el botón "añadir" en el formulario de crear un usuario
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int op = addData();
				if (op == -1){
					Notifications.create().title("PETICIÓN DENEGADA").text("El nombre y/o apellido está vacío.").showError();
				}
			}
		});

		//Botón deshabilitado hasta que se seleccione un row del tableview
		eliminar.disableProperty().bind(Bindings.isEmpty(tabla.getSelectionModel().getSelectedItems()));

		//Acciones al pulsar el botón "Eliminar"
		eliminar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.WARNING, "Una vez hecho no se podrá recuperar.", ButtonType.YES, ButtonType.CANCEL);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					deleteData();
				}
			}
		});

		//Botón deshabilitado hasta que se seleccione un row del tableview
		modificar.disableProperty().bind(Bindings.isEmpty(tabla.getSelectionModel().getSelectedItems()));

		//Acciones al pulsar el botón "Modificar"
		modificar.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (formulario.isVisible()){
					mostrarAdd.setVisible(true);
					formulario.setVisible(false);
				}

				formUpdate.setVisible(true);
			}
		});

		//Acciones al seleccionar una row
		tabla.getSelectionModel().selectedItemProperty().addListener(((obs, oldSelection, newSelection) -> {

			ObservableList<Usuaris> usuarios = tabla.getSelectionModel().getSelectedItems();
			Usuaris usuario = usuarios.get(0);

			nomUp.setText(usuario.getNom());
			cogUp.setText(usuario.getCognoms());
			emailUp.setText(usuario.getCorreu());

			//Inicializar los valores del comboBox
			permisoUp.getItems().addAll("Administrador", "Sin permisos");

			if (usuario.getPerfil().getCodi() == 2){
				permisoUp.setValue("Sin permisos");
			} else{
				permisoUp.setValue("Administrador");
			}
		}));

		//Acciones al pulsar el botón "Guardar" en el formulario de modificar un usuario
		update.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateData();
			}
		});

		//Acciones al pulsar el botón "cancelar" en el formulario de modificar un usuario
		cancelarUp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				formUpdate.setVisible(false);
			}
		});
	}

	//Función para cargar la estructura del tableview y los datos de la BBDD.
	@SuppressWarnings("unchecked")
	@FXML
	protected void addDataInit(){
		try {
			UsuarisDAOImpl usuaris = GestionDAO.getUsuarisDAOImpl();
			ObservableList<Usuaris> usuarios = FXCollections.observableArrayList(usuaris.getAllUsuaris());

			TableColumn<Usuaris, String> nombre = new TableColumn<Usuaris, String>("Nombre");
			TableColumn<Usuaris, String> apellidos = new TableColumn<Usuaris, String>("Apellidos");
			TableColumn<Usuaris, String> id = new TableColumn<Usuaris, String>("ID de usuario");
			TableColumn<Usuaris, String> email = new TableColumn<Usuaris, String>("Correo electrónico");
			TableColumn<Usuaris, String> rol = new TableColumn<Usuaris, String>("Tipo de permisos");

			nombre.setCellValueFactory(new PropertyValueFactory<Usuaris, String>("nom"));
			apellidos.setCellValueFactory(new PropertyValueFactory<Usuaris, String>("cognoms"));
			email.setCellValueFactory(new PropertyValueFactory<Usuaris, String>("correu"));
			id.setCellValueFactory(new PropertyValueFactory<Usuaris, String>("idUsuari"));

			rol.setCellValueFactory(new Callback<CellDataFeatures<Usuaris, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<Usuaris, String> p) {
					return new SimpleStringProperty(p.getValue().getPerfil().getDescripcio());
				}
			});

			tabla.setItems(usuarios);
			tabla.getColumns().addAll(id, rol, apellidos, nombre, email);
			tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("ERROR EN LA TABLA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}
	}

	//Función para refrescar los datos del tableview cada vez que se modifica la BBDD.
	@FXML
	protected void refreshData(){
		try {
			UsuarisDAOImpl usuaris = GestionDAO.getUsuarisDAOImpl();
			tabla.getItems().clear();
			tabla.setItems(FXCollections.observableArrayList(usuaris.getAllUsuaris()));
			Notifications.create().title("PETICIÓN ACEPTADA").text("Datos correctos. Se han hecho los cambios.").showConfirm();

			nomUp.setText("");
			cogUp.setText("");
			emailUp.setText("");

		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("ERROR EN LA TABLA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}
	}

	//Función para eliminar un usuario de la BBDD.
	@FXML
	protected void deleteData(){
		try {
			UsuarisDAOImpl usuaris = GestionDAO.getUsuarisDAOImpl();
			ObservableList<Usuaris> usuarios = tabla.getSelectionModel().getSelectedItems();
			Usuaris usuario = usuarios.get(0);
			usuaris.deleteUsuaris(usuario);
			refreshData();
		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("PETICIÓN DENEGADA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}
	}

	//Función para añadir un usuario a la BBDD.
	@FXML
	protected int addData(){
		try{
			UsuarisDAOImpl usuaris = GestionDAO.getUsuarisDAOImpl();

			if (nomAdd.getText().equals("") | cogAdd.getText().equals("")) return -1;
			Perfils perfil = null;
			if (permiso.getValue().equals("Administrador")) {
				perfil = usuaris.getPerfils(1);
			} else {
				perfil = usuaris.getPerfils(2);
			}

			String def = nomAdd.getText().substring(0, 3) + cogAdd.getText().substring(0, 3);
			Usuaris validar = usuaris.getUsuaris(def);
			if (validar == null){
				DateFormat df = new SimpleDateFormat("MMddyyyy");
				Date today = Calendar.getInstance().getTime();
				String reportDate = df.format(today);
				def += reportDate.substring(0, 3);
			}

			Usuaris usuario = new Usuaris(def, def, nomAdd.getText(), cogAdd.getText(), emailAdd.getText(), perfil);
			usuaris.addUsuaris(usuario);

			refreshData();
			emptyForm();
			return 0;
		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("PETICIÓN DENEGADA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}

		return -1;
	}

	//Función para modificar un usuario de la BBDD.
	@FXML
	protected void updateData(){
		try {
			UsuarisDAOImpl usuaris = GestionDAO.getUsuarisDAOImpl();
			Perfils perfil = null;
			if (permisoUp.getValue().equals("Administrador")) {
				perfil = usuaris.getPerfils(1);
			} else {
				perfil = usuaris.getPerfils(2);
			}
			ObservableList<Usuaris> usuarios = tabla.getSelectionModel().getSelectedItems();

			Usuaris usuario = new Usuaris(usuarios.get(0).getIdUsuari(), usuarios.get(0).getPassword(), nomUp.getText(), cogUp.getText(), emailUp.getText(), perfil);
			usuaris.updateUsuaris(usuario);
			refreshData();
		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("PETICIÓN DENEGADA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}
	}

	/*Función para limpiar el formulario de creación de usuario cada vez que se crea uno nuevo.
	 *De esta manera queda limpio en caso de querer agregar un nuevo usuario consecutivamente.
	 */
	@FXML
	protected void emptyForm(){
		emailAdd.clear();
		cogAdd.clear();
		nomAdd.clear();
	}


	// Carga la vista anterior.
	@FXML
	protected void retroceder(){
		try {
			AnchorPane loader = FXMLLoader.load(getClass().getResource("VistaGestorUsuaris.fxml"));
			carregarVista(loader);
		} catch (IOException e) {
			Notifications.create().title("PETICIÓN DENEGADA").text("Ha surgido un error inesperado en la aplicación.").showError();
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

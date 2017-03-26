package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dao.GestionConexiones;
import dao.GestionDAO;
import dao.IncidenciesDAOImpl;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Accions;
import model.Estats;
import model.Incidencies;
import model.Perfils;
import model.Usuaris;

public class IncidenciesController  implements Initializable {

	@FXML private DatePicker dataIncidencia;
	@FXML private TextField creador;
	@FXML private TextField codiIncidencia;
	@FXML private TextField aula;
	@FXML private TextField descripcio;
	@FXML private CheckBox isActivo;

	@FXML private Button buscar;
	@FXML private Button crear;
	@FXML private Button acciones;
	@FXML private Button eliminar;

	@FXML private TableView<Incidencies> tabla;
	private static Incidencies search;

	protected static Incidencies getCodi(){
		return search;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Inicializar los valores del tableview al cargar la vista
		rellenarTabla();

		//Permite buscar en la BBDD según el criterio del usuario
		buscar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}
		});

		//Cargar el formulario que permite crear incidencias en la BBDD.
		crear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("VistaFormulariIncidencies.fxml"));
					Stage form = new Stage();
					form.setTitle("Nueva incidencia");
					form.setResizable(false);

					BorderPane dialogBP = (BorderPane) loader.load();
					Scene dialogScene = new Scene(dialogBP);
					form.setScene(dialogScene);
					form.show();

				} catch (IOException e) {
					Notifications.create().title("ERROR").text("Ha surgido un error inesperado\nal cargar el formulario.").showError();
				}
			}
		});

		//Botón deshabilitado hasta que se seleccione un row del tableview
		acciones.disableProperty().bind(Bindings.isEmpty(tabla.getSelectionModel().getSelectedItems()));

		/*Permite visualizar las acciones asociadas de la incidencia seleccionada
		 * en otro stage.
		 */
		acciones.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					ObservableList<Incidencies> incidencia = tabla.getSelectionModel().getSelectedItems();
					search = incidencia.get(0);

					FXMLLoader loader = new FXMLLoader(getClass().getResource("VistaHistorialAccions.fxml"));
					Stage form = new Stage();
					form.setTitle("Historial acciones de " + search.getCodi());
					form.setResizable(false);

					BorderPane dialogBP = (BorderPane) loader.load();
					Scene dialogScene = new Scene(dialogBP);
					form.setScene(dialogScene);
					form.show();
				} catch (IOException e) {
					Notifications.create().title("ERROR").text("Ha surgido un error inesperado\nal cargar el historial.").showError();
					e.printStackTrace();
				}
			}
		});

		//Botón deshabilitado hasta que se seleccione un row del tableview
		eliminar.disableProperty().bind(Bindings.isEmpty(tabla.getSelectionModel().getSelectedItems()));

		//Permite borrar la incidencia seleccionada en la BBDD.
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
	}

	@SuppressWarnings("unchecked")
	@FXML
	protected void rellenarTabla(){
		try {
			IncidenciesDAOImpl incidencies = GestionDAO.getIncidenciesDAOImpl();
			ObservableList<Incidencies> inci = FXCollections.observableArrayList(incidencies.getAllIncidencies());

			TableColumn<Incidencies, Integer> id = new TableColumn<Incidencies, Integer>("Código");
			TableColumn<Incidencies, String> descrip = new TableColumn<Incidencies, String>("Descripción");
			TableColumn<Incidencies, Date> fecha = new TableColumn<Incidencies, Date>("Fecha");
			TableColumn<Incidencies, String> aula = new TableColumn<Incidencies, String>("Aula");
			TableColumn<Incidencies, String> comment = new TableColumn<Incidencies, String>("Comentario");
			TableColumn<Incidencies, String> creator = new TableColumn<Incidencies, String>("Creador");
			TableColumn<Incidencies, String> state = new TableColumn<Incidencies, String>("Estado");

			id.setCellValueFactory(new PropertyValueFactory<Incidencies, Integer>("codi"));
			aula.setCellValueFactory(new PropertyValueFactory<Incidencies, String>("aula"));
			descrip.setCellValueFactory(new PropertyValueFactory<Incidencies, String>("descripcio"));
			fecha.setCellValueFactory(new PropertyValueFactory<Incidencies, Date>("data"));

			creator.setCellValueFactory(new Callback<CellDataFeatures<Incidencies, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<Incidencies, String> p) {
					return new SimpleStringProperty(p.getValue().getCreador().getIdUsuari());
				}
			});

			comment.setCellValueFactory(new PropertyValueFactory<Incidencies, String>("comentari"));

			state.setCellValueFactory(new Callback<CellDataFeatures<Incidencies, String>, ObservableValue<String>>() {
				public ObservableValue<String> call(CellDataFeatures<Incidencies, String> p) {
					Estats estado = incidencies.getEstats(p.getValue().getEstat().getCodi());
					return new SimpleStringProperty(estado.getDescripcio());
				}
			});

			tabla.setItems(inci);
			tabla.getColumns().addAll(fecha, id, state, comment, creator, aula, descrip);
			tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("ERROR EN LA TABLA").text("Ha surgido un error inesperado.\nNo se han podido cargar los datos.").showError();
		}
	}

	@FXML
	protected void deleteData(){
		try {
			IncidenciesDAOImpl incidencies = GestionDAO.getIncidenciesDAOImpl();
			ObservableList<Incidencies> incidencia = tabla.getSelectionModel().getSelectedItems();
			Incidencies inci = incidencia.get(0);
			incidencies.eliminarIndicencia(inci);

			refreshData();
		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("PETICIÓN DENEGADA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}
	}


	@FXML
	protected void refreshData(){
		try {
			IncidenciesDAOImpl incidencies = GestionDAO.getIncidenciesDAOImpl();
			tabla.getItems().clear();
			tabla.setItems(FXCollections.observableArrayList(incidencies.getAllIncidencies()));
			Notifications.create().title("PETICIÓN ACEPTADA").text("Se han efectuado los cambios.").showConfirm();

		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("ERROR EN LA TABLA").text("Ha surgido un error inesperado.\nNo se han realizado los cambios.").showError();
		}
	}
}

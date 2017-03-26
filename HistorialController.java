package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dao.AccionsDAOImpl;
import dao.GestionDAO;
import dao.IncidenciesDAOImpl;
import dao.UsuarisDAOImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Accions;
import model.Estats;
import model.Incidencies;
import model.Usuaris;

public class HistorialController implements Initializable {

	@FXML private BorderPane paneVista;
	@FXML private TableView <Accions> tabla;

	@FXML private Button crear;
	@FXML private Button close;
	@FXML private Button delete;

	private Incidencies search = IncidenciesController.getCodi();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Acciones al confirmar la creación de incidencia

		crear.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Dialog<Accions> dialog = new Dialog<>();
				dialog.setTitle("Crear acción");
				dialog.setHeaderText("This is a custom dialog. Enter info and \n" +
						"press Okay (or click title bar 'X' for cancel).");
				dialog.setResizable(true);
				Label label1 = new Label("Descripción: ");
				TextField text1 = new TextField();
				GridPane grid = new GridPane();

				grid.add(label1, 1, 1);

				grid.add(text1, 2, 1);
				dialog.getDialogPane().setContent(grid);
				ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
				ButtonType buttonTypeCancel = new ButtonType("Okay", ButtonData.CANCEL_CLOSE);
				dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
			}
		});

		//Acciones al confirmar la creación de incidencia. Simplemente cierra el formulario.
		close.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) close.getScene().getWindow();
				stage.close();
			}
		});
	}

	@FXML
	protected int addAccion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unchecked")
	@FXML
	protected void rellenarTabla(){
		try {
			AccionsDAOImpl accions = GestionDAO.getAccionsDAOImpl();
			ObservableList<Accions> accio = FXCollections.observableArrayList(accions.getAllAccionsByIncidencia(search));

			TableColumn<Accions, Integer> id = new TableColumn<Accions, Integer>("Código");
			TableColumn<Accions, String> descrip = new TableColumn<Accions, String>("Descripción");
			TableColumn<Accions, Date> fecha = new TableColumn<Accions, Date>("Fecha");

			id.setCellValueFactory(new PropertyValueFactory<Accions, Integer>("numAccio"));
			descrip.setCellValueFactory(new PropertyValueFactory<Accions, String>("descripcio"));
			fecha.setCellValueFactory(new PropertyValueFactory<Accions, Date>("data"));

			tabla.setItems(accio);
			tabla.getColumns().addAll(id, fecha, descrip);
			tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		} catch (ClassNotFoundException | SQLException e) {
			Notifications.create().title("ERROR EN LA TABLA").text("Ha surgido un error inesperado.\nNo se han podido cargar los datos.").showError();
		}
	}
}

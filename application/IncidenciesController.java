package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class IncidenciesController  implements Initializable {
	@FXML private DatePicker dataIncidencia;
	@FXML private TextField creador;
	@FXML private TextField codiIncidencia;
	@FXML private TextField aula;
	@FXML private TextField descripcio;
	@FXML private CheckBox isActivo;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}


}

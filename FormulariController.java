package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import dao.GestionDAO;
import dao.IncidenciesDAOImpl;
import dao.UsuarisDAOImpl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Estats;
import model.Incidencies;
import model.Usuaris;

public class FormulariController implements Initializable {

    @FXML private BorderPane paneVista;

    @FXML private Button crear;
    @FXML private Button cancelar;

    @FXML private TextArea desAdd;
    @FXML private TextField comAdd;
    @FXML private TextField aulaAdd;

    private Usuaris user = LogController.getPerfil();
    private IncidenciesController ic = new IncidenciesController();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //Acciones al confirmar la creación de incidencia
        crear.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (aulaAdd.getText().length() > 5){
                    Notifications.create().title("PETICIÓN DENEGADA").text("Formato aula incorrecto.\nMáximo 5 caracteres.").showError();
                    return;
                } else if (desAdd.getText().length() > 50) {
                	Notifications.create().title("PETICIÓN DENEGADA").text("Formato descrición incorrecto.\nMáximo 5 caracteres.").showError();
                    return;
                }

                int op = addIncidencia();

                /*Se le obliga al usuario tener los campos nombre/apellido rellenos
                 *Con tal de generar sin excepciones el id y la password.
                 */
                if (op == -1){
                    Notifications.create().title("PETICIÓN DENEGADA").text("Hay campos vacíos.").showError();
                } else {
                    Stage stage = (Stage) cancelar.getScene().getWindow();
                    stage.close();
                    ic.refreshData();
                }
            }
        });

        //Acciones al confirmar la creación de incidencia. Simplemente cierra el formulario.
        cancelar.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) cancelar.getScene().getWindow();
                stage.close();
            }
        });
    }

    //Función que permite añadir una incidencia nueva a la BBDD
    @FXML
    protected int addIncidencia(){
        try {
            IncidenciesDAOImpl incidencies = GestionDAO.getIncidenciesDAOImpl();
            int codi = (int) (new Date().getTime()/1000);

            if (desAdd.getText().equals("") | aulaAdd.getText().equals("") | comAdd.getText().equals("")) return -1;
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            Estats estat = incidencies.getEstats(1);

            Incidencies inci = new Incidencies(codi, desAdd.getText(), date, aulaAdd.getText(), comAdd.getText(), user, estat);
            incidencies.afegirIncidencia(inci);

            return 0;
        } catch (ClassNotFoundException | SQLException e) {
            Notifications.create().title("ERROR").text("Ha surgido un error inesperado\nal crear la incidencia.").showError();
        }

        return -1;
    }
}

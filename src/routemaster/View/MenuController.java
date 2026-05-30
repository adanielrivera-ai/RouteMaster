
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private SideMenuController sideMenuController;

    @FXML
    private Button btnPedidos;

    @FXML
    private Button btnRepartidores;

    @FXML
    private Button btnRutas;

    @FXML
    private Button btnSimulacion;

    @FXML
    private ComboBox<String> strategyCombo;

    @FXML
    private Label statusLabel;

    @FXML
    void handlePedidos(ActionEvent event) {
        statusLabel.setText("Último evento: Abriendo gestión de pedidos...");
    }

    @FXML
    void handleRepartidores(ActionEvent event) {
        statusLabel.setText("Último evento: Abriendo lista de repartidores...");
    }

    @FXML
    void handleRutas(ActionEvent event) {
        String estrategia = strategyCombo.getValue();
        statusLabel.setText("Último evento: Planificando rutas con estrategia: " + (estrategia != null ? estrategia : "default"));
    }

    @FXML
    void handleSimulacion(ActionEvent event) {
        statusLabel.setText("Último evento: Iniciando simulación...");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strategyCombo.setItems(FXCollections.observableArrayList(
                "Distancia más corta",
                "Menor tiempo estimado",
                "Prioridad de pedidos"
        ));
        strategyCombo.getSelectionModel().select(0);

        sideMenuController.setActiveView("Dashboard");
        sideMenuController.setOnNavigate(view -> {
            statusLabel.setText("Navegando a: " + view);
        });

        Platform.runLater(() -> MainApp.primaryStage.setMaximized(true));
    }
}


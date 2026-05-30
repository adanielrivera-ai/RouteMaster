
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class RepartidorController implements Initializable {

    @FXML
    private SideMenuController sideMenuController;

    @FXML
    private Button btnRefrescar;

    @FXML
    private TableView<Repartidor> tablaRepartidores;

    @FXML
    private TableColumn<Repartidor, Integer> colId;

    @FXML
    private TableColumn<Repartidor, String> colNombre;

    @FXML
    private TableColumn<Repartidor, EstadoRepartidor> colEstado;

    @FXML
    private TableColumn<Repartidor, String> colVehiculo;

    @FXML
    private TableColumn<Repartidor, Integer> colPedidos;

    @FXML
    private Label statusLabel;

    @FXML
    private Label contadorLabel;

    private final ObservableList<Repartidor> repartidores = FXCollections.observableArrayList();
    private final Random random = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colVehiculo.setCellValueFactory(new PropertyValueFactory<>("vehiculo"));
        colPedidos.setCellValueFactory(new PropertyValueFactory<>("pedidosAsignados"));

        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(EstadoRepartidor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item.name());
                setStyle("-fx-text-fill: " + colorEstado(item) + "; -fx-font-weight: bold;");
            }
        });

        repartidores.addAll(
                new Repartidor("Carlos Méndez", EstadoRepartidor.DISPONIBLE, "Moto Honda 150cc"),
                new Repartidor("Ana Ramírez",   EstadoRepartidor.EN_RUTA,    "Furgoneta Renault Kangoo"),
                new Repartidor("Luis Fernández", EstadoRepartidor.DISPONIBLE, "Bicicleta Montaña"),
                new Repartidor("María Gómez",    EstadoRepartidor.OCUPADO,    "Furgoneta Ford Transit"),
                new Repartidor("Pedro Sánchez",  EstadoRepartidor.EN_RUTA,    "Moto Yamaha 250cc"),
                new Repartidor("Sofía Torres",   EstadoRepartidor.DESCONECTADO, "Automóvil Chevrolet Spark")
        );

        tablaRepartidores.setItems(repartidores);
        actualizarContador();
        statusLabel.setText("Flota cargada: " + repartidores.size() + " repartidores");

        sideMenuController.setActiveView("Repartidores");
        sideMenuController.setOnNavigate(view -> {
            statusLabel.setText("Navegando a: " + view);
        });

        Platform.runLater(() -> MainApp.primaryStage.setMaximized(true));
    }

    @FXML
    void handleRefrescar(ActionEvent event) {
        EstadoRepartidor[] estados = EstadoRepartidor.values();
        int cambios = 0;

        for (Repartidor r : repartidores) {
            if (random.nextDouble() < 0.4) {
                EstadoRepartidor nuevo;
                do {
                    nuevo = estados[random.nextInt(estados.length)];
                } while (nuevo == r.getEstado());
                r.setEstado(nuevo);
                cambios++;
            }
        }

        tablaRepartidores.refresh();
        actualizarContador();

        if (cambios > 0) {
            statusLabel.setText("Refrescado: " + cambios + " repartidor(es) cambiaron de estado");
        } else {
            statusLabel.setText("Refrescado: sin cambios en los estados");
        }
    }

    private void actualizarContador() {
        long disponibles = repartidores.stream()
                .filter(r -> r.getEstado() == EstadoRepartidor.DISPONIBLE).count();
        contadorLabel.setText("(" + disponibles + " disponibles / " + repartidores.size() + " total)");
    }

    private String colorEstado(EstadoRepartidor e) {
        return switch (e) {
            case DISPONIBLE   -> "#69f0ae";
            case EN_RUTA      -> "#40c4ff";
            case OCUPADO      -> "#ffb74d";
            case DESCONECTADO -> "#ff5252";
        };
    }
}


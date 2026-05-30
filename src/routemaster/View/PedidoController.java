
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PedidoController implements Initializable {

    @FXML
    private SideMenuController sideMenuController;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtCliente;

    @FXML
    private TextField txtPeso;

    @FXML
    private ComboBox<Prioridad> cmbPrioridad;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TableView<Pedido> tablaPedidos;

    @FXML
    private TableColumn<Pedido, Integer> colId;

    @FXML
    private TableColumn<Pedido, String> colCliente;

    @FXML
    private TableColumn<Pedido, String> colZona;

    @FXML
    private TableColumn<Pedido, Prioridad> colPrioridad;

    @FXML
    private TableColumn<Pedido, String> colEstado;

    @FXML
    private TableColumn<Pedido, Double> colPeso;

    @FXML
    private Label statusLabel;

    private final ObservableList<Pedido> pedidos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbPrioridad.setItems(FXCollections.observableArrayList(Prioridad.values()));
        cmbPrioridad.getSelectionModel().selectFirst();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zona"));
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        tablaPedidos.setItems(pedidos);

        sideMenuController.setActiveView("Pedidos");
        sideMenuController.setOnNavigate(view -> {
            statusLabel.setText("Navegando a: " + view);
        });

        Platform.runLater(() -> MainApp.primaryStage.setMaximized(true));
    }

    @FXML
    void handleRegistrar(ActionEvent event) {
        String direccion = txtDireccion.getText().trim();
        String cliente = txtCliente.getText().trim();
        String pesoText = txtPeso.getText().trim();
        Prioridad prioridad = cmbPrioridad.getValue();

        if (direccion.isEmpty() || cliente.isEmpty() || pesoText.isEmpty()) {
            statusLabel.setText("Todos los campos son obligatorios");
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(pesoText);
            if (peso <= 0) {
                statusLabel.setText("El peso debe ser un valor positivo");
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("El peso debe ser un número válido");
            return;
        }

        Pedido pedido = new Pedido(cliente, direccion, prioridad, peso);
        pedidos.add(pedido);

        txtDireccion.clear();
        txtCliente.clear();
        txtPeso.clear();
        cmbPrioridad.getSelectionModel().selectFirst();

        statusLabel.setText("Pedido #" + pedido.getId() + " registrado correctamente");
    }

    @FXML
    void handleCancelar(ActionEvent event) {
        Pedido seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            statusLabel.setText("Selecciona un pedido de la tabla para cancelar");
            return;
        }

        pedidos.remove(seleccionado);
        statusLabel.setText("Pedido #" + seleccionado.getId() + " cancelado");
    }
}


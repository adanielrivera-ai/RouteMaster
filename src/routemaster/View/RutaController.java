
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class RutaController implements Initializable {

    @FXML
    private SideMenuController sideMenuController;

    @FXML
    private ComboBox<RutaStrategy> cmbEstrategia;

    @FXML
    private Button btnGenerar;

    @FXML
    private TableView<Ruta> tablaRutas;

    @FXML
    private TableColumn<Ruta, Integer> colId;

    @FXML
    private TableColumn<Ruta, String> colRepartidor;

    @FXML
    private TableColumn<Ruta, Integer> colCantidad;

    @FXML
    private TableColumn<Ruta, Double> colKm;

    @FXML
    private TableColumn<Ruta, Integer> colMinutos;

    @FXML
    private TableColumn<Ruta, String> colEstado;

    @FXML
    private VBox detallePanel;

    @FXML
    private Label lblDetalleTitulo;

    @FXML
    private Label lblMetricas;

    @FXML
    private ListView<Pedido> listaPedidos;

    @FXML
    private Label statusLabel;

    private final ObservableList<Ruta> rutas = FXCollections.observableArrayList();
    private final ObservableList<Pedido> pedidosDisponibles = FXCollections.observableArrayList();
    private final List<String> repartidoresNombres = Arrays.asList(
            "Carlos Méndez", "Ana Ramírez", "Luis Fernández",
            "María Gómez", "Pedro Sánchez", "Sofía Torres"
    );

    private final Random random = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbEstrategia.setItems(FXCollections.observableArrayList(RutaStrategy.values()));
        cmbEstrategia.getSelectionModel().selectFirst();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRepartidor.setCellValueFactory(new PropertyValueFactory<>("repartidor"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadPedidos"));
        colKm.setCellValueFactory(new PropertyValueFactory<>("kilometrosTotales"));
        colMinutos.setCellValueFactory(new PropertyValueFactory<>("minutosEstimados"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaRutas.setItems(rutas);

        colEstado.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                setStyle("-fx-text-fill: " + ("Planificada".equals(item) ? "#69f0ae" : "#40c4ff") + "; -fx-font-weight: bold;");
            }
        });

        colKm.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f km", item));
                }
            }
        });

        colMinutos.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " min");
                }
            }
        });

        tablaRutas.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    if (selected != null) {
                        mostrarDetalle(selected);
                    } else {
                        detallePanel.setVisible(false);
                        detallePanel.setManaged(false);
                    }
                });

        precargarPedidos();

        sideMenuController.setActiveView("Rutas");
        sideMenuController.setOnNavigate(view -> {
            statusLabel.setText("Navegando a: " + view);
        });

        Platform.runLater(() -> MainApp.primaryStage.setMaximized(true));
    }

    @FXML
    void handleGenerar(ActionEvent event) {
        RutaStrategy estrategia = cmbEstrategia.getValue();

        if (pedidosDisponibles.isEmpty()) {
            statusLabel.setText("No hay pedidos disponibles para planificar");
            return;
        }

        rutas.clear();

        int repCount = repartidoresNombres.size();
        List<Pedido> pendientes = new ArrayList<>(pedidosDisponibles);
        Collections.shuffle(pendientes, random);

        int pedidosPorRepartidor = Math.max(1, pendientes.size() / repCount);

        for (int i = 0; i < repCount && !pendientes.isEmpty(); i++) {
            int toTake = Math.min(pedidosPorRepartidor, pendientes.size());
            List<Pedido> lote = new ArrayList<>(pendientes.subList(0, toTake));
            pendientes.subList(0, toTake).clear();

            if (estrategia == RutaStrategy.PRIORIDAD_DE_PEDIDOS) {
                lote.sort((a, b) -> b.getPrioridad().compareTo(a.getPrioridad()));
            }

            double kmBase = 5.0 + random.nextDouble() * 20.0;
            double kmPorPedido = lote.size() * (1.5 + random.nextDouble() * 3.0);
            double km = Math.round((kmBase + kmPorPedido) * 10.0) / 10.0;

            int minutos = (int) (km * (2.0 + random.nextDouble() * 2.0));

            String nombre = repartidoresNombres.get(i);
            Ruta ruta = new Ruta(nombre, FXCollections.observableArrayList(lote), km, minutos);
            rutas.add(ruta);
        }

        tablaRutas.getSelectionModel().clearSelection();
        detallePanel.setVisible(false);
        detallePanel.setManaged(false);

        double totalKm = rutas.stream().mapToDouble(Ruta::getKilometrosTotales).sum();
        int totalMin = rutas.stream().mapToInt(Ruta::getMinutosEstimados).sum();
        statusLabel.setText(String.format("Planificadas %d rutas | Total: %.1f km, %d min | Estrategia: %s",
                rutas.size(), totalKm, totalMin, estrategia.name()));
    }

    private void mostrarDetalle(Ruta ruta) {
        lblDetalleTitulo.setText("Pedidos de Ruta #" + ruta.getId() + " — " + ruta.getRepartidor());
        lblMetricas.setText(String.format("%.1f km — %d min", ruta.getKilometrosTotales(), ruta.getMinutosEstimados()));
        listaPedidos.setItems(ruta.getPedidos());

        listaPedidos.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Pedido p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                } else {
                    setText(String.format("#%d  %s  |  %s  |  %s  |  %.1f kg",
                            p.getId(), p.getCliente(), p.getZona(), p.getPrioridad(), p.getPeso()));
                }
            }
        });

        detallePanel.setVisible(true);
        detallePanel.setManaged(true);
    }

    private void precargarPedidos() {
        String[] clientes = {"María López", "Juan Pérez", "Laura García", "Roberto Díaz",
                "Carmen Ruiz", "David Torres", "Ana Martín", "Jorge Sánchez"};
        String[] zonas = {"Centro", "Norte", "Sur", "Este", "Oeste",
                "Centro", "Norte", "Sur"};
        Prioridad[] prioridades = Prioridad.values();

        for (int i = 0; i < clientes.length; i++) {
            Prioridad p = prioridades[random.nextInt(prioridades.length)];
            double peso = 0.5 + random.nextDouble() * 15.0;
            pedidosDisponibles.add(new Pedido(clientes[i], zonas[i], p, Math.round(peso * 10.0) / 10.0));
        }
    }
}


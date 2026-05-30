
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimulacionController implements Initializable {

    @FXML
    private SideMenuController sideMenuController;

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnFinalizar;

    @FXML
    private VBox contenedorProgreso;

    @FXML
    private ListView<EventoLogistico> listaEventos;

    @FXML
    private Label statusLabel;

    private final ObservableList<EventoLogistico> eventos = FXCollections.observableArrayList();
    private Timeline timeline;

    private final String[] repartidores = {
            "Carlos Méndez", "Ana Ramírez", "Luis Fernández",
            "María Gómez", "Pedro Sánchez", "Sofía Torres"
    };
    private final double[] avances = new double[6];
    private final Label[] labelsEstado = new Label[6];
    private final ProgressBar[] barras = new ProgressBar[6];

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Random random = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaEventos.setItems(eventos);

        for (int i = 0; i < repartidores.length; i++) {
            avances[i] = 0.0;

            Label repLabel = new Label(repartidores[i]);
            repLabel.getStyleClass().add("repartidor-label");

            ProgressBar bar = new ProgressBar(0);
            bar.getStyleClass().add("ruta-progress");
            bar.setPrefWidth(300);

            Label estLabel = new Label("DISPONIBLE");
            estLabel.getStyleClass().add("estado-label");
            estLabel.setStyle("-fx-text-fill: #69f0ae;");
            labelsEstado[i] = estLabel;
            barras[i] = bar;

            HBox row = new HBox(repLabel, bar, estLabel);
            row.getStyleClass().add("ruta-progress-row");
            contenedorProgreso.getChildren().add(row);
        }

        sideMenuController.setActiveView("Simulacion");
        sideMenuController.setOnNavigate(view -> {
            statusLabel.setText("Navegando a: " + view);
        });

        Platform.runLater(() -> MainApp.primaryStage.setMaximized(true));
    }

    @FXML
    void handleIniciar(ActionEvent event) {
        btnIniciar.setDisable(true);
        btnFinalizar.setDisable(false);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        statusLabel.setText("Simulación en curso...");
    }

    @FXML
    void handleFinalizar(ActionEvent event) {
        if (timeline != null) {
            timeline.stop();
        }

        for (int i = 0; i < repartidores.length; i++) {
            labelsEstado[i].setStyle("-fx-text-fill: #69f0ae;");
            labelsEstado[i].setText("DISPONIBLE");
        }

        btnIniciar.setDisable(false);
        btnFinalizar.setDisable(true);
        statusLabel.setText("Simulación detenida");
    }

    private void tick() {
        String hora = LocalTime.now().format(HORA);
        boolean algunaEnCurso = false;

        for (int i = 0; i < repartidores.length; i++) {
            if (avances[i] >= 1.0) continue;

            avances[i] = Math.min(1.0, avances[i] + 0.05 + random.nextDouble() * 0.12);
            barras[i].setProgress(avances[i]);

            labelsEstado[i].setStyle("-fx-text-fill: #40c4ff;");
            labelsEstado[i].setText("EN_RUTA");
            algunaEnCurso = true;

            if (random.nextDouble() < 0.35) {
                String tipo;
                String mensaje;
                double r = random.nextDouble();

                if (r < 0.5) {
                    tipo = "ENTREGA";
                    mensaje = "Pedido entregado correctamente";
                } else if (r < 0.8) {
                    tipo = "RETRASO";
                    mensaje = "Retraso por tráfico (" + (5 + random.nextInt(15)) + " min)";
                } else {
                    tipo = "AVERIA";
                    mensaje = "Vehículo con problema menor, continuando ruta";
                }

                eventos.add(0, new EventoLogistico(hora, tipo, repartidores[i], mensaje));
            }

            if (avances[i] >= 1.0 && !labelsEstado[i].getText().equals("DISPONIBLE")) {
                labelsEstado[i].setStyle("-fx-text-fill: #69f0ae;");
                labelsEstado[i].setText("DISPONIBLE");
                eventos.add(0, new EventoLogistico(hora, "FINALIZADO", repartidores[i], "Ruta completada, repartidor disponible"));
            }
        }

        if (listaEventos.getItems().size() > 200) {
            eventos.remove(eventos.size() - 1);
        }

        long completadas = Arrays.stream(avances).filter(a -> a >= 1.0).count();
        statusLabel.setText(String.format("En ejecución — %d/6 rutas completadas  |  Eventos: %d",
                completadas, eventos.size()));

        if (!algunaEnCurso) {
            handleFinalizar(null);
        }
    }
}



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SideMenuController {

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnPedidos;

    @FXML
    private Button btnRepartidores;

    @FXML
    private Button btnRutas;

    @FXML
    private Button btnSimulacion;

    private final Map<String, Button> botones = new HashMap<>();
    private Consumer<String> onNavigate;

    @FXML
    void initialize() {
        botones.put("Dashboard", btnDashboard);
        botones.put("Pedidos", btnPedidos);
        botones.put("Repartidores", btnRepartidores);
        botones.put("Rutas", btnRutas);
        botones.put("Simulacion", btnSimulacion);
    }

    @FXML
    void handleNav(ActionEvent event) {
        Button source = (Button) event.getSource();
        for (var entry : botones.entrySet()) {
            if (entry.getValue() == source) {
                setActiveView(entry.getKey());
                if (onNavigate != null) {
                    onNavigate.accept(entry.getKey());
                }
                return;
            }
        }
    }

    public void setActiveView(String viewName) {
        for (var entry : botones.entrySet()) {
            Button btn = entry.getValue();
            if (entry.getKey().equals(viewName)) {
                btn.getStyleClass().add("nav-btn-active");
            } else {
                btn.getStyleClass().remove("nav-btn-active");
            }
        }
    }

    public void setOnNavigate(Consumer<String> callback) {
        this.onNavigate = callback;
    }
}


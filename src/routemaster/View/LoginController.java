
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Usuario y contraseña son requeridos");
            return;
        }

        if (username.equals("admin") && password.equals("1234")) {
            messageLabel.setStyle("-fx-text-fill: #b388ff;");
            messageLabel.setText("¡Login exitoso!");
        } else {
            messageLabel.setStyle("-fx-text-fill: #ff8a80;");
            messageLabel.setText("Usuario o contraseña incorrectos");
        }
    }
}


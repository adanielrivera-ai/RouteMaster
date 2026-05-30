
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        URL fxmlUrl = getClass().getResource("login.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        AnchorPane root = loader.load();

        Scene scene = new Scene(root, 520, 620);
        URL cssUrl = getClass().getResource("login.css");
        scene.getStylesheets().add(cssUrl.toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Iniciar Sesión");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


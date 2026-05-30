
import javafx.beans.property.SimpleStringProperty;

public class EventoLogistico {

    private final SimpleStringProperty timestamp;
    private final SimpleStringProperty tipo;
    private final SimpleStringProperty repartidor;
    private final SimpleStringProperty mensaje;

    public EventoLogistico(String timestamp, String tipo, String repartidor, String mensaje) {
        this.timestamp = new SimpleStringProperty(timestamp);
        this.tipo = new SimpleStringProperty(tipo);
        this.repartidor = new SimpleStringProperty(repartidor);
        this.mensaje = new SimpleStringProperty(mensaje);
    }

    public String getTimestamp() { return timestamp.get(); }
    public SimpleStringProperty timestampProperty() { return timestamp; }

    public String getTipo() { return tipo.get(); }
    public SimpleStringProperty tipoProperty() { return tipo; }

    public String getRepartidor() { return repartidor.get(); }
    public SimpleStringProperty repartidorProperty() { return repartidor; }

    public String getMensaje() { return mensaje.get(); }
    public SimpleStringProperty mensajeProperty() { return mensaje; }
}



import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Pedido {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty cliente;
    private final SimpleStringProperty zona;
    private final SimpleObjectProperty<Prioridad> prioridad;
    private final SimpleStringProperty estado;
    private final SimpleDoubleProperty peso;

    private static int contador = 1;

    public Pedido(String cliente, String zona, Prioridad prioridad, double peso) {
        this.id = new SimpleIntegerProperty(contador++);
        this.cliente = new SimpleStringProperty(cliente);
        this.zona = new SimpleStringProperty(zona);
        this.prioridad = new SimpleObjectProperty<>(prioridad);
        this.estado = new SimpleStringProperty("Pendiente");
        this.peso = new SimpleDoubleProperty(peso);
    }

    public int getId() { return id.get(); }
    public SimpleIntegerProperty idProperty() { return id; }

    public String getCliente() { return cliente.get(); }
    public SimpleStringProperty clienteProperty() { return cliente; }

    public String getZona() { return zona.get(); }
    public SimpleStringProperty zonaProperty() { return zona; }

    public Prioridad getPrioridad() { return prioridad.get(); }
    public SimpleObjectProperty<Prioridad> prioridadProperty() { return prioridad; }

    public String getEstado() { return estado.get(); }
    public SimpleStringProperty estadoProperty() { return estado; }
    public void setEstado(String estado) { this.estado.set(estado); }

    public double getPeso() { return peso.get(); }
    public SimpleDoubleProperty pesoProperty() { return peso; }
}



import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Repartidor {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nombre;
    private final SimpleObjectProperty<EstadoRepartidor> estado;
    private final SimpleStringProperty vehiculo;
    private final SimpleIntegerProperty pedidosAsignados;

    private static int contador = 1;

    public Repartidor(String nombre, EstadoRepartidor estado, String vehiculo) {
        this.id = new SimpleIntegerProperty(contador++);
        this.nombre = new SimpleStringProperty(nombre);
        this.estado = new SimpleObjectProperty<>(estado);
        this.vehiculo = new SimpleStringProperty(vehiculo);
        this.pedidosAsignados = new SimpleIntegerProperty(0);
    }

    public int getId() { return id.get(); }
    public SimpleIntegerProperty idProperty() { return id; }

    public String getNombre() { return nombre.get(); }
    public SimpleStringProperty nombreProperty() { return nombre; }

    public EstadoRepartidor getEstado() { return estado.get(); }
    public SimpleObjectProperty<EstadoRepartidor> estadoProperty() { return estado; }
    public void setEstado(EstadoRepartidor estado) { this.estado.set(estado); }

    public String getVehiculo() { return vehiculo.get(); }
    public SimpleStringProperty vehiculoProperty() { return vehiculo; }

    public int getPedidosAsignados() { return pedidosAsignados.get(); }
    public SimpleIntegerProperty pedidosAsignadosProperty() { return pedidosAsignados; }
    public void setPedidosAsignados(int n) { this.pedidosAsignados.set(n); }
}


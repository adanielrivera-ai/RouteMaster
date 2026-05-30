
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Ruta {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty repartidor;
    private final ObservableList<Pedido> pedidos;
    private final SimpleIntegerProperty cantidadPedidos;
    private final SimpleDoubleProperty kilometrosTotales;
    private final SimpleIntegerProperty minutosEstimados;
    private final SimpleStringProperty estado;

    private static int contador = 1;

    public Ruta(String repartidor, ObservableList<Pedido> pedidos,
                double kilometrosTotales, int minutosEstimados) {
        this.id = new SimpleIntegerProperty(contador++);
        this.repartidor = new SimpleStringProperty(repartidor);
        this.pedidos = FXCollections.observableArrayList(pedidos);
        this.cantidadPedidos = new SimpleIntegerProperty(pedidos.size());
        this.kilometrosTotales = new SimpleDoubleProperty(kilometrosTotales);
        this.minutosEstimados = new SimpleIntegerProperty(minutosEstimados);
        this.estado = new SimpleStringProperty("Planificada");
    }

    public int getId() { return id.get(); }
    public SimpleIntegerProperty idProperty() { return id; }

    public String getRepartidor() { return repartidor.get(); }
    public SimpleStringProperty repartidorProperty() { return repartidor; }

    public ObservableList<Pedido> getPedidos() { return pedidos; }

    public int getCantidadPedidos() { return cantidadPedidos.get(); }
    public SimpleIntegerProperty cantidadPedidosProperty() { return cantidadPedidos; }

    public double getKilometrosTotales() { return kilometrosTotales.get(); }
    public SimpleDoubleProperty kilometrosTotalesProperty() { return kilometrosTotales; }

    public int getMinutosEstimados() { return minutosEstimados.get(); }
    public SimpleIntegerProperty minutosEstimadosProperty() { return minutosEstimados; }

    public String getEstado() { return estado.get(); }
    public SimpleStringProperty estadoProperty() { return estado; }
    public void setEstado(String estado) { this.estado.set(estado); }
}


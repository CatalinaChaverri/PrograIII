package Subastas;

import APIComunicacion.Observable;
import APIComunicacion.Observer;
import Modelos.Message;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author kiarabox
 */
public class Subasta implements Observable {
    private String nombre;
    private String descripcion;
    private String imagen;
    private Producto producto;
    private EstadoSubasta estado;
    private double precioActual;
    private List<Oferta> ofertas;
    private final Set<Observer> observadores;
    private Oferente ganador;

    public Subasta(String nombre, String descripcion, String imagen, Producto producto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.producto = producto;
        this.estado = EstadoSubasta.ABIERTA;
        this.precioActual = producto.getPrecioInicial();
        this.ofertas = new ArrayList<>();
        this.observadores = new HashSet<>();
    }

    public boolean aceptarOferta(Oferta oferta) {
        if (estado != EstadoSubasta.ABIERTA) return false;
        if (oferta.getMonto() <= precioActual) return false;

        precioActual = oferta.getMonto();
        ofertas.add(oferta);
        ganador = oferta.getOferente();
        notifyObservers(new Message(nombre, temaSubasta(), "Oferta aceptada",
                Message.Tipo.OFERTA_ACEPTADA, ganador.getNick() + "|" + precioActual));
        return true;
    }

    public void cerrar() {
        if (estado == EstadoSubasta.ABIERTA) {
            estado = EstadoSubasta.VENDIDA;
            producto.setPrecioFinal(precioActual);
            notifyObservers(new Message(nombre, temaSubasta(), "Subasta cerrada",
                    Message.Tipo.SUBASTA_CERRADA,
                    (ganador != null ? ganador.getNick() : "") + "|" + precioActual));
        }
    }

    public void cancelar() {
        estado = EstadoSubasta.CANCELADA;
        notifyObservers(new Message(nombre, temaSubasta(), "Subasta cancelada",
                Message.Tipo.SUBASTA_CANCELADA));
    }

    public String getNombre()           { return nombre; }
    public String getDescripcion()      { return descripcion; }
    public String getImagen()           { return imagen; }
    public Producto getProducto()       { return producto; }
    public EstadoSubasta getEstado()    { return estado; }
    public double getPrecioActual()     { return precioActual; }
    public List<Oferta> getOfertas()    { return ofertas; }
    public Oferente getGanador()        { return ganador; }

    @Override
    public void addObserver(Observer obs) {
        observadores.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        observadores.remove(obs);
    }

    @Override
    public void notifyObservers(Message msg) {
        for (Observer observador : new HashSet<>(observadores)) {
            observador.update(msg);
        }
    }

    private String temaSubasta() {
        return "subasta:" + nombre;
    }
}

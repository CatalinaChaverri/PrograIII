/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subastas;

/**
 *
 * @author kiarabox
 */
import java.util.ArrayList;
import java.util.List;

public class Subasta {
    private String nombre;
    private String descripcion;
    private String imagen;
    private Producto producto;
    private EstadoSubasta estado;
    private double precioActual;
    private List<Oferta> ofertas;
    private Oferente ganador;
    
    public Subasta(String nombre, String descripcion, String imagen, Producto producto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.producto = producto;
        this.estado = EstadoSubasta.ABIERTA;
        this.precioActual = producto.getPrecioInicial();
        this.ofertas = new ArrayList<>();
    }
    
    public boolean aceptarOferta(Oferta oferta) {
        if (estado != EstadoSubasta.ABIERTA) return false;
        if (oferta.getMonto() <= precioActual) return false;
        
        precioActual = oferta.getMonto();
        ofertas.add(oferta);
        ganador = oferta.getOferente();
        return true;
    }
    
    public void cerrar() {
        if (estado == EstadoSubasta.ABIERTA) {
            estado = EstadoSubasta.VENDIDA;
            producto.setPrecioFinal(precioActual);
        }
    }
    
    public void cancelar() {
        estado = EstadoSubasta.CANCELADA;
    }
    
    public String getNombre()           { return nombre; }
    public String getDescripcion()      { return descripcion; }
    public String getImagen()           { return imagen; }
    public Producto getProducto()       { return producto; }
    public EstadoSubasta getEstado()    { return estado; }
    public double getPrecioActual()     { return precioActual; }
    public List<Oferta> getOfertas()    { return ofertas; }
    public Oferente getGanador()        { return ganador; }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subastas;

/**
 *
 * @author kiarabox
 */
public class Subastador extends Usuario {
    
    public Subastador(String nick) {
        super(nick);
    }
    
    public Subasta crearSubasta(String nombre, String descripcion, String imagen, Producto producto) {
        return new Subasta(nombre, descripcion, imagen, producto);
    }
    
    public boolean aceptarOferta(Subasta subasta, Oferta oferta) {
        return subasta.aceptarOferta(oferta);
    }
    
    public void cerrarSubasta(Subasta subasta) {
        subasta.cerrar();
    }
    
    public void cancelarSubasta(Subasta subasta) {
        subasta.cancelar();
    }
    
    public String mensajeFelicitacion(Oferente ganador, Subasta subasta) {
        return "Felicitaciones " + ganador.getNick() + "! Ganaste la subasta '" +
               subasta.getNombre() + "' con una oferta de $" + subasta.getPrecioActual();
    }
}

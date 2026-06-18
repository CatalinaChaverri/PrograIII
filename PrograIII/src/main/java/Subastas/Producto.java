/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subastas;

/**
 *
 * @author kiarabox
 */
public class Producto {
    private String nombre;
    private String descripcion;
    private String imagen;
    private double precioInicial;
    private double precioFinal;
    
    public Producto(String nombre, String descripcion, String imagen, double precioInicial) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precioInicial = precioInicial;
        this.precioFinal = 0;
    }
    
    public String getNombre()          { return nombre; }
    public String getDescripcion()     { return descripcion; }
    public String getImagen()          { return imagen; }
    public double getPrecioInicial()   { return precioInicial; }
    public double getPrecioFinal()     { return precioFinal; }
    public void setPrecioFinal(double precioFinal) { this.precioFinal = precioFinal; }
}

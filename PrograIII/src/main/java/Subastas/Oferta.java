/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subastas;

/**
 *
 * @author kiarabox
 */
public class Oferta {
    private double monto;
    private Oferente oferente;
    
    public Oferta(double monto, Oferente oferente) {
        this.monto = monto;
        this.oferente = oferente;
    }
    
    public double getMonto()       { return monto; }
    public Oferente getOferente()  { return oferente; }
}
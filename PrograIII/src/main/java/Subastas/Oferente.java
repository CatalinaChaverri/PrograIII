/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subastas;

/**
 *
 * @author kiarabox
 */
public class Oferente extends Usuario {
    
    public Oferente(String nick) {
        super(nick);
    }
    
    public Oferta hacerOferta(double monto) {
        return new Oferta(monto, this);
    }
}

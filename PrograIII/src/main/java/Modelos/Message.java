/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.io.Serializable;

/**
 *
 * @author catac
 */
public class Message implements Serializable {
    public enum Tipo {
        // Subastas
        NUEVA_SUBASTA, NUEVA_OFERTA, OFERTA_ACEPTADA, SUBASTA_CERRADA,
        SUBASTA_CANCELADA, UNIRSE_SUBASTA, FELICITACION,
        // Red Social
        NUEVO_POST, NUEVO_SEGUIDOR, SUBIDA_NIVEL, LIKES_MILESTONE,
        LIKE_POST, DISLIKE_POST, ARTISTA_BAJA,
        // General
        CONECTAR, DESCONECTAR, REGISTRAR, SUSCRIBIR, ERROR
    }
    
    public String emisor;
    public String receptor;
    public String mensaje;
    public Tipo tipo;
    public String datos; 
    
    public Message(String emisor, String receptor, String mensaje, Tipo tipo) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }
    
    public Message(String emisor, String receptor, String mensaje, Tipo tipo, String datos) {
        this(emisor, receptor, mensaje, tipo);
        this.datos = datos;
    }
}

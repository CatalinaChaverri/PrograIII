/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Modelos.Message;
import static Modelos.Message.Tipo.ARTISTA_BAJA;
import static Modelos.Message.Tipo.ACTUALIZAR_POST;
import static Modelos.Message.Tipo.DESCONECTAR;
import static Modelos.Message.Tipo.DISLIKE_POST;
import static Modelos.Message.Tipo.FELICITACION;
import static Modelos.Message.Tipo.LIKES_MILESTONE;
import static Modelos.Message.Tipo.LIKE_POST;
import static Modelos.Message.Tipo.NUEVA_OFERTA;
import static Modelos.Message.Tipo.NUEVA_SUBASTA;
import static Modelos.Message.Tipo.NUEVO_POST;
import static Modelos.Message.Tipo.NUEVO_SEGUIDOR;
import static Modelos.Message.Tipo.OFERTA_ACEPTADA;
import static Modelos.Message.Tipo.REGISTRAR;
import static Modelos.Message.Tipo.SUBASTA_CANCELADA;
import static Modelos.Message.Tipo.SUBASTA_CERRADA;
import static Modelos.Message.Tipo.SUBIDA_NIVEL;
import static Modelos.Message.Tipo.SUSCRIBIR;
import static Modelos.Message.Tipo.UNIRSE_SUBASTA;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author catac
 */
public class ThreadServidor extends Thread{
    private Servidor servidor;
    private Socket socket; //Spcket espejo del cliente recibido en el accept
    private ObjectInputStream readerStream;
    private ObjectOutputStream writerStream;
    private boolean isRunning = true;
    private String nombre;

    public ThreadServidor(Servidor servidor, Socket socket) {
        this.servidor = servidor;
        this.socket = socket;
        try {
            // IMPORTANTE: primero ObjectOutputStream
            writerStream = new ObjectOutputStream(socket.getOutputStream());
            writerStream.flush();

            // Luego ObjectInputStream
            readerStream = new ObjectInputStream(socket.getInputStream());
            
        } catch (IOException ex) {
            servidor.serverForm.escribirMensaje(ex.getMessage());
        }
    }
    
    @Override
    public void run() {
        while (isRunning) {
            try {
                Message mensajeRecibido = (Message) readerStream.readObject();
                procesarMensaje(mensajeRecibido);
            } catch (IOException ex) {
                servidor.serverForm.escribirMensaje(ex.getMessage());
                isRunning = false;
                servidor.remover(this);
            } catch (ClassNotFoundException ex) {
                servidor.serverForm.escribirMensaje(ex.getMessage());
            }
        }
    }
    
    private void procesarMensaje(Message mensaje) {
        servidor.serverForm.escribirMensaje("Mensaje: " + mensaje.tipo + " de " + mensaje.emisor
                + " para " + mensaje.receptor + " | " + mensaje.mensaje);
        switch (mensaje.tipo) {
            case REGISTRAR:
                servidor.registrar(this, mensaje.emisor);
                enviar(new Message("SERVIDOR", mensaje.emisor, "Conectado como " + mensaje.emisor, Message.Tipo.CONECTAR));
                break;
            case SUSCRIBIR:
                servidor.suscribir(this, mensaje.datos != null ? mensaje.datos : mensaje.receptor);
                break;
            case NUEVA_SUBASTA:
                servidor.suscribir(this, mensaje.receptor);
                servidor.publicarTema(mensaje.receptor, mensaje);
                break;
            case NUEVA_OFERTA:
            case UNIRSE_SUBASTA:
            case OFERTA_ACEPTADA:
            case SUBASTA_CERRADA:
            case SUBASTA_CANCELADA:
            case NUEVO_POST:
            case NUEVO_SEGUIDOR:
            case LIKE_POST:
            case DISLIKE_POST:
            case ACTUALIZAR_POST:
            case SUBIDA_NIVEL:
            case LIKES_MILESTONE:
            case FELICITACION:
            case ARTISTA_BAJA:
                servidor.publicarTema(mensaje.receptor, mensaje);
                break;
            case DESCONECTAR:
                isRunning = false;
                servidor.remover(this);
                break;
            default:
                enviar(new Message("SERVIDOR", mensaje.emisor, "Tipo no soportado", Message.Tipo.ERROR));
                break;
        }
    }
    
    public void enviar(Message mensaje) {
        try {
            writerStream.writeObject(mensaje);
            writerStream.flush();
        } catch (IOException ex) {
            servidor.serverForm.escribirMensaje("Error enviando a " + nombre + ": " + ex.getMessage());
        }
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ObjectOutputStream getWriterStream() {
        return writerStream;
    }
    
}

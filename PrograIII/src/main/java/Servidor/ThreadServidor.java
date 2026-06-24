/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Modelos.Message;
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
    
    public void run(){
        Message mensajeRecibido;
        while(isRunning){
            try {
                mensajeRecibido = (Message)readerStream.readObject();
                servidor.serverForm.escribirMensaje("Msj de client: " +
                        mensajeRecibido + "\n" );
//                if (mensajeRecibido.tipo.equals("Name")){
//                    this.nombre = mensajeRecibido.mensaje;
//                    writerStream.writeObject(new Message("NameConfirmation", "Servidor", nombre, "Tu TS recibió tu nombre"));
//                }else if (mensajeRecibido.tipo.equals("Broadcast")){
//                    servidor.broadcast(mensajeRecibido);
//                }else if (mensajeRecibido.tipo.equals("Private")){
//                    servidor.sendPrivateMessage(mensajeRecibido);
//                }else if (mensajeRecibido.tipo.equals("Disparo")){
//                    servidor.sendPrivateMessage(mensajeRecibido);
//                }
                
                //TODO: procesar el mensaje recibido
                // if tipo == ... then haga x
                //broadcast: repartir el mensaje a todos
                //mensaje individual
                
            } catch (IOException ex) {
                servidor.serverForm.escribirMensaje(ex.getMessage());
                isRunning = false;
            } catch (ClassNotFoundException ex) {
                servidor.serverForm.escribirMensaje(ex.getMessage());
            }
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

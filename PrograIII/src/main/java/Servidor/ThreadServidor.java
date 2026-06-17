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

    public ThreadServidor(Servidor servidor, Socket socket) {
        this.servidor = servidor;
        this.socket = socket;
        
        try {
            readerStream = new ObjectInputStream(socket.getInputStream());
            writerStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            servidor.serverForm.escribirMensaje(ex.getMessage());
        }
    }
    
    public void run(){
        Message mensajeRecibido;
        while(isRunning){
            try {
                mensajeRecibido = (Message)readerStream.readObject();
            } catch (IOException ex) {
                servidor.serverForm.escribirMensaje(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                servidor.serverForm.escribirMensaje(ex.getMessage());
            }
        }
    }
    
}

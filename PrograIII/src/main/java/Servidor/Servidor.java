/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author catac
 */
public class Servidor {
    private final int PORT = 35501;
    ServerSocket serverSocket;
    ServerForm serverForm;
    ThreadConexiones threadConexiones;
    //TODO
    //arrayList de clientes conectados: ThreadServidor
    
    public Servidor(ServerForm serverForm){
        this.serverForm = serverForm;
        this.connect();
        this.threadConexiones = new ThreadConexiones(this);
        this.threadConexiones.start();
    }
    
    private void connect(){
        try {
            serverSocket = new ServerSocket(PORT);
            serverForm.escribirMensaje("Servidor escuchando puerto: "+ PORT);
        } catch (IOException ex) {
            serverForm.escribirMensaje("Error levantando el server: "+ex.getMessage());
        }
    }
}

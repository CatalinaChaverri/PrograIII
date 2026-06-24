/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Modelos.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

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
    ArrayList<ThreadServidor> connectClientsThreads = new ArrayList<ThreadServidor>();
    
    public Servidor(ServerForm serverForm){
        this.serverForm = serverForm;
        this.connect();
        this.threadConexiones =  new ThreadConexiones(this);
        
    }
    
    private void connect(){
        try {
            serverSocket = new ServerSocket(PORT);
            serverForm.escribirMensaje("Servidor escuchando puerto: " + PORT);
        } catch (IOException ex) {
            serverForm.escribirMensaje("Error levantando el server: "+ex.getMessage());
        }
    }
    
    public void broadcast(Message msg){
        
        for (ThreadServidor client : connectClientsThreads) {
            try {
                client.getWriterStream().writeObject(msg);
            } catch (IOException ex) {
                serverForm.escribirMensaje("Error levantando el server: "+ex.getMessage());
            }
        }
    }
    
    public void sendPrivateMessage(Message msg){
        
        for (ThreadServidor client : connectClientsThreads) {
            try {
                if (msg.receptor.equals(client.getNombre())){
                    client.getWriterStream().writeObject(msg);
                    break;
                }
            } catch (IOException ex) {
                serverForm.escribirMensaje("Error levantando el server: "+ex.getMessage());
            }
        }
    }
}

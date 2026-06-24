/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author catac
 */
public class ThreadConexiones extends Thread{
    private Servidor servidor;
    private boolean isRunning = true;
    private int counter = 0;

    public ThreadConexiones(Servidor servidor) {
        this.servidor = servidor;
    }
    
    
    
    public void run(){
        servidor.serverForm.escribirMensaje("Ahora escuchando conexiones \n" );
        while (isRunning){
            try {
                //se queda esperando que alguien en la red se conecte a la IP y port
                //cuando alguien se conecta obtiene el socket y crea la conexion
                //en el server con el cliente
                Socket socketCliente = servidor.serverSocket.accept();
                servidor.serverForm.escribirMensaje("Conexión recibida: #" + (++counter) + "\n");
                //crear todo el threadServidor
                ThreadServidor newClient = new ThreadServidor(servidor, socketCliente);
                servidor.connectClientsThreads.add(newClient);
                newClient.start();
                servidor.serverForm.escribirMensaje("Cliente agregado al arreglo\n");
            } catch (IOException ex) {
                servidor.serverForm.escribirMensaje("Error en accept: " + ex.getMessage() + "\n");
            }
        }
    }
}

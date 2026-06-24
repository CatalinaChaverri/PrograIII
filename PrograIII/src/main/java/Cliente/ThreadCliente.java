/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import Modelos.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author catac
 */
public class ThreadCliente extends Thread{
    private Cliente cliente;
    private Socket socket; //el socket espeejo del cliente recibido en el accept
    private ObjectInputStream readerStream;
    
    private boolean isRunning = true;

       //1: servidor, 2: socket
    public ThreadCliente(Cliente cliente, Socket socket) {
            this.cliente = cliente;
            this.socket = socket;
        try {
            // Luego ObjectInputStream
            readerStream = new ObjectInputStream(socket.getInputStream());
            
        } catch (IOException ex) {
            
        }
    }
    
    
    public void run (){
        Message mensajeRecibido;
        while(isRunning){
            System.out.println("running");
            try {
                mensajeRecibido = (Message)readerStream.readObject();
                
                System.out.println("Out if: " + mensajeRecibido);
                
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                isRunning = false;
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

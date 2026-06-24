/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import Modelos.Message;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author catac
 */
public class Cliente {
    private final int PORT = 35501;
    private final String IP = "localhost";
    String nombre;
    Socket socket;
    ClientForm pantalla;
    ThreadCliente threadCliente;
    private ObjectOutputStream writerStream;
    int counter = 0;
    

    public Cliente(ClientForm pantalla) {
        this.pantalla = pantalla;
        this.connect();
    }
    
    private void connect(){
        try {
            this.socket = new Socket(IP, PORT);
            writerStream = new ObjectOutputStream(socket.getOutputStream());
            writerStream.flush();
            threadCliente = new ThreadCliente(this, socket);
            threadCliente.start();
            //this.nombre = pantalla.obtenerNombre();
        } catch (IOException ex) {
            
        }
    }
     
    public void escribirMensaje(Message msg){
        try {
            writerStream.writeObject(msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import APIComunicacion.MessageListener;
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
    ThreadCliente threadCliente;
    private ObjectOutputStream writerStream;
    private MessageListener listener;
    int counter = 0;
    
    public Cliente(String nombre, MessageListener listener) {
        this.nombre = nombre;
        this.listener = listener;
        this.connect();
        registrar(nombre);
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
            writerStream.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void registrar(String nombre) {
        escribirMensaje(new Message(nombre, "SERVIDOR", "registrar", Message.Tipo.REGISTRAR));
    }
    
    public void suscribir(String tema) {
        escribirMensaje(new Message(nombre, tema, "suscribir", Message.Tipo.SUSCRIBIR, tema));
    }
    
    public void recibirMensaje(Message msg) {
        if (listener != null) {
            listener.onMessage(msg);
        }
    }
    
    
}

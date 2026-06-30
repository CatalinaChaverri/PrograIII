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
    private final ArrayList<ClientesTema> clientesPorTema = new ArrayList<>();
    
    public Servidor(ServerForm serverForm){
        this.serverForm = serverForm;
        this.connect();
        this.threadConexiones =  new ThreadConexiones(this);
        this.threadConexiones.start();
        
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
        for (ThreadServidor cliente : new ArrayList<>(connectClientsThreads)) {
            cliente.enviar(msg);
        }
    }
    
    public void registrar(ThreadServidor cliente, String nombre) {
        cliente.setNombre(nombre);
        if (!connectClientsThreads.contains(cliente)) {
            connectClientsThreads.add(cliente);
        }
        serverForm.escribirMensaje("Cliente registrado: " + nombre);
    }
    
    public void suscribir(ThreadServidor cliente, String tema) {
        ClientesTema clientesTema = obtenerClientesTema(tema);
        if (clientesTema == null) {
            clientesTema = new ClientesTema(tema);
            clientesPorTema.add(clientesTema);
        }
        clientesTema.agregarCliente(cliente);
        serverForm.escribirMensaje(cliente.getNombre() + " observa " + tema);
    }
    
    public boolean publicarTema(String tema, Message msg) {
        ClientesTema clientesTema = obtenerClientesTema(tema);
        if (clientesTema == null || clientesTema.clientes.isEmpty()) {
            return false;
        }
        for (ThreadServidor cliente : new ArrayList<>(clientesTema.clientes)) {
            cliente.enviar(msg);
        }
        return true;
    }
    
    public void remover(ThreadServidor cliente) {
        connectClientsThreads.remove(cliente);
        for (ClientesTema clientesTema : clientesPorTema) {
            clientesTema.removerCliente(cliente);
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

    private ClientesTema obtenerClientesTema(String tema) {
        for (ClientesTema clientesTema : clientesPorTema) {
            if (clientesTema.tema.equals(tema)) {
                return clientesTema;
            }
        }
        return null;
    }

    private class ClientesTema {
        private String tema;
        private ArrayList<ThreadServidor> clientes = new ArrayList<>();

        private ClientesTema(String tema) {
            this.tema = tema;
        }

        private void agregarCliente(ThreadServidor cliente) {
            if (!clientes.contains(cliente)) {
                clientes.add(cliente);
            }
        }

        private void removerCliente(ThreadServidor cliente) {
            clientes.remove(cliente);
        }
    }
}

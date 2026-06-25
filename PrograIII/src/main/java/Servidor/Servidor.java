/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Modelos.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private final Map<String, Set<ThreadServidor>> clientesPorTema = new HashMap<>();
    
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
    
    public synchronized void registrar(ThreadServidor cliente, String nombre) {
        cliente.setNombre(nombre);
        if (!connectClientsThreads.contains(cliente)) {
            connectClientsThreads.add(cliente);
        }
        serverForm.escribirMensaje("Cliente registrado: " + nombre);
    }
    
    public synchronized void suscribir(ThreadServidor cliente, String tema) {
        clientesPorTema.computeIfAbsent(tema, k -> new HashSet<>()).add(cliente);
        serverForm.escribirMensaje(cliente.getNombre() + " observa " + tema);
    }
    
    public synchronized void publicarTema(String tema, Message msg) {
        Set<ThreadServidor> clientesTema = clientesPorTema.get(tema);
        if (clientesTema == null || clientesTema.isEmpty()) {
            return;
        }
        for (ThreadServidor cliente : new HashSet<>(clientesTema)) {
            cliente.enviar(msg);
        }
    }
    
    public synchronized void remover(ThreadServidor cliente) {
        connectClientsThreads.remove(cliente);
        for (Set<ThreadServidor> clientesTema : clientesPorTema.values()) {
            clientesTema.remove(cliente);
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

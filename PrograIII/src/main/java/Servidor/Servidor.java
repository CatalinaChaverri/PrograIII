/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import APIComunicacion.Observable;
import APIComunicacion.Observer;
import APIComunicacion.ObserverRemoto;
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
    ArrayList<ThreadServidor> connectClientsThreads = new ArrayList<ThreadServidor>();
    private final Set<Observer> observadores = new HashSet<>();
    private final Map<String, Set<ObserverRemoto>> observadoresPorTema = new HashMap<>();
    private final Map<ThreadServidor, ObserverRemoto> observadoresRemotos = new HashMap<>();
    
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
        notifyObservers(msg);
    }
    
    public synchronized void registrar(ThreadServidor cliente, String nombre) {
        cliente.setNombre(nombre);
        addObserver(observadorRemoto(cliente));
        serverForm.escribirMensaje("Cliente registrado: " + nombre);
    }
    
    public synchronized void suscribir(ThreadServidor cliente, String tema) {
        ObserverRemoto observador = observadorRemoto(cliente);
        addObserver(observador);
        observadoresPorTema.computeIfAbsent(tema, k -> new HashSet<>()).add(observador);
        serverForm.escribirMensaje(cliente.getNombre() + " observa " + tema);
    }
    
    public synchronized void publicarTema(String tema, Message msg) {
        Set<ObserverRemoto> observadoresTema = observadoresPorTema.get(tema);
        if (observadoresTema == null || observadoresTema.isEmpty()) {
            return;
        }
        for (ObserverRemoto observador : new HashSet<>(observadoresTema)) {
            observador.update(msg);
        }
    }
    
    public synchronized void remover(ThreadServidor cliente) {
        connectClientsThreads.remove(cliente);
        ObserverRemoto observador = observadoresRemotos.remove(cliente);
        if (observador != null) {
            removeObserver(observador);
        }
        for (Set<ObserverRemoto> observadoresTema : observadoresPorTema.values()) {
            observadoresTema.remove(observador);
        }
    }

    public synchronized void addObserver(Observer obs) {
        observadores.add(obs);
    }

    public synchronized void removeObserver(Observer obs) {
        observadores.remove(obs);
    }

    public synchronized void notifyObservers(Message msg) {
        for (Observer observador : new HashSet<>(observadores)) {
            observador.update(msg);
        }
    }

    private ObserverRemoto observadorRemoto(ThreadServidor cliente) {
        return observadoresRemotos.computeIfAbsent(cliente, ObserverRemoto::new);
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

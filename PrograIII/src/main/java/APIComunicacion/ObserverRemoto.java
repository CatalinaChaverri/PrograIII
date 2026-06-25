/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package APIComunicacion;

import Modelos.Message;
import Servidor.ThreadServidor;

/**
 *
 * @author catac
 */
public class ObserverRemoto implements Observer {
   private final ThreadServidor conexion;

    public ObserverRemoto(ThreadServidor conexion) {
        this.conexion = conexion;
    }

    @Override
    public void update(Message msg) {
        conexion.enviar(msg);
    }

    public ThreadServidor getConexion() {
        return conexion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ObserverRemoto)) {
            return false;
        }
        ObserverRemoto other = (ObserverRemoto) obj;
        return conexion == other.conexion;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(conexion);
    }
}

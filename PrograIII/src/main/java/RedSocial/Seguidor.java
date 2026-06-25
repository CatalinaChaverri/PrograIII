package RedSocial;

import APIComunicacion.Observer;
import APIComunicacion.MessageListener;
import Modelos.Message;

/**
 *
 * @author kiarabox
 */
public class Seguidor implements Observer {
    private String nombre;
    private MessageListener listener;

    public Seguidor(String nombre) {
        this.nombre = nombre;
    }

    public Seguidor(String nombre, MessageListener listener) {
        this(nombre);
        this.listener = listener;
    }

    public String getNombre() { return nombre; }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(Message mensaje) {
        if (listener != null) {
            listener.onMessage(mensaje);
        }
    }
}

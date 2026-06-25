package Subastas;

import APIComunicacion.Observer;
import APIComunicacion.MessageListener;
import Modelos.Message;

/**
 *
 * @author kiarabox
 */
public class Oferente extends Usuario implements Observer {
    private MessageListener listener;

    public Oferente(String nick) {
        super(nick);
    }

    public Oferente(String nick, MessageListener listener) {
        this(nick);
        this.listener = listener;
    }

    public Oferta hacerOferta(double monto) {
        return new Oferta(monto, this);
    }

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

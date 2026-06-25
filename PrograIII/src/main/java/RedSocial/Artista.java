package RedSocial;

import APIComunicacion.Observable;
import APIComunicacion.Observer;
import Modelos.Message;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author kiarabox
 */
public class Artista implements Observable {
    private String nombre;
    private int nivel;
    private List<Seguidor> seguidores;
    private List<Publicacion> publicaciones;
    private final Set<Observer> observadores;

    public Artista(String nombre) {
        this.nombre = nombre;
        this.nivel = 1;
        this.seguidores = new ArrayList<>();
        this.publicaciones = new ArrayList<>();
        this.observadores = new HashSet<>();
    }

    public Publicacion postear(String mensaje) {
        Publicacion pub = new Publicacion(mensaje, nombre);
        publicaciones.add(pub);
        int indice = publicaciones.size() - 1;
        notifyObservers(new Message(nombre, temaArtista(),
                "Nueva publicacion de " + nombre, Message.Tipo.NUEVO_POST,
                indice + "|" + mensaje));
        return pub;
    }

    public boolean agregarSeguidor(Seguidor seguidor) {
        seguidores.add(seguidor);
        addObserver(seguidor);
        if (seguidores.size() % 10 == 0) {
            nivel++;
            notifyObservers(new Message(nombre, temaArtista(),
                    nombre + " subio al nivel " + nivel + " con " + getCantidadSeguidores() + " seguidores!",
                    Message.Tipo.SUBIDA_NIVEL, String.valueOf(nivel)));
            return true;
        }
        return false;
    }

    public boolean darLikeAPublicacion(Publicacion pub) {
        pub.darLike();
        boolean milestone = pub.getLikes() % 10 == 0;
        if (milestone) {
            int indice = publicaciones.indexOf(pub);
            notifyObservers(new Message(nombre, temaArtista(),
                    "La publicacion de " + nombre + " llego a " + pub.getLikes() + " likes!",
                    Message.Tipo.LIKES_MILESTONE, indice + "|" + pub.getLikes()));
        }
        return milestone;
    }

    public boolean darDislikeAPublicacion(Publicacion pub) {
        pub.darDislike();
        return pub.getDislikes() % 10 == 0;
    }

    public String getNombre()                   { return nombre; }
    public int getNivel()                       { return nivel; }
    public List<Seguidor> getSeguidores()       { return seguidores; }
    public List<Publicacion> getPublicaciones() { return publicaciones; }
    public int getCantidadSeguidores()          { return seguidores.size(); }

    @Override
    public void addObserver(Observer obs) {
        observadores.add(obs);
    }

    @Override
    public void removeObserver(Observer obs) {
        observadores.remove(obs);
    }

    @Override
    public void notifyObservers(Message msg) {
        for (Observer observador : new HashSet<>(observadores)) {
            observador.update(msg);
        }
    }

    private String temaArtista() {
        return "artista:" + nombre;
    }
}

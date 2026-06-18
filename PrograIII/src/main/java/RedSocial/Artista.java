/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RedSocial;

/**
 *
 * @author kiarabox
 */
import java.util.ArrayList;
import java.util.List;

public class Artista {
    private String nombre;
    private int nivel;
    private List<Seguidor> seguidores;
    private List<Publicacion> publicaciones;
    
    public Artista(String nombre) {
        this.nombre = nombre;
        this.nivel = 1;
        this.seguidores = new ArrayList<>();
        this.publicaciones = new ArrayList<>();
    }
    
    public Publicacion postear(String mensaje) {
        Publicacion pub = new Publicacion(mensaje, nombre);
        publicaciones.add(pub);
        return pub;
    }
    
    public boolean agregarSeguidor(Seguidor seguidor) {
        seguidores.add(seguidor);
        // Verificar si llegó a multiplo de 10
        if (seguidores.size() % 10 == 0) {
            nivel++;
            return true; 
        }
        return false;
    }
    
    public boolean darLikeAPublicacion(Publicacion pub) {
        pub.darLike();
        // Verificar multiplo de 10 likes
        return pub.getLikes() % 10 == 0;
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
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RedSocial;

/**
 *
 * @author kiarabox
 */
public class Publicacion {
    private String mensaje;
    private int likes;
    private int dislikes;
    private String autor;
    
    public Publicacion(String mensaje, String autor) {
        this.mensaje = mensaje;
        this.autor = autor;
        this.likes = 0;
        this.dislikes = 0;
    }
    
    public void darLike() {
        likes++;
    }
    
    public void darDislike() {
        dislikes++;
    }
    
    public String getMensaje()  { return mensaje; }
    public String getAutor()    { return autor; }
    public int getLikes()       { return likes; }
    public int getDislikes()    { return dislikes; }
}

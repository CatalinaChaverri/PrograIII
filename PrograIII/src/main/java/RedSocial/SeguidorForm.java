package RedSocial;

import Cliente.Cliente;
import Modelos.Message;
import static Modelos.Message.Tipo.ACTUALIZAR_POST;
import static Modelos.Message.Tipo.ARTISTA_BAJA;
import static Modelos.Message.Tipo.ERROR;
import static Modelos.Message.Tipo.LIKES_MILESTONE;
import static Modelos.Message.Tipo.NUEVO_POST;
import static Modelos.Message.Tipo.SEGUIDOR_ACEPTADO;
import static Modelos.Message.Tipo.SUBIDA_NIVEL;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SeguidorForm extends javax.swing.JFrame {

    private final Seguidor seguidor;
    private final DefaultListModel<String> modeloFeed;
    private final DefaultListModel<String> modeloArtistas;
    private final List<String> temasFeed;
    private final List<Integer> indicesFeed;
    private final Set<String> artistasDadosDeBaja;
    private Cliente cliente;

    public SeguidorForm(String nombre) {
        this.seguidor = new Seguidor(nombre);
        this.modeloFeed = new DefaultListModel<>();
        this.modeloArtistas = new DefaultListModel<>();
        this.temasFeed = new ArrayList<>();
        this.indicesFeed = new ArrayList<>();
        this.artistasDadosDeBaja = new HashSet<>();
        initComponents();
        setTitle("Seguidor: " + nombre);
        lstFeed.setModel(modeloFeed);
        lstArtistas.setModel(modeloArtistas);
        cliente = new Cliente(nombre, this::procesarMensaje);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(700, 600);

        JLabel lblTitulo = new JLabel("RED SOCIAL VIP - SEGUIDOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 200));
        lblTitulo.setBounds(150, 10, 400, 35);
        add(lblTitulo);

        JLabel lblNombre = new JLabel("Seguidor: " + seguidor.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setBounds(20, 50, 300, 25);
        add(lblNombre);

        JLabel lblSeguir = new JLabel("Artista:");
        lblSeguir.setBounds(380, 85, 80, 25);
        add(lblSeguir);

        txtArtista = new JTextField();
        txtArtista.setBounds(455, 85, 120, 25);
        add(txtArtista);

        JButton btnSeguir = button("Seguir", 585, 85, 75, new Color(80, 120, 190));
        btnSeguir.addActionListener(e -> seguirArtista());
        add(btnSeguir);

        JLabel lblArtistasTit = new JLabel("--- Artistas que seguis ---");
        lblArtistasTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblArtistasTit.setBounds(380, 120, 280, 25);
        add(lblArtistasTit);

        lstArtistas = new JList<>();
        JScrollPane scrollArtistas = new JScrollPane(lstArtistas);
        scrollArtistas.setBounds(380, 150, 280, 150);
        add(scrollArtistas);

        JLabel lblFeedTit = new JLabel("--- Feed de publicaciones ---");
        lblFeedTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblFeedTit.setBounds(20, 85, 280, 25);
        add(lblFeedTit);

        lstFeed = new JList<>();
        JScrollPane scrollFeed = new JScrollPane(lstFeed);
        scrollFeed.setBounds(20, 115, 340, 300);
        add(scrollFeed);

        JButton btnLike = button("Like", 20, 425, 160, new Color(50, 150, 50));
        btnLike.addActionListener(e -> darLike());
        add(btnLike);

        JButton btnDislike = button("Dislike", 190, 425, 160, new Color(200, 50, 50));
        btnDislike.addActionListener(e -> darDislike());
        add(btnDislike);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setBounds(20, 475, 650, 30);
        add(lblMensaje);

        lblNotificacion = new JLabel("", SwingConstants.CENTER);
        lblNotificacion.setFont(new Font("Arial", Font.BOLD, 14));
        lblNotificacion.setForeground(new Color(150, 0, 150));
        lblNotificacion.setBounds(20, 510, 650, 30);
        add(lblNotificacion);
    }

    private void seguirArtista() {
        String artista = txtArtista.getText().trim();
        if (artista.isEmpty()) {
            mostrar("Ingresa el nombre del artista!", Color.RED);
            return;
        }
        if (artistasDadosDeBaja.contains(artista)) {
            mostrar("Ese artista se dio de baja y ya no esta disponible.", Color.RED);
            return;
        }
        String tema = temaArtista(artista);
        cliente.suscribir(tema);
        cliente.escribirMensaje(new Message(seguidor.getNombre(), tema,
                "Nuevo seguidor", Message.Tipo.NUEVO_SEGUIDOR));
    }

    private void darLike() {
        reaccionar(Message.Tipo.LIKE_POST, "Like");
    }

    private void darDislike() {
        reaccionar(Message.Tipo.DISLIKE_POST, "Dislike");
    }

    private void reaccionar(Message.Tipo tipo, String accion) {
        int idx = lstFeed.getSelectedIndex();
        if (idx < 0) {
            mostrar("Selecciona una publicacion!", Color.RED);
            return;
        }
        cliente.escribirMensaje(new Message(seguidor.getNombre(), temasFeed.get(idx),
                accion, tipo, String.valueOf(indicesFeed.get(idx))));
        mostrar("Enviaste " + accion + " a la publicacion " + (idx + 1), new Color(0, 120, 0));
    }

    private void procesarMensaje(Message mensaje) {
        switch (mensaje.tipo) {
            case NUEVO_POST:
                recibirPublicacion(mensaje);
                break;
            case SUBIDA_NIVEL:
                notificarSubidaNivel(mensaje.emisor, mensaje.datos);
                break;
            case LIKES_MILESTONE:
                notificarMilestoneLikes(mensaje.emisor, mensaje.datos);
                break;
            case ACTUALIZAR_POST:
                actualizarPost(mensaje);
                break;
            case ARTISTA_BAJA:
                procesarBajaArtista(mensaje);
                JOptionPane.showMessageDialog(this, mensaje.mensaje, "Notificacion", JOptionPane.INFORMATION_MESSAGE);
                break;
            case SEGUIDOR_ACEPTADO:
                confirmarSeguimiento(mensaje.emisor);
                break;
            case ERROR:
                mostrar(mensaje.mensaje, Color.RED);
                JOptionPane.showMessageDialog(this, mensaje.mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                break;
        }
    }
    
    private void confirmarSeguimiento(String artista){
        cliente.suscribir(temaArtista(artista));
        if(!modeloArtistas.contains(artista)){
            modeloArtistas.addElement(artista);
        }
        txtArtista.setText("");
        mostrar("Ahora seguis a " + artista, new Color(0, 120, 0));
    }

    private void recibirPublicacion(Message mensaje) {
        String[] partes = mensaje.datos != null ? mensaje.datos.split("\\|", 2) : new String[0];
        if (partes.length < 2 || artistasDadosDeBaja.contains(mensaje.emisor)) {
            return;
        }

        int indice = parseEntero(partes[0], -1);
        if (indice < 0) {
            return;
        }

        modeloFeed.addElement("[" + mensaje.emisor + "] " + partes[1] + " | Likes: 0 | Dislikes: 0");
        temasFeed.add(temaArtista(mensaje.emisor));
        indicesFeed.add(indice);
        actualizarArtistaConContador(mensaje.emisor);
        lblNotificacion.setText("Nueva publicacion de " + mensaje.emisor + "!");
    }

    private void actualizarArtistaConContador(String artista) {
        int totalPubs = 0;
        for (String tema : temasFeed) {
            if (tema.equals(temaArtista(artista))) {
                totalPubs++;
            }
        }

        String texto = artista + " (" + totalPubs + " publicaciones)";
        for (int i = 0; i < modeloArtistas.size(); i++) {
            if (obtenerNombreArtista(modeloArtistas.get(i)).equals(artista)) {
                modeloArtistas.set(i, texto);
                return;
            }
        }
        modeloArtistas.addElement(texto);
    }

    private void actualizarPost(Message mensaje) {
        String[] partes = mensaje.datos != null ? mensaje.datos.split("\\|", -1) : new String[0];
        if (partes.length < 3) {
            return;
        }
        int indice = parseEntero(partes[0], -1);
        int likes = parseEntero(partes[1], 0);
        int dislikes = parseEntero(partes[2], 0);
        String tema = temaArtista(mensaje.emisor);
        for (int i = 0; i < modeloFeed.size(); i++) {
            if (tema.equals(temasFeed.get(i)) && indicesFeed.get(i) == indice) {
                modeloFeed.set(i, reemplazarReacciones(modeloFeed.get(i), likes, dislikes));
                return;
            }
        }
    }

    private String reemplazarReacciones(String publicacion, int likes, int dislikes) {
        String textoBase = publicacion.split(" \\| Likes: ", 2)[0];
        return textoBase + " | Likes: " + likes + " | Dislikes: " + dislikes;
    }

    private void procesarBajaArtista(Message mensaje) {
        artistasDadosDeBaja.add(mensaje.emisor);
        eliminarArtistaDeLista(mensaje.emisor);
        String tema = temaArtista(mensaje.emisor);
        for (int i = modeloFeed.size() - 1; i >= 0; i--) {
            if (tema.equals(temasFeed.get(i))) {
                modeloFeed.remove(i);
                temasFeed.remove(i);
                indicesFeed.remove(i);
            }
        }
        lblNotificacion.setText(mensaje.mensaje);
    }

    private void eliminarArtistaDeLista(String artista) {
        for (int i = modeloArtistas.size() - 1; i >= 0; i--) {
            if (obtenerNombreArtista(modeloArtistas.get(i)).equals(artista)) {
                modeloArtistas.remove(i);
            }
        }
    }

    private boolean existeArtistaEnLista(String artista) {
        for (int i = 0; i < modeloArtistas.size(); i++) {
            if (obtenerNombreArtista(modeloArtistas.get(i)).equals(artista)) {
                return true;
            }
        }
        return false;
    }

    private void notificarSubidaNivel(String nombreArtista, String nivel) {
        String msg = nombreArtista + " subio al nivel " + nivel + "!";
        lblNotificacion.setText(msg);
        JOptionPane.showMessageDialog(this, msg, "Notificacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private void notificarMilestoneLikes(String nombreArtista, String datos) {
        String likes = datos != null && datos.contains("|") ? datos.split("\\|", -1)[1] : datos;
        String msg = "Una publicacion de " + nombreArtista + " llego a " + likes + " likes!";
        lblNotificacion.setText(msg);
        JOptionPane.showMessageDialog(this, msg, "Notificacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private String temaArtista(String artista) {
        return "artista:" + artista;
    }

    private String obtenerNombreArtista(String entrada) {
        return entrada.contains(" (") ? entrada.substring(0, entrada.indexOf(" (")) : entrada;
    }

    private int parseEntero(String valor, int valorPorDefecto) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            return valorPorDefecto;
        }
    }

    private void mostrar(String texto, Color color) {
        lblMensaje.setText(texto);
        lblMensaje.setForeground(color);
    }

    private JButton button(String text, int x, int y, int width, Color color) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, width, 35);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private JTextField txtArtista;
    private JList<String> lstFeed;
    private JList<String> lstArtistas;
    private JLabel lblMensaje;
    private JLabel lblNotificacion;
}

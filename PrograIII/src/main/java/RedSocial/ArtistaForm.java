package RedSocial;

import Cliente.Cliente;
import Modelos.Message;
import static Modelos.Message.Tipo.DISLIKE_POST;
import static Modelos.Message.Tipo.LIKE_POST;
import static Modelos.Message.Tipo.NUEVO_SEGUIDOR;
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ArtistaForm extends javax.swing.JFrame {

    private final Artista artista;
    private final DefaultListModel<String> modeloPublicaciones;
    private final DefaultListModel<String> modeloSeguidores;
    private Cliente cliente;
    private boolean dadoDeBaja;

    public ArtistaForm(String nombre) {
        this.artista = new Artista(nombre);
        this.modeloPublicaciones = new DefaultListModel<>();
        this.modeloSeguidores = new DefaultListModel<>();
        initComponents();
        setTitle("Artista: " + nombre);
        lstPublicaciones.setModel(modeloPublicaciones);
        lstSeguidores.setModel(modeloSeguidores);
        cliente = new Cliente(nombre, this::procesarMensaje);
        cliente.suscribir(temaArtista());
        actualizarInfo();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(700, 560);

        JLabel lblTitulo = new JLabel("RED SOCIAL VIP - ARTISTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(150, 0, 150));
        lblTitulo.setBounds(150, 10, 400, 35);
        add(lblTitulo);

        lblNombreNivel = new JLabel();
        lblNombreNivel.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombreNivel.setBounds(20, 50, 400, 25);
        add(lblNombreNivel);

        lblSeguidores = new JLabel();
        lblSeguidores.setBounds(20, 75, 200, 25);
        add(lblSeguidores);

        JLabel lblPostTit = new JLabel("--- Publicar Mensaje ---");
        lblPostTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblPostTit.setBounds(20, 110, 250, 25);
        add(lblPostTit);

        txtMensaje = new JTextArea();
        txtMensaje.setLineWrap(true);
        JScrollPane scrollMsg = new JScrollPane(txtMensaje);
        scrollMsg.setBounds(20, 140, 300, 80);
        add(scrollMsg);

        btnPublicar = button("Publicar", 20, 230, 120, new Color(150, 0, 150));
        btnPublicar.addActionListener(e -> publicar());
        add(btnPublicar);

        JLabel lblPubsTit = new JLabel("Mis publicaciones:");
        lblPubsTit.setFont(new Font("Arial", Font.BOLD, 12));
        lblPubsTit.setBounds(20, 275, 200, 25);
        add(lblPubsTit);

        lstPublicaciones = new JList<>();
        JScrollPane scrollPubs = new JScrollPane(lstPublicaciones);
        scrollPubs.setBounds(20, 300, 300, 180);
        add(scrollPubs);

        JLabel lblListaSegTit = new JLabel("Mis seguidores:");
        lblListaSegTit.setFont(new Font("Arial", Font.BOLD, 12));
        lblListaSegTit.setBounds(380, 110, 200, 25);
        add(lblListaSegTit);

        lstSeguidores = new JList<>();
        JScrollPane scrollSegs = new JScrollPane(lstSeguidores);
        scrollSegs.setBounds(380, 140, 280, 265);
        add(scrollSegs);

        btnBaja = button("Darse de baja", 380, 415, 140, new Color(180, 60, 60));
        btnBaja.addActionListener(e -> darseBaja());
        add(btnBaja);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setBounds(20, 495, 650, 30);
        add(lblMensaje);
    }

    private void publicar() {
        if (dadoDeBaja) {
            mostrar("El artista esta dado de baja y no puede publicar.", Color.RED);
            return;
        }
        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty()) {
            mostrar("Escribe un mensaje para publicar!", Color.RED);
            return;
        }
        Publicacion pub = artista.postear(texto);
        int indice = artista.getPublicaciones().size() - 1;
        modeloPublicaciones.addElement(formatoPublicacion(pub));
        cliente.escribirMensaje(new Message(artista.getNombre(), temaArtista(),
                "Nueva publicacion de " + artista.getNombre(),
                Message.Tipo.NUEVO_POST, indice + "|" + texto));
        txtMensaje.setText("");
        mostrar("Publicacion enviada a " + artista.getCantidadSeguidores() + " seguidores!", new Color(0, 120, 0));
    }

    private void darseBaja() {
        if (dadoDeBaja) {
            return;
        }
        dadoDeBaja = true;
        cliente.escribirMensaje(new Message(artista.getNombre(), temaArtista(),
                artista.getNombre() + " se dio de baja", Message.Tipo.ARTISTA_BAJA));
        artista.eliminarSeguidores();
        modeloSeguidores.clear();
        actualizarInfo();
        btnPublicar.setEnabled(false);
        txtMensaje.setEnabled(false);
        btnBaja.setEnabled(false);
        mostrar("Aviso de baja enviado. Ya no puede publicar.", Color.RED);
    }

    private void procesarMensaje(Message mensaje) {
        if (dadoDeBaja) {
            return;
        }
        switch (mensaje.tipo) {
            case NUEVO_SEGUIDOR:
                agregarSeguidorRemoto(mensaje.emisor);
                break;
            case LIKE_POST:
                recibirLike(mensaje.datos);
                break;
            case DISLIKE_POST:
                recibirDislike(mensaje.datos);
                break;
            default:
                break;
        }
    }

    private void agregarSeguidorRemoto(String nombre) {
        if (modeloSeguidores.contains(nombre)) {
            return;
        }
        boolean subioNivel = artista.agregarSeguidor(new Seguidor(nombre));
        modeloSeguidores.addElement(nombre);
        actualizarInfo();
        if (subioNivel) {
            String msg = artista.getNombre() + " subio al nivel " + artista.getNivel()
                    + " con " + artista.getCantidadSeguidores() + " seguidores!";
            cliente.escribirMensaje(new Message(artista.getNombre(), temaArtista(), msg,
                    Message.Tipo.SUBIDA_NIVEL, String.valueOf(artista.getNivel())));
            JOptionPane.showMessageDialog(this, msg, "Subida de Nivel", JOptionPane.INFORMATION_MESSAGE);
            mostrar(msg, new Color(150, 0, 150));
        } else {
            mostrar("Nuevo seguidor: " + nombre, new Color(0, 120, 0));
        }
    }

    private void recibirLike(String datos) {
        int indice = parseIndice(datos);
        if (indice < 0 || indice >= artista.getPublicaciones().size()) {
            return;
        }
        Publicacion pub = artista.getPublicaciones().get(indice);
        boolean milestone = artista.darLikeAPublicacion(pub);
        actualizarPublicacion(indice, pub);
        notificarActualizacionPost(indice, pub);
        if (milestone) {
            String msg = "La publicacion de " + artista.getNombre() + " llego a " + pub.getLikes() + " likes!";
            cliente.escribirMensaje(new Message(artista.getNombre(), temaArtista(), msg,
                    Message.Tipo.LIKES_MILESTONE, indice + "|" + pub.getLikes()));
            JOptionPane.showMessageDialog(this, msg, "Likes", JOptionPane.INFORMATION_MESSAGE);
            mostrar(msg, new Color(150, 0, 150));
        }
    }

    private void recibirDislike(String datos) {
        int indice = parseIndice(datos);
        if (indice < 0 || indice >= artista.getPublicaciones().size()) {
            return;
        }
        Publicacion pub = artista.getPublicaciones().get(indice);
        artista.darDislikeAPublicacion(pub);
        actualizarPublicacion(indice, pub);
        notificarActualizacionPost(indice, pub);
    }

    private int parseIndice(String datos) {
        try {
            return Integer.parseInt(datos == null ? "-1" : datos.split("\\|", -1)[0]);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void actualizarPublicacion(int indice, Publicacion pub) {
        modeloPublicaciones.set(indice, formatoPublicacion(pub));
    }

    private void notificarActualizacionPost(int indice, Publicacion pub) {
        cliente.escribirMensaje(new Message(artista.getNombre(), temaArtista(), "",
                Message.Tipo.ACTUALIZAR_POST,
                indice + "|" + pub.getLikes() + "|" + pub.getDislikes()));
    }

    private String formatoPublicacion(Publicacion pub) {
        return "[" + artista.getNombre() + "] " + pub.getMensaje()
                + " | Likes: " + pub.getLikes() + " | Dislikes: " + pub.getDislikes();
    }

    private void actualizarInfo() {
        lblNombreNivel.setText("Artista: " + artista.getNombre() + " | Nivel: " + artista.getNivel());
        lblSeguidores.setText("Seguidores: " + artista.getCantidadSeguidores());
    }

    private String temaArtista() {
        return "artista:" + artista.getNombre();
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
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private JTextArea txtMensaje;
    private JButton btnPublicar;
    private JButton btnBaja;
    private JList<String> lstPublicaciones;
    private JList<String> lstSeguidores;
    private JLabel lblNombreNivel;
    private JLabel lblSeguidores;
    private JLabel lblMensaje;
}

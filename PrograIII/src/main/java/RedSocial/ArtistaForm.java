/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RedSocial;

/**
 *
 * @author kiarabox
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ArtistaForm extends javax.swing.JFrame {

    private Artista artista;
    private DefaultListModel<String> modeloPublicaciones;
    private DefaultListModel<String> modeloSeguidores;

    public ArtistaForm(String nombre) {
        this.artista = new Artista(nombre);
        initComponents();
        setTitle("Artista: " + nombre);
        modeloPublicaciones = new DefaultListModel<>();
        modeloSeguidores = new DefaultListModel<>();
        lstPublicaciones.setModel(modeloPublicaciones);
        lstSeguidores.setModel(modeloSeguidores);
        actualizarInfo();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(700, 600);

        JLabel lblTitulo = new JLabel("RED SOCIAL VIP — ARTISTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(150, 0, 150));
        lblTitulo.setBounds(150, 10, 400, 35);
        add(lblTitulo);

        lblNombreNivel = new JLabel("Artista: " + artista.getNombre() + " | Nivel: 1");
        lblNombreNivel.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombreNivel.setBounds(20, 50, 400, 25);
        add(lblNombreNivel);

        lblSeguidores = new JLabel("Seguidores: 0");
        lblSeguidores.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSeguidores.setBounds(20, 75, 200, 25);
        add(lblSeguidores);

        // Panel publicaciones
        JLabel lblPostTit = new JLabel("--- Publicar Mensaje ---");
        lblPostTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblPostTit.setBounds(20, 110, 250, 25);
        add(lblPostTit);

        txtMensaje = new JTextArea();
        txtMensaje.setLineWrap(true);
        JScrollPane scrollMsg = new JScrollPane(txtMensaje);
        scrollMsg.setBounds(20, 140, 300, 80);
        add(scrollMsg);

        JButton btnPublicar = new JButton("Publicar");
        btnPublicar.setBounds(20, 230, 120, 35);
        btnPublicar.setBackground(new Color(150, 0, 150));
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setFocusPainted(false);
        btnPublicar.setOpaque(true);
        btnPublicar.setBorderPainted(false);
        btnPublicar.addActionListener(e -> publicar());
        add(btnPublicar);

        // Lista publicaciones
        JLabel lblPubsTit = new JLabel("Mis publicaciones:");
        lblPubsTit.setFont(new Font("Arial", Font.BOLD, 12));
        lblPubsTit.setBounds(20, 275, 200, 25);
        add(lblPubsTit);

        lstPublicaciones = new JList<>();
        JScrollPane scrollPubs = new JScrollPane(lstPublicaciones);
        scrollPubs.setBounds(20, 300, 300, 200);
        add(scrollPubs);

        // Panel seguidores
        JLabel lblSegTit = new JLabel("--- Agregar Seguidor (prueba) ---");
        lblSegTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblSegTit.setBounds(380, 110, 280, 25);
        add(lblSegTit);

        JLabel lblNombreSeg = new JLabel("Nombre seguidor:");
        lblNombreSeg.setBounds(380, 140, 150, 25);
        add(lblNombreSeg);

        txtNombreSeguidor = new JTextField();
        txtNombreSeguidor.setBounds(380, 165, 180, 25);
        add(txtNombreSeguidor);

        JButton btnAgregarSeg = new JButton("Agregar");
        btnAgregarSeg.setBounds(570, 165, 90, 25);
        btnAgregarSeg.setBackground(new Color(50, 150, 50));
        btnAgregarSeg.setForeground(Color.WHITE);
        btnAgregarSeg.setFocusPainted(false);
        btnAgregarSeg.setOpaque(true);
        btnAgregarSeg.setBorderPainted(false);
        btnAgregarSeg.addActionListener(e -> agregarSeguidor());
        add(btnAgregarSeg);

        // Lista seguidores
        JLabel lblListaSegTit = new JLabel("Mis seguidores:");
        lblListaSegTit.setFont(new Font("Arial", Font.BOLD, 12));
        lblListaSegTit.setBounds(380, 200, 200, 25);
        add(lblListaSegTit);

        lstSeguidores = new JList<>();
        JScrollPane scrollSegs = new JScrollPane(lstSeguidores);
        scrollSegs.setBounds(380, 225, 280, 180);
        add(scrollSegs);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setBounds(20, 510, 650, 30);
        add(lblMensaje);
    }

    private void publicar() {
        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty()) {
            lblMensaje.setText("Escribe un mensaje para publicar!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        Publicacion pub = artista.postear(texto);
        modeloPublicaciones.addElement("[" + artista.getNombre() + "] " +
                                       texto + " | Likes: 0 | Dislikes: 0");
        txtMensaje.setText("");
        lblMensaje.setText("Publicacion enviada a " + artista.getCantidadSeguidores() + " seguidores!");
        lblMensaje.setForeground(new Color(0, 120, 0));
    }

    private void agregarSeguidor() {
        String nombre = txtNombreSeguidor.getText().trim();
        if (nombre.isEmpty()) {
            lblMensaje.setText("Ingresa el nombre del seguidor!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        Seguidor seg = new Seguidor(nombre);
        boolean subioNivel = artista.agregarSeguidor(seg);
        modeloSeguidores.addElement(nombre);
        txtNombreSeguidor.setText("");
        actualizarInfo();

        if (subioNivel) {
            String msg = artista.getNombre() + " subio al nivel " + artista.getNivel() +
                         " con " + artista.getCantidadSeguidores() + " seguidores!";
            lblMensaje.setText(msg);
            lblMensaje.setForeground(new Color(150, 0, 150));
            JOptionPane.showMessageDialog(this, msg, "Subida de Nivel!",
                                          JOptionPane.INFORMATION_MESSAGE);
        } else {
            lblMensaje.setText("Seguidor " + nombre + " agregado!");
            lblMensaje.setForeground(new Color(0, 120, 0));
        }
    }

    // Llamado desde socket cuando llega un like/dislike
    public void recibirLike(int indicePublicacion) {
        if (indicePublicacion >= artista.getPublicaciones().size()) return;
        Publicacion pub = artista.getPublicaciones().get(indicePublicacion);
        boolean milestone = artista.darLikeAPublicacion(pub);
        actualizarPublicacion(indicePublicacion, pub);
        if (milestone) {
            String msg = "Tu publicacion llego a " + pub.getLikes() + " likes!";
            lblMensaje.setText(msg);
            lblMensaje.setForeground(new Color(150, 0, 150));
            JOptionPane.showMessageDialog(this, msg, "Milestone!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarPublicacion(int indice, Publicacion pub) {
        modeloPublicaciones.set(indice, "[" + artista.getNombre() + "] " +
                                pub.getMensaje() + " | Likes: " + pub.getLikes() +
                                " | Dislikes: " + pub.getDislikes());
    }

    private void actualizarInfo() {
        lblNombreNivel.setText("Artista: " + artista.getNombre() +
                               " | Nivel: " + artista.getNivel());
        lblSeguidores.setText("Seguidores: " + artista.getCantidadSeguidores());
    }

    private JTextArea txtMensaje;
    private JTextField txtNombreSeguidor;
    private JList<String> lstPublicaciones;
    private JList<String> lstSeguidores;
    private JLabel lblNombreNivel;
    private JLabel lblSeguidores;
    private JLabel lblMensaje;
}
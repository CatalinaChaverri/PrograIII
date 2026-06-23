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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SeguidorForm extends javax.swing.JFrame {

    private Seguidor seguidor;
    private DefaultListModel<String> modeloFeed;
    private DefaultListModel<String> modeloArtistas;

    public SeguidorForm(String nombre) {
        this.seguidor = new Seguidor(nombre);
        initComponents();
        setTitle("Seguidor: " + nombre);
        modeloFeed = new DefaultListModel<>();
        modeloArtistas = new DefaultListModel<>();
        lstFeed.setModel(modeloFeed);
        lstArtistas.setModel(modeloArtistas);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(700, 600);

        JLabel lblTitulo = new JLabel("RED SOCIAL VIP — SEGUIDOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 100, 200));
        lblTitulo.setBounds(150, 10, 400, 35);
        add(lblTitulo);

        JLabel lblNombre = new JLabel("Seguidor: " + seguidor.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setBounds(20, 50, 300, 25);
        add(lblNombre);

        // Artistas seguidos
        JLabel lblArtistasTit = new JLabel("--- Artistas que seguís ---");
        lblArtistasTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblArtistasTit.setBounds(380, 85, 280, 25);
        add(lblArtistasTit);

        lstArtistas = new JList<>();
        JScrollPane scrollArtistas = new JScrollPane(lstArtistas);
        scrollArtistas.setBounds(380, 115, 280, 150);
        add(scrollArtistas);

        // Feed de publicaciones
        JLabel lblFeedTit = new JLabel("--- Feed de publicaciones ---");
        lblFeedTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblFeedTit.setBounds(20, 85, 280, 25);
        add(lblFeedTit);

        lstFeed = new JList<>();
        JScrollPane scrollFeed = new JScrollPane(lstFeed);
        scrollFeed.setBounds(20, 115, 340, 300);
        add(scrollFeed);

        // Botones like y dislike
        JButton btnLike = new JButton("👍 Like");
        btnLike.setBounds(20, 425, 160, 35);
        btnLike.setBackground(new Color(50, 150, 50));
        btnLike.setForeground(Color.WHITE);
        btnLike.setFont(new Font("Arial", Font.BOLD, 13));
        btnLike.setFocusPainted(false);
        btnLike.setOpaque(true);
        btnLike.setBorderPainted(false);
        btnLike.addActionListener(e -> darLike());
        add(btnLike);

        JButton btnDislike = new JButton("👎 Dislike");
        btnDislike.setBounds(190, 425, 160, 35);
        btnDislike.setBackground(new Color(200, 50, 50));
        btnDislike.setForeground(Color.WHITE);
        btnDislike.setFont(new Font("Arial", Font.BOLD, 13));
        btnDislike.setFocusPainted(false);
        btnDislike.setOpaque(true);
        btnDislike.setBorderPainted(false);
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

    private void darLike() {
        int idx = lstFeed.getSelectedIndex();
        if (idx < 0) {
            lblMensaje.setText("Selecciona una publicacion!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        // Actualizar visualmente
        String item = modeloFeed.get(idx);
        lblMensaje.setText("Le diste Like a la publicacion " + (idx + 1));
        lblMensaje.setForeground(new Color(0, 120, 0));
        // Cuando esté el socket: enviar mensaje de like al servidor
    }

    private void darDislike() {
        int idx = lstFeed.getSelectedIndex();
        if (idx < 0) {
            lblMensaje.setText("Selecciona una publicacion!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        lblMensaje.setText("Le diste Dislike a la publicacion " + (idx + 1));
        lblMensaje.setForeground(new Color(200, 100, 0));
        // Cuando esté el socket: enviar mensaje de dislike al servidor
    }

    // Llamado desde socket cuando llega nueva publicacion
    public void recibirPublicacion(String nombreArtista, String mensaje) {
        modeloFeed.addElement("[" + nombreArtista + "] " + mensaje + " | Likes: 0");
        if (!modeloArtistas.contains(nombreArtista)) {
            modeloArtistas.addElement(nombreArtista);
        }
        lblNotificacion.setText("Nueva publicacion de " + nombreArtista + "!");
    }

    // Llamado desde socket cuando artista sube de nivel
    public void notificarSubidaNivel(String nombreArtista, int nivel) {
        String msg = nombreArtista + " subio al nivel " + nivel + "!";
        lblNotificacion.setText(msg);
        JOptionPane.showMessageDialog(this, msg, "Notificacion",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    // Llamado desde socket cuando publicacion llega a milestone de likes
    public void notificarMilestoneLikes(String nombreArtista, String mensaje, int likes) {
        String msg = "La publicacion de " + nombreArtista + " llego a " + likes + " likes!";
        lblNotificacion.setText(msg);
        JOptionPane.showMessageDialog(this, msg, "Notificacion",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    private JList<String> lstFeed;
    private JList<String> lstArtistas;
    private JLabel lblMensaje;
    private JLabel lblNotificacion;
}

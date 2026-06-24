/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

/**
 *
 * @author kiarabox
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import RedSocial.ArtistaForm;
import RedSocial.SeguidorForm;
import Subastas.SubastadorForm;
import Subastas.OferenteForm;

public class LoginForm extends javax.swing.JFrame {

    public LoginForm() {
        initComponents();
        setTitle("Red Social VIP & Subastas");
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(450, 380);
        getContentPane().setBackground(new Color(30, 30, 60));

        JLabel lblTitulo = new JLabel("BIENVENIDO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(75, 20, 300, 45);
        add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Red Social VIP & Subastas", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(180, 180, 255));
        lblSubtitulo.setBounds(75, 65, 300, 25);
        add(lblSubtitulo);

        JLabel lblNick = new JLabel("Nick / Nombre:");
        lblNick.setFont(new Font("Arial", Font.BOLD, 13));
        lblNick.setForeground(Color.WHITE);
        lblNick.setBounds(60, 115, 130, 25);
        add(lblNick);

        txtNick = new JTextField();
        txtNick.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNick.setBounds(190, 115, 200, 30);
        add(txtNick);

        JLabel lblRol = new JLabel("Tipo de usuario:");
        lblRol.setFont(new Font("Arial", Font.BOLD, 13));
        lblRol.setForeground(Color.WHITE);
        lblRol.setBounds(60, 165, 130, 25);
        add(lblRol);

        String[] roles = {"Subastador", "Oferente", "Artista", "Seguidor"};
        cmbRol = new JComboBox<>(roles);
        cmbRol.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbRol.setBounds(190, 165, 200, 30);
        add(cmbRol);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(130, 230, 180, 45);
        btnEntrar.setBackground(new Color(100, 50, 200));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setBorderPainted(false);
        btnEntrar.addActionListener(e -> entrar());
        add(btnEntrar);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 12));
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setBounds(60, 290, 330, 25);
        add(lblMensaje);
    }

    private void entrar() {
        String nick = txtNick.getText().trim();
        if (nick.isEmpty()) {
            lblMensaje.setText("Ingresa tu nick o nombre!");
            return;
        }

        String rol = (String) cmbRol.getSelectedItem();

        switch (rol) {
            case "Subastador":
                new SubastadorForm(nick).setVisible(true);
                break;
            case "Oferente":
                new OferenteForm(nick).setVisible(true);
                break;
            case "Artista":
                new ArtistaForm(nick).setVisible(true);
                break;
            case "Seguidor":
                new SeguidorForm(nick).setVisible(true);
                break;
        }
        this.dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }

    private JTextField txtNick;
    private JComboBox<String> cmbRol;
    private JLabel lblMensaje;
}

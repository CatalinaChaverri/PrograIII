/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subastas;

/**
 *
 * @author kiarabox
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class OferenteForm extends javax.swing.JFrame {

    private Oferente oferente;
    private Subasta subastaActual;

    public OferenteForm(String nick) {
        this.oferente = new Oferente(nick);
        initComponents();
        setTitle("Oferente: " + nick);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(500, 450);

        JLabel lblTitulo = new JLabel("PANEL OFERENTE", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(150, 50, 50));
        lblTitulo.setBounds(100, 10, 300, 35);
        add(lblTitulo);

        JLabel lblNick = new JLabel("Nick: " + oferente.getNick());
        lblNick.setFont(new Font("Arial", Font.PLAIN, 13));
        lblNick.setBounds(20, 50, 200, 25);
        add(lblNick);

        // Info subasta
        lblInfoSubasta = new JLabel("Sin subasta activa");
        lblInfoSubasta.setFont(new Font("Arial", Font.BOLD, 13));
        lblInfoSubasta.setBounds(20, 85, 450, 25);
        add(lblInfoSubasta);

        lblPrecioActual = new JLabel("Precio actual: --");
        lblPrecioActual.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecioActual.setForeground(new Color(0, 100, 0));
        lblPrecioActual.setBounds(20, 115, 300, 30);
        add(lblPrecioActual);

        lblEstado = new JLabel("");
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstado.setBounds(20, 150, 450, 25);
        add(lblEstado);

        // Hacer oferta
        JLabel lblOfertaTit = new JLabel("--- Hacer Oferta ---");
        lblOfertaTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblOfertaTit.setBounds(20, 190, 200, 25);
        add(lblOfertaTit);

        JLabel lblMonto = new JLabel("Monto ($):");
        lblMonto.setBounds(20, 220, 100, 25);
        add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(120, 220, 150, 25);
        add(txtMonto);

        JButton btnOfertar = new JButton("Ofertar");
        btnOfertar.setBounds(280, 220, 100, 25);
        btnOfertar.setBackground(new Color(50, 100, 200));
        btnOfertar.setForeground(Color.WHITE);
        btnOfertar.setFocusPainted(false);
        btnOfertar.setOpaque(true);
        btnOfertar.setBorderPainted(false);
        btnOfertar.addActionListener(e -> hacerOferta());
        add(btnOfertar);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setBounds(20, 270, 450, 30);
        add(lblMensaje);

        lblGanador = new JLabel("", SwingConstants.CENTER);
        lblGanador.setFont(new Font("Arial", Font.BOLD, 16));
        lblGanador.setForeground(new Color(200, 150, 0));
        lblGanador.setBounds(20, 320, 450, 35);
        add(lblGanador);
    }

    private void hacerOferta() {
        if (subastaActual == null) {
            lblMensaje.setText("No hay subasta activa!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        if (subastaActual.getEstado() != EstadoSubasta.ABIERTA) {
            lblMensaje.setText("La subasta ya no está abierta!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        String montoStr = txtMonto.getText().trim();
        if (montoStr.isEmpty()) {
            lblMensaje.setText("Ingresa un monto!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        try {
            double monto = Double.parseDouble(montoStr);
            if (monto <= subastaActual.getPrecioActual()) {
                lblMensaje.setText("El monto debe ser mayor al precio actual: $" +
                                   subastaActual.getPrecioActual());
                lblMensaje.setForeground(Color.RED);
                return;
            }
            Oferta oferta = oferente.hacerOferta(monto);
            subastaActual.aceptarOferta(oferta);
            actualizarInfo();
            lblMensaje.setText("Oferta de $" + monto + " enviada!");
            lblMensaje.setForeground(new Color(0, 120, 0));
            txtMonto.setText("");
        } catch (NumberFormatException e) {
            lblMensaje.setText("El monto debe ser un número!");
            lblMensaje.setForeground(Color.RED);
        }
    }

    // Llamado desde socket cuando llega actualización
    public void actualizarSubasta(Subasta subasta) {
        this.subastaActual = subasta;
        actualizarInfo();
    }

    // Llamado cuando la subasta se cierra
    public void notificarCierre(String mensajeFelicitacion) {
        lblGanador.setText(mensajeFelicitacion);
        JOptionPane.showMessageDialog(this, mensajeFelicitacion,
                                      "Subasta Cerrada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarInfo() {
        if (subastaActual == null) {
            lblInfoSubasta.setText("Sin subasta activa");
            lblPrecioActual.setText("Precio actual: --");
            lblEstado.setText("");
        } else {
            lblInfoSubasta.setText("Subasta: " + subastaActual.getNombre() +
                                   " — Producto: " + subastaActual.getProducto().getNombre());
            lblPrecioActual.setText("Precio actual: $" + subastaActual.getPrecioActual());
            lblEstado.setText("Estado: " + subastaActual.getEstado());
        }
    }

    private JTextField txtMonto;
    private JLabel lblInfoSubasta;
    private JLabel lblPrecioActual;
    private JLabel lblEstado;
    private JLabel lblMensaje;
    private JLabel lblGanador;
}

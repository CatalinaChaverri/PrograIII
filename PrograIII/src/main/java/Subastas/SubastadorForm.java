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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SubastadorForm extends javax.swing.JFrame {

    private Subastador subastador;
    private Subasta subastaActual;
    private DefaultListModel<String> modeloOfertas;

    public SubastadorForm(String nick) {
        this.subastador = new Subastador(nick);
        initComponents();
        setTitle("Subastador: " + nick);
        modeloOfertas = new DefaultListModel<>();
        lstOfertas.setModel(modeloOfertas);
        actualizarEstado();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(600, 550);
        setBackground(new Color(245, 245, 245));

        JLabel lblTitulo = new JLabel("PANEL SUBASTADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(50, 50, 150));
        lblTitulo.setBounds(150, 10, 300, 35);
        add(lblTitulo);

        JLabel lblNick = new JLabel("Nick: " + subastador.getNick(), SwingConstants.LEFT);
        lblNick.setFont(new Font("Arial", Font.PLAIN, 13));
        lblNick.setBounds(20, 50, 200, 25);
        add(lblNick);

        // Crear subasta
        JLabel lblCrear = new JLabel("--- Crear Subasta ---", SwingConstants.LEFT);
        lblCrear.setFont(new Font("Arial", Font.BOLD, 13));
        lblCrear.setBounds(20, 85, 200, 25);
        add(lblCrear);

        JLabel lblNombreP = new JLabel("Producto:");
        lblNombreP.setBounds(20, 115, 100, 25);
        add(lblNombreP);
        txtNombreProducto = new JTextField();
        txtNombreProducto.setBounds(120, 115, 150, 25);
        add(txtNombreProducto);

        JLabel lblDescP = new JLabel("Descripción:");
        lblDescP.setBounds(20, 145, 100, 25);
        add(lblDescP);
        txtDescProducto = new JTextField();
        txtDescProducto.setBounds(120, 145, 150, 25);
        add(txtDescProducto);

        JLabel lblPrecio = new JLabel("Precio inicial:");
        lblPrecio.setBounds(20, 175, 100, 25);
        add(lblPrecio);
        txtPrecioInicial = new JTextField();
        txtPrecioInicial.setBounds(120, 175, 150, 25);
        add(txtPrecioInicial);

        JLabel lblNombreS = new JLabel("Nombre subasta:");
        lblNombreS.setBounds(20, 205, 120, 25);
        add(lblNombreS);
        txtNombreSubasta = new JTextField();
        txtNombreSubasta.setBounds(140, 205, 130, 25);
        add(txtNombreSubasta);

        JButton btnCrear = new JButton("Crear Subasta");
        btnCrear.setBounds(20, 240, 140, 35);
        btnCrear.setBackground(new Color(50, 150, 50));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setOpaque(true);
        btnCrear.setBorderPainted(false);
        btnCrear.addActionListener(e -> crearSubasta());
        add(btnCrear);

        // Estado subasta actual
        JLabel lblEstadoTit = new JLabel("--- Subasta Actual ---", SwingConstants.LEFT);
        lblEstadoTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstadoTit.setBounds(300, 85, 200, 25);
        add(lblEstadoTit);

        lblEstadoSubasta = new JLabel("Sin subasta activa");
        lblEstadoSubasta.setFont(new Font("Arial", Font.PLAIN, 12));
        lblEstadoSubasta.setBounds(300, 115, 270, 25);
        add(lblEstadoSubasta);

        lblPrecioActual = new JLabel("Precio actual: --");
        lblPrecioActual.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrecioActual.setForeground(new Color(0, 100, 0));
        lblPrecioActual.setBounds(300, 145, 270, 25);
        add(lblPrecioActual);

        // Lista de ofertas
        JLabel lblOfertasTit = new JLabel("Ofertas recibidas:");
        lblOfertasTit.setFont(new Font("Arial", Font.BOLD, 12));
        lblOfertasTit.setBounds(300, 175, 200, 25);
        add(lblOfertasTit);

        lstOfertas = new JList<>();
        JScrollPane scrollOfertas = new JScrollPane(lstOfertas);
        scrollOfertas.setBounds(300, 200, 270, 150);
        add(scrollOfertas);

        // Botón aceptar oferta seleccionada
        JButton btnAceptar = new JButton("Aceptar Oferta Seleccionada");
        btnAceptar.setBounds(300, 360, 270, 35);
        btnAceptar.setBackground(new Color(50, 100, 200));
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setOpaque(true);
        btnAceptar.setBorderPainted(false);
        btnAceptar.addActionListener(e -> aceptarOfertaSeleccionada());
        add(btnAceptar);

        // Botones cerrar y cancelar
        JButton btnCerrar = new JButton("Cerrar Subasta");
        btnCerrar.setBounds(20, 290, 140, 35);
        btnCerrar.setBackground(new Color(200, 150, 0));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setOpaque(true);
        btnCerrar.setBorderPainted(false);
        btnCerrar.addActionListener(e -> cerrarSubasta());
        add(btnCerrar);

        JButton btnCancelar = new JButton("Cancelar Subasta");
        btnCancelar.setBounds(20, 340, 140, 35);
        btnCancelar.setBackground(new Color(200, 50, 50));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setOpaque(true);
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(e -> cancelarSubasta());
        add(btnCancelar);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setBounds(20, 400, 550, 30);
        add(lblMensaje);
    }

    private void crearSubasta() {
        String nombreProducto = txtNombreProducto.getText().trim();
        String descProducto   = txtDescProducto.getText().trim();
        String nombreSubasta  = txtNombreSubasta.getText().trim();
        String precioStr      = txtPrecioInicial.getText().trim();

        if (nombreProducto.isEmpty() || descProducto.isEmpty() ||
            nombreSubasta.isEmpty() || precioStr.isEmpty()) {
            lblMensaje.setText("Completa todos los campos!");
            lblMensaje.setForeground(Color.RED);
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            Producto producto = new Producto(nombreProducto, descProducto, "", precio);
            subastaActual = subastador.crearSubasta(nombreSubasta, "", "", producto);
            modeloOfertas.clear();
            actualizarEstado();
            lblMensaje.setText("Subasta '" + nombreSubasta + "' creada!");
            lblMensaje.setForeground(new Color(0, 120, 0));
        } catch (NumberFormatException e) {
            lblMensaje.setText("El precio debe ser un número!");
            lblMensaje.setForeground(Color.RED);
        }
    }

    private void aceptarOfertaSeleccionada() {
        if (subastaActual == null) {
            lblMensaje.setText("No hay subasta activa!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        int idx = lstOfertas.getSelectedIndex();
        if (idx < 0) {
            lblMensaje.setText("Selecciona una oferta de la lista!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        Oferta oferta = subastaActual.getOfertas().size() > 0 ?
                        subastaActual.getOfertas().get(idx) : null;
        if (oferta != null) {
            boolean aceptada = subastador.aceptarOferta(subastaActual, oferta);
            if (aceptada) {
                actualizarEstado();
                lblMensaje.setText("Oferta de $" + oferta.getMonto() + " aceptada!");
                lblMensaje.setForeground(new Color(0, 120, 0));
            } else {
                lblMensaje.setText("No se pudo aceptar la oferta!");
                lblMensaje.setForeground(Color.RED);
            }
        }
    }

    // Metodo para agregar oferta desde socket
    public void recibirOferta(Oferta oferta) {
        if (subastaActual != null) {
            subastaActual.getOfertas().add(oferta);
            modeloOfertas.addElement(oferta.getOferente().getNick() +
                                     " — $" + oferta.getMonto());
            actualizarEstado();
        }
    }

    private void cerrarSubasta() {
        if (subastaActual == null) {
            lblMensaje.setText("No hay subasta activa!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        subastador.cerrarSubasta(subastaActual);
        String msg = subastaActual.getGanador() != null ?
            subastador.mensajeFelicitacion(subastaActual.getGanador(), subastaActual) :
            "Subasta cerrada sin ganador.";
        actualizarEstado();
        JOptionPane.showMessageDialog(this, msg, "Subasta Cerrada",
                                      JOptionPane.INFORMATION_MESSAGE);
        lblMensaje.setText(msg);
        lblMensaje.setForeground(new Color(0, 100, 200));
    }

    private void cancelarSubasta() {
        if (subastaActual == null) {
            lblMensaje.setText("No hay subasta activa!");
            lblMensaje.setForeground(Color.RED);
            return;
        }
        subastador.cancelarSubasta(subastaActual);
        actualizarEstado();
        lblMensaje.setText("Subasta cancelada.");
        lblMensaje.setForeground(Color.RED);
    }

    private void actualizarEstado() {
        if (subastaActual == null) {
            lblEstadoSubasta.setText("Sin subasta activa");
            lblPrecioActual.setText("Precio actual: --");
        } else {
            lblEstadoSubasta.setText(subastaActual.getNombre() +
                                     " [" + subastaActual.getEstado() + "]");
            lblPrecioActual.setText("Precio actual: $" + subastaActual.getPrecioActual());
        }
    }

    // Variables
    private JTextField txtNombreProducto;
    private JTextField txtDescProducto;
    private JTextField txtPrecioInicial;
    private JTextField txtNombreSubasta;
    private JLabel lblEstadoSubasta;
    private JLabel lblPrecioActual;
    private JLabel lblMensaje;
    private JList<String> lstOfertas;
}

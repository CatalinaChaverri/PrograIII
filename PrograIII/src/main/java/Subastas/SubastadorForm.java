package Subastas;

import Cliente.Cliente;
import Modelos.Message;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class SubastadorForm extends javax.swing.JFrame {

    private final Subastador subastador;
    private Subasta subastaActual;
    private final DefaultListModel<String> modeloOfertas;
    private final List<Oferta> ofertasPendientes;
    private Cliente cliente;
    private Timer timerSubasta;
    private int segundosRestantes;
    private JLabel lblTimer;

    public SubastadorForm(String nick) {
        this.subastador = new Subastador(nick);
        this.modeloOfertas = new DefaultListModel<>();
        this.ofertasPendientes = new ArrayList<>();
        initComponents();
        setTitle("Subastador: " + nick);
        lstOfertas.setModel(modeloOfertas);
        cliente = new Cliente(nick, this::procesarMensaje);
        actualizarEstado();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(600, 550);

        JLabel lblTitulo = new JLabel("PANEL SUBASTADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(50, 50, 150));
        lblTitulo.setBounds(150, 10, 300, 35);
        add(lblTitulo);

        JLabel lblNick = new JLabel("Nick: " + subastador.getNick());
        lblNick.setBounds(20, 50, 220, 25);
        add(lblNick);

        JLabel lblCrear = new JLabel("--- Crear Subasta ---");
        lblCrear.setFont(new Font("Arial", Font.BOLD, 13));
        lblCrear.setBounds(20, 85, 200, 25);
        add(lblCrear);

        addLabel("Producto:", 20, 115);
        txtNombreProducto = field(120, 115, 150);
        addLabel("Descripcion:", 20, 145);
        txtDescProducto = field(120, 145, 150);
        addLabel("Precio inicial:", 20, 175);
        txtPrecioInicial = field(120, 175, 150);
        addLabel("Nombre subasta:", 20, 205);
        txtNombreSubasta = field(140, 205, 130);

        JButton btnCrear = button("Crear Subasta", 20, 240, 140, new Color(50, 150, 50));
        btnCrear.addActionListener(e -> crearSubasta());
        add(btnCrear);

        JButton btnCerrar = button("Cerrar Subasta", 20, 290, 140, new Color(200, 150, 0));
        btnCerrar.addActionListener(e -> cerrarSubasta());
        add(btnCerrar);

        JButton btnCancelar = button("Cancelar Subasta", 20, 340, 140, new Color(200, 50, 50));
        btnCancelar.addActionListener(e -> cancelarSubasta());
        add(btnCancelar);

        JLabel lblEstadoTit = new JLabel("--- Subasta Actual ---");
        lblEstadoTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstadoTit.setBounds(300, 85, 200, 25);
        add(lblEstadoTit);

        lblEstadoSubasta = new JLabel("Sin subasta activa");
        lblEstadoSubasta.setBounds(300, 115, 270, 25);
        add(lblEstadoSubasta);

        lblPrecioActual = new JLabel("Precio actual: --");
        lblPrecioActual.setFont(new Font("Arial", Font.BOLD, 13));
        lblPrecioActual.setForeground(new Color(0, 100, 0));
        lblPrecioActual.setBounds(300, 145, 270, 25);
        add(lblPrecioActual);
        
        lblTimer = new JLabel("Tiempo restante: --", SwingConstants.LEFT);
        lblTimer.setFont(new Font("Arial", Font.BOLD, 13));
        lblTimer.setForeground(new Color(180, 80, 0));
        lblTimer.setBounds(300, 170, 270, 25);
        add(lblTimer);

        JLabel lblOfertasTit = new JLabel("Ofertas recibidas:");
        lblOfertasTit.setFont(new Font("Arial", Font.BOLD, 12));
        lblOfertasTit.setBounds(300, 175, 200, 25);
        add(lblOfertasTit);

        lstOfertas = new JList<>();
        JScrollPane scrollOfertas = new JScrollPane(lstOfertas);
        scrollOfertas.setBounds(300, 200, 270, 150);
        add(scrollOfertas);

        JButton btnAceptar = button("Aceptar Oferta Seleccionada", 300, 360, 270, new Color(50, 100, 200));
        btnAceptar.addActionListener(e -> aceptarOfertaSeleccionada());
        add(btnAceptar);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setBounds(20, 400, 550, 30);
        add(lblMensaje);
    }

    private void crearSubasta() {
        String nombreProducto = txtNombreProducto.getText().trim();
        String descProducto = txtDescProducto.getText().trim();
        String nombreSubasta = txtNombreSubasta.getText().trim();
        String precioStr = txtPrecioInicial.getText().trim();

        if (nombreProducto.isEmpty() || descProducto.isEmpty() || nombreSubasta.isEmpty() || precioStr.isEmpty()) {
            mostrar("Completa todos los campos!", Color.RED);
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            Producto producto = new Producto(nombreProducto, descProducto, "", precio);
            subastaActual = subastador.crearSubasta(nombreSubasta, "", "", producto);
            modeloOfertas.clear();
            ofertasPendientes.clear();
            cliente.suscribir(temaSubasta());
            cliente.escribirMensaje(new Message(subastador.getNick(), temaSubasta(),
                    "Nueva subasta: " + nombreSubasta, Message.Tipo.NUEVA_SUBASTA,
                    nombreSubasta + "|" + nombreProducto + "|" + descProducto + "|" + precio));
            actualizarEstado();
            mostrar("Subasta '" + nombreSubasta + "' creada!", new Color(0, 120, 0));
            iniciarTimer(60);
        } catch (NumberFormatException e) {
            mostrar("El precio debe ser un numero!", Color.RED);
        }
    }

    private void aceptarOfertaSeleccionada() {
        if (subastaActual == null) {
            mostrar("No hay subasta activa!", Color.RED);
            return;
        }
        int idx = lstOfertas.getSelectedIndex();
        if (idx < 0 || idx >= ofertasPendientes.size()) {
            mostrar("Selecciona una oferta de la lista!", Color.RED);
            return;
        }
        Oferta oferta = ofertasPendientes.get(idx);
        if (subastador.aceptarOferta(subastaActual, oferta)) {
            ofertasPendientes.remove(idx);
            modeloOfertas.remove(idx);
            cliente.escribirMensaje(new Message(subastador.getNick(), temaSubasta(),
                    "Oferta aceptada", Message.Tipo.OFERTA_ACEPTADA,
                    oferta.getOferente().getNick() + "|" + oferta.getMonto()));
            actualizarEstado();
            mostrar("Oferta de $" + oferta.getMonto() + " aceptada!", new Color(0, 120, 0));
        } else {
            mostrar("No se pudo aceptar la oferta!", Color.RED);
        }
    }

    private void recibirOferta(Oferta oferta) {
        if (subastaActual == null) {
            return;
        }
        ofertasPendientes.add(oferta);
        modeloOfertas.addElement(oferta.getOferente().getNick() + " - $" + oferta.getMonto());
        mostrar("Nueva oferta de " + oferta.getOferente().getNick(), new Color(0, 100, 200));
    }

    private void cerrarSubasta() {
        if (subastaActual == null) {
            mostrar("No hay subasta activa!", Color.RED);
            return;
        }
        subastador.cerrarSubasta(subastaActual);
        String msg = subastaActual.getGanador() != null
                ? subastador.mensajeFelicitacion(subastaActual.getGanador(), subastaActual)
                : "Subasta cerrada sin ganador.";
        cliente.escribirMensaje(new Message(subastador.getNick(), temaSubasta(), msg,
                Message.Tipo.SUBASTA_CERRADA,
                (subastaActual.getGanador() != null ? subastaActual.getGanador().getNick() : "")
                + "|" + subastaActual.getPrecioActual()));
        actualizarEstado();
        JOptionPane.showMessageDialog(this, msg, "Subasta Cerrada", JOptionPane.INFORMATION_MESSAGE);
        mostrar(msg, new Color(0, 100, 200));
    }

    private void cancelarSubasta() {
        if (subastaActual == null) {
            mostrar("No hay subasta activa!", Color.RED);
            return;
        }
        subastador.cancelarSubasta(subastaActual);
        cliente.escribirMensaje(new Message(subastador.getNick(), temaSubasta(), "Subasta cancelada",
                Message.Tipo.SUBASTA_CANCELADA));
        actualizarEstado();
        mostrar("Subasta cancelada.", Color.RED);
    }

    private void procesarMensaje(Message mensaje) {
        if (mensaje.tipo == Message.Tipo.NUEVA_OFERTA && subastaActual != null) {
            try {
                recibirOferta(new Oferta(Double.parseDouble(mensaje.datos), new Oferente(mensaje.emisor)));
            } catch (NumberFormatException ex) {
                mostrar("Oferta invalida recibida.", Color.RED);
            }
        }
    }

    private void actualizarEstado() {
        if (subastaActual == null) {
            lblEstadoSubasta.setText("Sin subasta activa");
            lblPrecioActual.setText("Precio actual: --");
        } else {
            lblEstadoSubasta.setText(subastaActual.getNombre() + " [" + subastaActual.getEstado() + "]");
            lblPrecioActual.setText("Precio actual: $" + subastaActual.getPrecioActual());
        }
    }
    
    private void iniciarTimer(int segundos) {
    if (timerSubasta != null && timerSubasta.isRunning()) {
        timerSubasta.stop();
    }
    segundosRestantes = segundos;
    lblTimer.setForeground(new Color(180, 80, 0));
    lblTimer.setText("Tiempo restante: " + segundosRestantes + "s");

    timerSubasta = new Timer(1000, e -> {
        segundosRestantes--;
        lblTimer.setText("Tiempo restante: " + segundosRestantes + "s");

        if (segundosRestantes <= 10) {
            lblTimer.setForeground(Color.RED);
        }
        if (segundosRestantes <= 0) {
            timerSubasta.stop();
            lblTimer.setText("Tiempo agotado!");
            // Cerrar subasta automaticamente
            if (subastaActual != null && subastaActual.getEstado() == EstadoSubasta.ABIERTA) {
                cerrarSubasta();
            }
        }
    });
    timerSubasta.start();
    }

    private String temaSubasta() {
        return "subasta:" + subastaActual.getNombre();
    }

    private void mostrar(String texto, Color color) {
        lblMensaje.setText(texto);
        lblMensaje.setForeground(color);
    }

    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 120, 25);
        add(label);
    }

    private JTextField field(int x, int y, int width) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, 25);
        add(textField);
        return textField;
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

    private JTextField txtNombreProducto;
    private JTextField txtDescProducto;
    private JTextField txtPrecioInicial;
    private JTextField txtNombreSubasta;
    private JLabel lblEstadoSubasta;
    private JLabel lblPrecioActual;
    private JLabel lblMensaje;
    private JList<String> lstOfertas;
}

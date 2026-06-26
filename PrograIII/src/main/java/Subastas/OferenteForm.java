package Subastas;

import Cliente.Cliente;
import Modelos.Message;
import static Modelos.Message.Tipo.NUEVA_SUBASTA;
import static Modelos.Message.Tipo.OFERTA_ACEPTADA;
import static Modelos.Message.Tipo.SUBASTA_CANCELADA;
import static Modelos.Message.Tipo.SUBASTA_CERRADA;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class OferenteForm extends javax.swing.JFrame {

    private final Oferente oferente;
    private Cliente cliente;
    private String subastaActual;
    private double precioActual;
    private EstadoSubasta estadoActual = EstadoSubasta.ABIERTA;

    public OferenteForm(String nick) {
        this.oferente = new Oferente(nick);
        initComponents();
        setTitle("Oferente: " + nick);
        cliente = new Cliente(nick, this::procesarMensaje);
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
        lblNick.setBounds(20, 50, 200, 25);
        add(lblNick);

        JLabel lblUnirse = new JLabel("Subasta:");
        lblUnirse.setBounds(20, 85, 80, 25);
        add(lblUnirse);

        txtSubasta = new JTextField();
        txtSubasta.setBounds(100, 85, 180, 25);
        add(txtSubasta);

        JButton btnUnirse = button("Unirse", 290, 85, 100, new Color(80, 120, 190));
        btnUnirse.addActionListener(e -> unirseSubasta());
        add(btnUnirse);

        lblInfoSubasta = new JLabel("Sin subasta activa");
        lblInfoSubasta.setFont(new Font("Arial", Font.BOLD, 13));
        lblInfoSubasta.setBounds(20, 125, 450, 25);
        add(lblInfoSubasta);

        lblPrecioActual = new JLabel("Precio actual: --");
        lblPrecioActual.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecioActual.setForeground(new Color(0, 100, 0));
        lblPrecioActual.setBounds(20, 155, 300, 30);
        add(lblPrecioActual);

        lblEstado = new JLabel("");
        lblEstado.setBounds(20, 190, 450, 25);
        add(lblEstado);

        JLabel lblOfertaTit = new JLabel("--- Hacer Oferta ---");
        lblOfertaTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblOfertaTit.setBounds(20, 230, 200, 25);
        add(lblOfertaTit);

        JLabel lblMonto = new JLabel("Monto ($):");
        lblMonto.setBounds(20, 260, 100, 25);
        add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(120, 260, 150, 25);
        add(txtMonto);

        JButton btnOfertar = button("Ofertar", 280, 260, 100, new Color(50, 100, 200));
        btnOfertar.addActionListener(e -> hacerOferta());
        add(btnOfertar);

        lblMensaje = new JLabel("", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 13));
        lblMensaje.setBounds(20, 310, 450, 30);
        add(lblMensaje);

        lblGanador = new JLabel("", SwingConstants.CENTER);
        lblGanador.setFont(new Font("Arial", Font.BOLD, 16));
        lblGanador.setForeground(new Color(200, 150, 0));
        lblGanador.setBounds(20, 355, 450, 35);
        add(lblGanador);
    }

        private void unirseSubasta() {
        String nombre = txtSubasta.getText().trim();
        if (nombre.isEmpty()) {
            mostrar("Ingresa el nombre de la subasta!", Color.RED);
            return;
        }
        // Primero suscribirse al tema
        subastaActual = nombre;
        cliente.suscribir(temaSubasta());

        // Enviar mensaje pidiendo info de la subasta para validar que existe
        cliente.escribirMensaje(new Message(oferente.getNick(), temaSubasta(),
                "Oferente unido", Message.Tipo.UNIRSE_SUBASTA));

        // Mostrar mensaje de espera hasta recibir confirmacion del servidor
        mostrar("Buscando subasta '" + nombre + "'...", new Color(0, 100, 200));
    }



    private void hacerOferta() {
        if (subastaActual == null) {
            mostrar("Primero unite a una subasta!", Color.RED);
            return;
        }
        if (estadoActual != EstadoSubasta.ABIERTA) {
            mostrar("La subasta ya no esta abierta!", Color.RED);
            return;
        }
        try {
            double monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= precioActual) {
                mostrar("El monto debe ser mayor al precio actual: $" + precioActual, Color.RED);
                return;
            }
            Oferta oferta = oferente.hacerOferta(monto);
            cliente.escribirMensaje(new Message(oferente.getNick(), temaSubasta(),
                    "Nueva oferta", Message.Tipo.NUEVA_OFERTA,
                    String.valueOf(oferta.getMonto())));
            mostrar("Oferta de $" + monto + " enviada!", new Color(0, 120, 0));
            txtMonto.setText("");
        } catch (NumberFormatException e) {
            mostrar("El monto debe ser un numero!", Color.RED);
        }
    }

    private void procesarMensaje(Message mensaje) {
        switch (mensaje.tipo) {
            case NUEVA_SUBASTA:
                aplicarNuevaSubasta(mensaje);
                break;
            case OFERTA_ACEPTADA:
                aplicarOfertaAceptada(mensaje);
                break;
            case SUBASTA_CERRADA:
                estadoActual = EstadoSubasta.VENDIDA;
                actualizarInfo();
                notificarCierre(mensaje.mensaje);
                break;
            case SUBASTA_CANCELADA:
                estadoActual = EstadoSubasta.CANCELADA;
                actualizarInfo();
                mostrar("Subasta cancelada.", Color.RED);
                break;
            default:
                break;
        }
    }

    private void aplicarNuevaSubasta(Message mensaje) {
        String[] partes = mensaje.datos != null ? mensaje.datos.split("\\|", -1) : new String[0];
        if (partes.length >= 4 && (subastaActual == null || partes[0].equals(subastaActual))) {
            subastaActual = partes[0];
            precioActual  = Double.parseDouble(partes[3]);
            estadoActual  = EstadoSubasta.ABIERTA;
            actualizarInfo();
            // Mostrar precio inicial al unirse
            mostrar("Te uniste a '" + subastaActual + "'! Precio inicial: $" + precioActual,
                    new Color(0, 120, 0));
        } else {
            // La subasta no existe o no coincide
            subastaActual = null;
            mostrar("La subasta '" + txtSubasta.getText().trim() + "' no existe!", Color.RED);
            lblInfoSubasta.setText("Sin subasta activa");
            lblPrecioActual.setText("Precio actual: --");
            lblEstado.setText("");
        }
    }

    private void aplicarOfertaAceptada(Message mensaje) {
        String[] partes = mensaje.datos != null ? mensaje.datos.split("\\|", -1) : new String[0];
        if (partes.length >= 2) {
            precioActual = Double.parseDouble(partes[1]);
            actualizarInfo();
            if (oferente.getNick().equals(partes[0])) {
                mostrar("Tu oferta fue aceptada!", new Color(0, 120, 0));
            } else {
                mostrar("Nuevo precio aceptado: $" + precioActual, new Color(0, 100, 200));
            }
        }
    }

    private void notificarCierre(String mensajeFelicitacion) {
        lblGanador.setText(mensajeFelicitacion);
        JOptionPane.showMessageDialog(this, mensajeFelicitacion, "Subasta Cerrada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarInfo() {
        if (subastaActual == null) {
            lblInfoSubasta.setText("Sin subasta activa");
            lblPrecioActual.setText("Precio actual: --");
            lblEstado.setText("");
        } else {
            lblInfoSubasta.setText("Subasta: " + subastaActual);
            lblPrecioActual.setText("Precio actual: $" + precioActual);
            lblEstado.setText("Estado: " + estadoActual);
        }
    }

    private String temaSubasta() {
        return "subasta:" + subastaActual;
    }

    private void mostrar(String texto, Color color) {
        lblMensaje.setText(texto);
        lblMensaje.setForeground(color);
    }

    private JButton button(String text, int x, int y, int width, Color color) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, width, 25);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private JTextField txtSubasta;
    private JTextField txtMonto;
    private JLabel lblInfoSubasta;
    private JLabel lblPrecioActual;
    private JLabel lblEstado;
    private JLabel lblMensaje;
    private JLabel lblGanador;
}

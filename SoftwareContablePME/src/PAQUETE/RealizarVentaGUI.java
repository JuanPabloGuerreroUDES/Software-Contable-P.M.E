/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 *
 * @author juang
 */
public class RealizarVentaGUI extends JFrame {
    private List<Producto> productos;
    private List<Cliente> clientes;
    private JTable tablaVenta;
    private DefaultTableModel modeloTablaVenta;
    private GestorDeInventario gestorInventario;
    private GestorDeClientes gestorClientes;
    private Cliente clienteActual;

    public RealizarVentaGUI() {
        super("Realizar Venta");
        this.productos = new GestorDeInventario().getProductos();
        this.clientes = new GestorDeClientes().getClientes();
        this.gestorInventario = new GestorDeInventario(productos);
        this.gestorClientes = new GestorDeClientes(clientes);

        inicializarComponentes();

        // Cargar productos y clientes desde el archivo CSV al iniciar
        cargarDatos();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Guardar productos y clientes en el archivo CSV al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarYSalir();
            }
        });
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel de búsqueda de cliente
        JPanel panelCliente = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel labelDocumento = new JLabel("Documento del Cliente:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCliente.add(labelDocumento, gbc);

        JTextField campoDocumento = new JTextField(10);
        gbc.gridx = 1;
        panelCliente.add(campoDocumento, gbc);

        JButton botonBuscarCliente = new JButton("Buscar Cliente");
        gbc.gridx = 2;
        panelCliente.add(botonBuscarCliente, gbc);

        JButton botonAgregarCliente = new JButton("Nuevo Cliente");
        gbc.gridx = 3;
        panelCliente.add(botonAgregarCliente, gbc);

        add(panelCliente, BorderLayout.NORTH);

        // Modelo de la tabla de ventas
        String[] columnasVenta = {"ID", "Nombre", "Cantidad", "Precio"};
        modeloTablaVenta = new DefaultTableModel(columnasVenta, 0);
        tablaVenta = new JTable(modeloTablaVenta);

        // Panel para la tabla de ventas
        add(new JScrollPane(tablaVenta), BorderLayout.CENTER);

        // Panel de productos
        JPanel panelProducto = new JPanel(new GridBagLayout());

        JLabel labelIdProducto = new JLabel("ID del Producto:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelProducto.add(labelIdProducto, gbc);

        JTextField campoIdProducto = new JTextField(10);
        gbc.gridx = 1;
        panelProducto.add(campoIdProducto, gbc);

        JLabel labelCantidad = new JLabel("Cantidad:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelProducto.add(labelCantidad, gbc);

        JTextField campoCantidad = new JTextField(10);
        gbc.gridx = 1;
        panelProducto.add(campoCantidad, gbc);

        JButton botonAgregarProducto = new JButton("Agregar Producto");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelProducto.add(botonAgregarProducto, gbc);

        add(panelProducto, BorderLayout.SOUTH);

        // Panel de confirmación de venta y volver al menú
        JPanel panelConfirmacion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonConfirmarVenta = new JButton("Confirmar Venta");
        panelConfirmacion.add(botonConfirmarVenta);
        JButton botonVolver = new JButton("Volver al Menú Principal");
        botonVolver.addActionListener(e -> {
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        });
        panelConfirmacion.add(botonVolver);
        add(panelConfirmacion, BorderLayout.EAST);

        // Funcionalidades del botón buscar cliente
        botonBuscarCliente.addActionListener(e -> {
            String documento = campoDocumento.getText();
            clienteActual = buscarClientePorIdentificacion(documento);
            if (clienteActual != null) {
                JOptionPane.showMessageDialog(null, "Cliente encontrado: " + clienteActual.getNombre(), "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado. Por favor, registre el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Funcionalidades del botón agregar cliente
        botonAgregarCliente.addActionListener(e -> {
            new AgregarClienteGUI().setVisible(true);
            dispose();
        });

        // Funcionalidades del botón agregar producto
        botonAgregarProducto.addActionListener(e -> {
            try {
                String idProducto = campoIdProducto.getText();
                int cantidad = Integer.parseInt(campoCantidad.getText());

                Producto producto = buscarProductoPorId(idProducto);
                if (producto == null) {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (cantidad > producto.getCantidad()) {
                    JOptionPane.showMessageDialog(null, "Cantidad no disponible", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                modeloTablaVenta.addRow(new Object[]{producto.getId(), producto.getNombre(), cantidad, producto.getPrecio() * cantidad});
                campoIdProducto.setText("");
                campoCantidad.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error en el formato de número", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Funcionalidades del botón confirmar venta
        botonConfirmarVenta.addActionListener(e -> {
            if (clienteActual == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente antes de confirmar la venta", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            confirmarVenta();
        });
    }

    private void cargarDatos() {
        gestorInventario.cargarProductosDesdeCSV("inventario.csv");
        gestorClientes.cargarClientesDesdeCSV("clientes.csv");
    }

    private Producto buscarProductoPorId(String id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return producto;
            }
        }
        return null;
    }

    private Cliente buscarClientePorIdentificacion(String identificacion) {
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equals(identificacion)) {
                return cliente;
            }
        }
        return null;
    }

    private void confirmarVenta() {
        int rowCount = modeloTablaVenta.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String idProducto = (String) modeloTablaVenta.getValueAt(i, 0);
            int cantidadVendida = (int) modeloTablaVenta.getValueAt(i, 2);

            Producto producto = buscarProductoPorId(idProducto);
            if (producto != null) {
                producto.setCantidad(producto.getCantidad() - cantidadVendida);
            }
        }

        // Guardar los cambios en el inventario
        gestorInventario.guardarProductosEnCSV("inventario.csv");

        JOptionPane.showMessageDialog(null, "Venta confirmada", "Información", JOptionPane.INFORMATION_MESSAGE);
        modeloTablaVenta.setRowCount(0);
    }

    private void guardarYSalir() {
        gestorInventario.guardarProductosEnCSV("inventario.csv");
        gestorClientes.guardarClientesEnCSV("clientes.csv");
        System.out.println("Datos guardados antes de salir.");
        new MenuPrincipalGUI().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new RealizarVentaGUI().setVisible(true));
    }
}
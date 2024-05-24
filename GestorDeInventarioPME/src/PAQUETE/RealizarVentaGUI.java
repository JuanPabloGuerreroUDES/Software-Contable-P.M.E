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
        setLocationRelativeTo(null); 
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

        // Etiqueta y campo para ID del Producto
        JLabel labelIdProducto = new JLabel("ID del Producto:");
        JTextField campoIdProducto = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelProducto.add(labelIdProducto, gbc);
        gbc.gridx = 1;
        panelProducto.add(campoIdProducto, gbc);

        // Etiqueta y campo para Nombre del Producto
        JLabel labelNombreProducto = new JLabel("Nombre del Producto:");
        JTextField campoNombreProducto = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelProducto.add(labelNombreProducto, gbc);
        gbc.gridx = 1;
        panelProducto.add(campoNombreProducto, gbc);

        // Etiqueta y campo para Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        JTextField campoCantidad = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelProducto.add(labelCantidad, gbc);
        gbc.gridx = 1;
        panelProducto.add(campoCantidad, gbc);

        // Botón para agregar productos por ID o nombre
        JButton botonAgregarProducto = new JButton("Agregar Producto");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelProducto.add(botonAgregarProducto, gbc);

        add(panelProducto, BorderLayout.SOUTH);

        // Panel de confirmación de venta y otros controles
        JPanel panelConfirmacion = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botón para confirmar la venta
        JButton botonConfirmarVenta = new JButton("Confirmar Venta");
        panelConfirmacion.add(botonConfirmarVenta);

        // Botón para eliminar un producto seleccionado
        JButton botonEliminarProducto = new JButton("Eliminar Producto");
        panelConfirmacion.add(botonEliminarProducto);

        // Botón para volver al menú principal
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

        // Funcionalidad para agregar productos
        botonAgregarProducto.addActionListener(e -> {
            String idProducto = campoIdProducto.getText().trim();
            String nombreProducto = campoNombreProducto.getText().trim();
            try {
                int cantidad = Integer.parseInt(campoCantidad.getText().trim());
                if (cantidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Producto producto = idProducto.isEmpty() ? gestorInventario.buscarProductoPorNombre(nombreProducto) : gestorInventario.buscarProductoPorId(idProducto);
                if (producto == null) {
                    JOptionPane.showMessageDialog(this, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (cantidad > producto.getCantidad()) {
                    JOptionPane.showMessageDialog(this, "Cantidad no disponible", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                modeloTablaVenta.addRow(new Object[]{producto.getId(), producto.getNombre(), cantidad, producto.getPrecio() * cantidad});
                campoIdProducto.setText("");
                campoNombreProducto.setText("");
                campoCantidad.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido para la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
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
        
        // Funcionalidades del botón eliminar producto
        botonEliminarProducto.addActionListener(e -> {
            int selectedRow = tablaVenta.getSelectedRow();
            if (selectedRow >= 0) {
                modeloTablaVenta.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Seleccione un producto para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    StringBuilder detallesProductos = new StringBuilder();
    double totalValor = 0;
    for (int i = 0; i < rowCount; i++) {
        String idProducto = (String) modeloTablaVenta.getValueAt(i, 0);
        int cantidadVendida = (int) modeloTablaVenta.getValueAt(i, 2);
        double precio = (double) modeloTablaVenta.getValueAt(i, 3);

        Producto producto = gestorInventario.buscarProductoPorId(idProducto);
        if (producto != null) {
            producto.setCantidad(producto.getCantidad() - cantidadVendida);
            detallesProductos.append(producto.getNombre()).append(" x").append(cantidadVendida).append("; ");
            totalValor += precio;
        }
    }

    // Incrementa el ID de venta
    gestorInventario.ultimaIdVenta++;

    // Guarda los cambios en el inventario y en el archivo de ventas
    gestorInventario.guardarProductosEnCSV("inventario.csv");
    gestorInventario.guardarVentaEnCSV("ventas.csv", gestorInventario.ultimaIdVenta, clienteActual.getNombre(), detallesProductos.toString(), totalValor);

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
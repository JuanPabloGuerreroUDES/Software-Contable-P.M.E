/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 *
 * @author juang
 */
public class AgregarProductosGUI extends JFrame {
    private List<Producto> productos;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private GestorDeInventario gestor;

    public AgregarProductosGUI() {
        super("Agregar Productos");
        this.productos = new GestorDeInventario().getProductos();
        this.gestor = new GestorDeInventario(productos);

        inicializarComponentes();

        // Cargar productos desde el archivo CSV al iniciar
        cargarDatos();
        
        
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Guardar productos en el archivo CSV al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarYSalir();
            }
        });
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Modelo de la tabla
        String[] columnas = {"ID", "Nombre", "Cantidad", "Precio", "Stock Mínimo", "Proveedor"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);

        // Panel para la tabla
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel para formularios y botones
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campo y etiqueta ID
        JLabel labelId = new JLabel("ID del Producto:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(labelId, gbc);

        JTextField campoId = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoId, gbc);

        // Campo y etiqueta Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(labelCantidad, gbc);

        JTextField campoCantidad = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoCantidad, gbc);

        // Botón agregar productos
        JButton botonAgregar = new JButton("Agregar Productos");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelFormulario.add(botonAgregar, gbc);

        // Botón para volver al menú principal
        JButton botonVolver = new JButton("Volver al Menú Principal");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelFormulario.add(botonVolver, gbc);

        add(panelFormulario, BorderLayout.SOUTH);

        // Funcionalidades del botón agregar
        botonAgregar.addActionListener(e -> {
            try {
                String id = campoId.getText();
                int cantidad = Integer.parseInt(campoCantidad.getText());

                Producto producto = buscarProductoPorId(id);
                if (producto == null) {
                    JOptionPane.showMessageDialog(null, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                producto.setCantidad(producto.getCantidad() + cantidad);
                actualizarTabla();
                // Limpiar campos
                campoId.setText("");
                campoCantidad.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error en el formato de número", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Funcionalidades del botón volver
        botonVolver.addActionListener(e -> {
            guardarYSalir();
        });
    }

    private void cargarDatos() {
        gestor.cargarProductosDesdeCSV("inventario.csv");
        actualizarTabla();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);  // Limpiar tabla antes de cargar
        for (Producto producto : productos) {
            modeloTabla.addRow(new Object[]{
                producto.getId(), 
                producto.getNombre(), 
                producto.getCantidad(), 
                producto.getPrecio(), 
                producto.getStockMinimo(), 
                producto.getProveedor()
            });
        }
    }

    private Producto buscarProductoPorId(String id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return producto;
            }
        }
        return null;
    }

    private void guardarYSalir() {
        gestor.guardarProductosEnCSV("inventario.csv");
        System.out.println("Inventario guardado en inventario.csv antes de salir.");
        new MenuPrincipalGUI().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new AgregarProductosGUI().setVisible(true));
    }
}

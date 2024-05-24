/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;




import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author juang
 */

public class CrearProductosGUI extends JFrame {
    private List<Producto> productos;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private GestorDeInventario gestor;

    public CrearProductosGUI() {
        super("Añadir Productos");
        this.productos = new ArrayList<>();
        this.gestor = new GestorDeInventario(productos);

        inicializarComponentes();

        // Cargar productos desde el archivo CSV al iniciar
        cargarProductos();

        setSize(800, 600);
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
        JLabel labelId = new JLabel("ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        JTextField campoId = new JTextField(10);
        panelFormulario.add(labelId, gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoId, gbc);

        // Campo y etiqueta Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        JTextField campoNombre = new JTextField(10);
        panelFormulario.add(labelNombre, gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoNombre, gbc);

        // Campo y etiqueta Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        JTextField campoCantidad = new JTextField(10);
        panelFormulario.add(labelCantidad, gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoCantidad, gbc);

        // Campo y etiqueta Precio
        JLabel labelPrecio = new JLabel("Precio:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        JTextField campoPrecio = new JTextField(10);
        panelFormulario.add(labelPrecio, gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoPrecio, gbc);

        // Campo y etiqueta Stock Mínimo
        JLabel labelStockMinimo = new JLabel("Stock Mínimo:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        JTextField campoStockMinimo = new JTextField(10);
        panelFormulario.add(labelStockMinimo, gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoStockMinimo, gbc);

        // Campo y etiqueta Proveedor
        JLabel labelProveedor = new JLabel("Proveedor:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        JTextField campoProveedor = new JTextField(10);
        panelFormulario.add(labelProveedor, gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoProveedor, gbc);

        // Botón agregar producto
        JButton botonAgregar = new JButton("Agregar Producto");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panelFormulario.add(botonAgregar, gbc);

        botonAgregar.addActionListener(e -> agregarProducto(campoId, campoNombre, campoCantidad, campoPrecio, campoStockMinimo, campoProveedor));

        // Botón borrar seleccionados
        JButton botonBorrarSeleccionados = new JButton("Borrar seleccionados");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panelFormulario.add(botonBorrarSeleccionados, gbc);

        botonBorrarSeleccionados.addActionListener(e -> borrarProductosSeleccionados());

        // Botón borrar por ID
        JLabel labelIdBorrar = new JLabel("ID a borrar:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        panelFormulario.add(labelIdBorrar, gbc);

        JTextField campoIdBorrar = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoIdBorrar, gbc);

        JButton botonBorrarPorId = new JButton("Borrar por ID");
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panelFormulario.add(botonBorrarPorId, gbc);

        botonBorrarPorId.addActionListener(e -> borrarProductoPorId(campoIdBorrar.getText()));

        // Botón para volver al menú principal
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonVolver = new JButton("Volver al Menú Principal");
        panelVolver.add(botonVolver);
        add(panelVolver, BorderLayout.NORTH);

        botonVolver.addActionListener(e -> guardarYSalir());

        add(panelFormulario, BorderLayout.SOUTH);
    }

    private void cargarProductos() {
        gestor.cargarProductosDesdeCSV("inventario.csv");
        for (Producto producto : productos) {
            agregarProductoATabla(producto);
        }
    }

    private void agregarProductoATabla(Producto producto) {
        modeloTabla.addRow(new Object[]{producto.getId(), producto.getNombre(), producto.getCantidad(), producto.getPrecio(), producto.getStockMinimo(), producto.getProveedor()});
    }

    private void borrarProductoPorId(String id) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId().equals(id)) {
                productos.remove(i);
                modeloTabla.removeRow(i);
                break;
            }
        }
    }

    private void borrarProductosSeleccionados() {
        int[] selectedRows = tabla.getSelectedRows();
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int modelRow = tabla.convertRowIndexToModel(selectedRows[i]);
            modeloTabla.removeRow(modelRow);
            productos.remove(modelRow);
        }
        JOptionPane.showMessageDialog(this, "Productos seleccionados eliminados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void agregarProducto(JTextField campoId, JTextField campoNombre, JTextField campoCantidad, JTextField campoPrecio, JTextField campoStockMinimo, JTextField campoProveedor) {
    try {
        String id = campoId.getText().trim();
        String nombre = campoNombre.getText().trim();
        int cantidad = Integer.parseInt(campoCantidad.getText().trim());
        double precio = Double.parseDouble(campoPrecio.getText().trim());
        int stockMinimo = Integer.parseInt(campoStockMinimo.getText().trim());
        String proveedor = campoProveedor.getText().trim();

        if (nombre.isEmpty() || proveedor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cantidad <= 0 || precio <= 0 || stockMinimo < 0) {
            JOptionPane.showMessageDialog(this, "Cantidad, precio y stock mínimo deben tener valores adecuados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto productoExistente = buscarProductoPorId(id);
        if (productoExistente != null) {
            JOptionPane.showMessageDialog(this, "Un producto con este ID ya existe. Por favor, utilice un ID único.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto nuevoProducto = new Producto(id, nombre, cantidad, precio, stockMinimo, proveedor);
        productos.add(nuevoProducto);
        agregarProductoATabla(nuevoProducto);
        limpiarCampos(campoId, campoNombre, campoCantidad, campoPrecio, campoStockMinimo, campoProveedor);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos en los campos de cantidad, precio y stock mínimo.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void limpiarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
    
    private Producto buscarProductoPorId(String id) {
    for (Producto producto : productos) {
        if (producto.getId().equals(id)) {
            return producto;
        }
    }
    return null; // Retorna null si no encuentra el producto con el ID especificado.
}

    private void guardarYSalir() {
        gestor.guardarProductosEnCSV("inventario.csv");
        System.out.println("Inventario guardado en inventario.csv antes de salir.");
        dispose();
        new MenuPrincipalGUI().setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new CrearProductosGUI().setVisible(true));
    }
}

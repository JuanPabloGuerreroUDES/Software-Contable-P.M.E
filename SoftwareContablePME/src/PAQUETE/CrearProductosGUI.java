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
        panelFormulario.add(labelId, gbc);

        JTextField campoId = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoId, gbc);

        // Campo y etiqueta Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(labelNombre, gbc);

        JTextField campoNombre = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoNombre, gbc);

        // Campo y etiqueta Cantidad
        JLabel labelCantidad = new JLabel("Cantidad:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(labelCantidad, gbc);

        JTextField campoCantidad = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoCantidad, gbc);

        // Campo y etiqueta Precio
        JLabel labelPrecio = new JLabel("Precio:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFormulario.add(labelPrecio, gbc);

        JTextField campoPrecio = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoPrecio, gbc);

        // Campo y etiqueta Stock Mínimo
        JLabel labelStockMinimo = new JLabel("Stock Mínimo:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelFormulario.add(labelStockMinimo, gbc);

        JTextField campoStockMinimo = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoStockMinimo, gbc);

        // Campo y etiqueta Proveedor
        JLabel labelProveedor = new JLabel("Proveedor:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelFormulario.add(labelProveedor, gbc);

        JTextField campoProveedor = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoProveedor, gbc);

        // Botón agregar producto
        JButton botonAgregar = new JButton("Agregar Producto");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panelFormulario.add(botonAgregar, gbc);

        // Botón borrar seleccionados
        JButton botonBorrarSeleccionados = new JButton("Borrar seleccionados");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panelFormulario.add(botonBorrarSeleccionados, gbc);

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

        // Botón para volver al menú principal
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonVolver = new JButton("Volver al Menú Principal");
        panelVolver.add(botonVolver);
        add(panelVolver, BorderLayout.NORTH);

        // Añadir panel de formulario al sur
        add(panelFormulario, BorderLayout.SOUTH);

        // Funcionalidades del botón agregar
        botonAgregar.addActionListener(e -> {
    try {
        String id = campoId.getText();
        String nombre = campoNombre.getText();
        int cantidad = Integer.parseInt(campoCantidad.getText());
        double precio = Double.parseDouble(campoPrecio.getText());
        int stockMinimo = Integer.parseInt(campoStockMinimo.getText());
        String proveedor = campoProveedor.getText();

        // Verificar que el campo proveedor no esté vacío
        if (proveedor.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo Proveedor es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar que la ID no esté duplicada
        if (buscarProductoPorId(id) != null) {
            JOptionPane.showMessageDialog(null, "La ID ya existe. Por favor, ingrese una ID diferente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto producto = new Producto(id, nombre, cantidad, precio, stockMinimo, proveedor);
        productos.add(producto);
        agregarProductoATabla(producto);
        // Limpiar campos
        campoId.setText("");
        campoNombre.setText("");
        campoCantidad.setText("");
        campoPrecio.setText("");
        campoStockMinimo.setText("");
        campoProveedor.setText("");
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Error en el formato de número", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});

        // Funcionalidades del botón borrar por ID
        botonBorrarPorId.addActionListener(e -> {
            String id = campoIdBorrar.getText();
            borrarProductoPorId(id);
            campoIdBorrar.setText("");
        });

        // Funcionalidades del botón borrar seleccionados
        botonBorrarSeleccionados.addActionListener(e -> borrarProductosSeleccionados());

        // Funcionalidad del botón volver
        botonVolver.addActionListener(e -> {
            guardarYSalir();
        });
    }

    private void cargarProductos() {
        gestor.cargarProductosDesdeCSV("inventario.csv");
        modeloTabla.setRowCount(0);  // Limpiar tabla antes de cargar
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
        int[] filasSeleccionadas = tabla.getSelectedRows();
        List<Producto> productosABorrar = new ArrayList<>();

        for (int fila : filasSeleccionadas) {
            String id = (String) modeloTabla.getValueAt(fila, 0);
            productosABorrar.add(buscarProductoPorId(id));
        }

        for (Producto producto : productosABorrar) {
            productos.remove(producto);
            modeloTabla.removeRow(buscarIndiceEnTabla(producto.getId()));
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
    private int buscarIndiceEnTabla(String id) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (modeloTabla.getValueAt(i, 0).equals(id)) {
                return i;
            }
        }
        return -1;
    }
    
    

    private void guardarYSalir() {
        gestor.guardarProductosEnCSV("inventario.csv");
        System.out.println("Inventario guardado en inventario.csv antes de salir.");
        new MenuPrincipalGUI().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new CrearProductosGUI().setVisible(true));
    }
}

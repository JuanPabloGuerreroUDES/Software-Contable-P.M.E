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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juang
 */
public class ProductosBajosStockGUI extends JFrame {
    private List<Producto> productos;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private GestorDeInventario gestor;

    public ProductosBajosStockGUI() {
        super("Productos Bajos de Stock");
        this.productos = new GestorDeInventario().getProductos();
        this.gestor = new GestorDeInventario(productos);

        inicializarComponentes();

        // Cargar productos desde el archivo CSV al iniciar
        cargarDatos();

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Guardar productos en el archivo CSV al cerrar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarProductosBajosStock();
                new MenuPrincipalGUI().setVisible(true);
                dispose();
            }
        });
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Modelo de la tabla
        String[] columnas = {"ID", "Nombre", "Cantidad", "Stock Mínimo", "Proveedor"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);

        // Panel para la tabla
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botón para verificar productos bajos de stock
        JPanel panelBoton = new JPanel();
        JButton botonVerificar = new JButton("Verificar Productos Bajos de Stock");
        botonVerificar.addActionListener(e -> verificarProductosBajosStock());
        panelBoton.add(botonVerificar);

        // Botón para volver al menú principal
        JButton botonVolver = new JButton("Volver al Menú Principal");
        botonVolver.addActionListener(e -> {
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        });
        panelBoton.add(botonVolver);

        add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        gestor.cargarProductosDesdeCSV("inventario.csv");
    }

    private void verificarProductosBajosStock() {
        modeloTabla.setRowCount(0);  // Limpiar tabla antes de cargar
        for (Producto producto : productos) {
            if (producto.getCantidad() < producto.getStockMinimo()) {
                modeloTabla.addRow(new Object[]{
                    producto.getId(), 
                    producto.getNombre(), 
                    producto.getCantidad(), 
                    producto.getStockMinimo(), 
                    producto.getProveedor()
                });
            }
        }
    }

    private void guardarProductosBajosStock() {
        List<Producto> productosBajosStock = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getCantidad() < producto.getStockMinimo()) {
                productosBajosStock.add(producto);
            }
        }
        gestor.guardarProductosBajosStockEnCSV("productos_bajos_stock.csv", productosBajosStock);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new ProductosBajosStockGUI().setVisible(true));
    }
}

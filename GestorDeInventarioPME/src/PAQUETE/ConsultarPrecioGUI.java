/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author juang
 */
public class ConsultarPrecioGUI extends JFrame {
    private JTable tablaInventario;
    private DefaultTableModel modeloTabla;
    private JTextField campoBusqueda;
    private JButton botonBuscar, botonVolver;

    public ConsultarPrecioGUI() {
        setTitle("Consultar Precio");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarComponentes();
        cargarDatosInventario();
        setVisible(true);
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Cantidad", "Precio"}, 0);
        tablaInventario = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaInventario);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel();
        campoBusqueda = new JTextField(15);
        botonBuscar = new JButton("Buscar");
        botonVolver = new JButton("Volver al Menú Principal");

        botonBuscar.addActionListener(this::accionBuscar);
        botonVolver.addActionListener(e -> {
            MenuPrincipalGUI menuPrincipal = new MenuPrincipalGUI();
            menuPrincipal.setVisible(true);
            dispose();
        });

        panelInferior.add(new JLabel("Buscar por ID o Nombre:"));
        panelInferior.add(campoBusqueda);
        panelInferior.add(botonBuscar);
        panelInferior.add(botonVolver);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarDatosInventario() {
    File archivo = new File("inventario.csv");
    if (!archivo.exists()) {
        JOptionPane.showMessageDialog(this, "El archivo de inventario no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String line = br.readLine(); // Leer y descartar la cabecera, si es necesario.
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 4) {  // Asegurarse de que la línea tiene la cantidad correcta de datos
                modeloTabla.addRow(new Object[] {data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim()});
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar el inventario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void accionBuscar(ActionEvent e) {
    String busqueda = campoBusqueda.getText().toLowerCase().trim();
    if (busqueda.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese un ID o nombre para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    boolean found = false;
    for (int i = 0; i < modeloTabla.getRowCount(); i++) {
        String id = modeloTabla.getValueAt(i, 0).toString().toLowerCase();
        String nombre = modeloTabla.getValueAt(i, 1).toString().toLowerCase();
        if (id.contains(busqueda) || nombre.contains(busqueda)) {
            tablaInventario.setRowSelectionInterval(i, i);
            tablaInventario.scrollRectToVisible(new Rectangle(tablaInventario.getCellRect(i, 0, true)));
            found = true;
            break;
        }
    }

    if (!found) {
        JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}

    public static void main(String[] args) {
        EventQueue.invokeLater(ConsultarPrecioGUI::new);
    }
}
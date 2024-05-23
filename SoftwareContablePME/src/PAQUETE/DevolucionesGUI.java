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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author juang
 */
public class DevolucionesGUI extends JFrame {
    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;
    private GestorDeInventario gestorInventario;

    public DevolucionesGUI() {
        super("Devoluciones");
        this.gestorInventario = new GestorDeInventario();
        inicializarComponentes();
        cargarDatosVentas("ventas.csv");

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new Object[]{"ID Venta", "Cliente", "Productos", "Valor Total", "Estado"}, 0);
        tablaVentas = new JTable(modeloTabla);
        add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JLabel labelIdVenta = new JLabel("ID Venta:");
        JTextField campoIdVenta = new JTextField(10);
        JButton botonDevolver = new JButton("Devolver");
        JButton botonVolver = new JButton("Volver al Menú Principal");

        panelInferior.add(labelIdVenta);
        panelInferior.add(campoIdVenta);
        panelInferior.add(botonDevolver);
        panelInferior.add(botonVolver);

        add(panelInferior, BorderLayout.SOUTH);

        botonDevolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int idVenta = Integer.parseInt(campoIdVenta.getText());
                    boolean exito = gestorInventario.cambiarEstadoVenta(idVenta, "Devolucion");
                    if (exito) {
                        JOptionPane.showMessageDialog(null, "Venta devuelta con éxito", "Información", JOptionPane.INFORMATION_MESSAGE);
                        cargarDatosVentas("ventas.csv"); // Recargar los datos
                    } else {
                        JOptionPane.showMessageDialog(null, "ID de venta no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID de venta inválido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        botonVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipalGUI().setVisible(true);
                dispose();
            }
        });
    }

    private void cargarDatosVentas(String archivo) {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar nuevos datos
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5) {
                    // Manejo del error: podrías registrar esto o mostrar un mensaje
                    System.err.println("Datos incompletos en la línea: " + line);
                    continue; // Salta esta línea porque no tiene datos completos
                }
                modeloTabla.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4]});
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
        }
    }
}
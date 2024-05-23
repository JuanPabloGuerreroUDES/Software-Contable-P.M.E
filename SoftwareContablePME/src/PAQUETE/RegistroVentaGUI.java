/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author juang
 */
public class RegistroVentaGUI extends JFrame {
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public RegistroVentaGUI() {
        super("Registro de Ventas");
        inicializarComponentes();
        cargarDatosVentas("ventas.csv");

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new Object[]{"ID Venta", "Cliente", "Productos", "Valor Total"}, 0);
        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton botonVolver = new JButton("Volver al Menú Principal");
        botonVolver.addActionListener(e -> {
            new MenuPrincipalGUI().setVisible(true);
            dispose();
        });
        add(botonVolver, BorderLayout.SOUTH);
    }

    private void cargarDatosVentas(String archivo) {
    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length < 4) {
                // Manejo del error: podrías registrar esto o mostrar un mensaje
                System.err.println("Datos incompletos en la línea: " + line);
                continue; // Salta esta línea porque no tiene datos completos
            }
            modeloTabla.addRow(new Object[]{data[0], data[1], data[2], data[3]});
        }
    } catch (IOException e) {
        System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
    }
}
}

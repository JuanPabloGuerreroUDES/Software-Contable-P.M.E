/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author juang
 */
public class ConsultarPrecioGUI extends JFrame {
    private GestorDeInventario gestor;

    public ConsultarPrecioGUI() {
        setTitle("Consultar Precio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gestor = new GestorDeInventario();
        gestor.cargarProductosDesdeCSV("inventario.csv"); // Asegúrate de cargar los productos desde el CSV
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel labelId = new JLabel("ID del producto:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(labelId, gbc);

        JTextField campoId = new JTextField();
        gbc.gridx = 1;
        add(campoId, gbc);

        JLabel labelNombre = new JLabel("Nombre del producto:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(labelNombre, gbc);

        JTextField campoNombre = new JTextField();
        gbc.gridx = 1;
        add(campoNombre, gbc);

        JButton botonConsultar = new JButton("Consultar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(botonConsultar, gbc);

        JLabel labelResultado = new JLabel();
        gbc.gridy = 3;
        add(labelResultado, gbc);

        // Botón para volver al menú principal en un panel separado
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonVolver = new JButton("Volver al Menú Principal");
        panelVolver.add(botonVolver);
        gbc.gridy = 4;
        add(panelVolver, gbc);

        botonConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = campoId.getText();
                String nombre = campoNombre.getText();
                Producto producto = null;

                if (!id.isEmpty()) {
                    producto = gestor.buscarProductoPorId(id);
                } else if (!nombre.isEmpty()) {
                    producto = gestor.buscarProductoPorNombre(nombre);
                }

                if (producto != null) {
                    labelResultado.setText("Nombre: "+ producto.getNombre()+" Precio: " + producto.getPrecio());
                } else {
                    labelResultado.setText("Producto no encontrado");
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
}

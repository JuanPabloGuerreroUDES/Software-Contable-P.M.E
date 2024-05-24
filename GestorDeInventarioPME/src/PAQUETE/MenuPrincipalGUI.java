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
public class MenuPrincipalGUI extends JFrame {
    public MenuPrincipalGUI() {
        setTitle("Menú Principal");
         
        setSize(400, 400);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new GridLayout(8, 1, 10, 10));

        JButton botonCrearProductos = new JButton("Crear productos");
        JButton botonCrearClientes = new JButton("Crear clientes");
        JButton botonConsultarPrecio = new JButton("Consultar precio");
        JButton botonRealizarVentas = new JButton("Realizar ventas");
        JButton botonAgregarProductos = new JButton("Agregar Productos");
        JButton botonBajosDeStock = new JButton("Productos bajos de stock");
        JButton botonDevoluciones = new JButton("Devoluciones");
        JButton botonRegistroVentas = new JButton("Registro de ventas");
        JButton botonSalir = new JButton("Salir");
        JButton botonCerrarSesion = new JButton("Cerrar Sesión");
        
        
        // Añadir botones según el tipo de usuario
        if (SessionInfo.getTipoUsuario() == 1) {
            add(botonCrearClientes);
            add(botonConsultarPrecio);
            add(botonRealizarVentas);
            add(botonRegistroVentas);
            add(botonCerrarSesion);
            add(botonSalir);
            
        } else if (SessionInfo.getTipoUsuario() == 2) {
            // Asumiendo que el tipo 2 puede acceder a todas las funcionalidades
            add(botonCrearProductos);
            add(botonCrearClientes);
            add(botonConsultarPrecio);
            add(botonRealizarVentas);
            add(botonAgregarProductos);
            add(botonBajosDeStock);
            add(botonDevoluciones);
            add(botonRegistroVentas);
            add(botonCerrarSesion);
            add(botonSalir);
        }
        botonSalir.addActionListener(e -> System.exit(0));
        
        botonCrearProductos.addActionListener(e -> {
            new CrearProductosGUI().setVisible(true);
            dispose();
        });
        
        botonCrearClientes.addActionListener(e -> {
            new AgregarClienteGUI().setVisible(true);
            dispose();
        });
        
        botonConsultarPrecio.addActionListener(e -> {
            new ConsultarPrecioGUI().setVisible(true);
            dispose();
        });
        
        botonRealizarVentas.addActionListener(e -> {
            new RealizarVentaGUI().setVisible(true);
            dispose();
        });
        
        botonAgregarProductos.addActionListener(e -> {
            new AgregarProductosGUI().setVisible(true);
            dispose();
        });
        
        botonBajosDeStock.addActionListener(e -> {
            new ProductosBajosStockGUI().setVisible(true);
            dispose();
        });
        
        botonDevoluciones.addActionListener(e -> {
            new DevolucionesGUI().setVisible(true);
            dispose();
        });
        
        botonRegistroVentas.addActionListener(e -> {
            new RegistroVentaGUI().setVisible(true);
            dispose();
        });
        
        botonCerrarSesion.addActionListener(e -> {
            new InicioGUI().setVisible(true);
            dispose();
        });

        botonSalir.addActionListener(e -> System.exit(0));
    }
}
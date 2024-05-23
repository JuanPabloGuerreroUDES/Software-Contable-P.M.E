/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import javax.swing.*;
import java.awt.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;


/**
 *
 * @author juang
 */
public class InicioGUI extends JFrame {
    JTextField campoUsuario;
    JPasswordField campoContrasena;
    JButton botonIniciarSesion;
    HashMap<String, String> credenciales;  // Para almacenar las credenciales de usuario

    public InicioGUI() {
        setTitle("Inicio de Sesión - Software Contable PME");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cargarCredenciales();  // Cargar credenciales desde el archivo

        JLabel titulo = new JLabel("Software Contable PME", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel centroPanel = new JPanel();
        centroPanel.setLayout(new GridLayout(4, 1, 10, 5));
        campoUsuario = new JTextField(10);
        campoContrasena = new JPasswordField(10);
        botonIniciarSesion = new JButton("Iniciar Sesión");

        centroPanel.add(new JLabel("Usuario:"));
        centroPanel.add(campoUsuario);
        centroPanel.add(new JLabel("Contraseña:"));
        centroPanel.add(campoContrasena);

        add(centroPanel, BorderLayout.CENTER);

        botonIniciarSesion.addActionListener(e -> {
            String usuario = campoUsuario.getText();
            String contrasena = new String(campoContrasena.getPassword());
            if (verificarCredenciales(usuario, contrasena)) {
                determinarTipoUsuario(usuario);
                iniciarMenuPrincipal();
                dispose();  // Cierra la ventana de inicio de sesión
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel surPanel = new JPanel(new FlowLayout());
        surPanel.add(botonIniciarSesion);
        add(surPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarCredenciales() {
        credenciales = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    credenciales.put(partes[0], partes[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de usuarios: " + e.getMessage());
        }
    }

    private boolean verificarCredenciales(String usuario, String contrasena) {
        return credenciales.containsKey(usuario) && credenciales.get(usuario).equals(contrasena);
    }

    private void determinarTipoUsuario(String usuario) {
        if (usuario.equalsIgnoreCase("Empleado")) {
            SessionInfo.setTipoUsuario(1);  // Tipo de usuario 1 para Empleado
        } else if (usuario.equalsIgnoreCase("Administrador")) {
            SessionInfo.setTipoUsuario(2);  // Tipo de usuario 2 para Administrador
        }
    }

    private void iniciarMenuPrincipal() {
        MenuPrincipalGUI menuPrincipal = new MenuPrincipalGUI();
        menuPrincipal.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(InicioGUI::new);
    }
}
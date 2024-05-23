/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juang
 */
public class InicioGUI extends JFrame {
    private Map<String, String> credenciales;

    public InicioGUI() {
        setTitle("Software Contable P.M.E");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarComponentes();
        cargarCredenciales();
    }

    private void inicializarComponentes() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel labelEmpresa = new JLabel("Software Contable P.M.E", SwingConstants.CENTER);
        labelEmpresa.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(labelEmpresa, gbc);

        JLabel labelUsuario = new JLabel("Usuario:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(labelUsuario, gbc);

        JTextField campoUsuario = new JTextField();
        gbc.gridx = 1;
        add(campoUsuario, gbc);

        JLabel labelPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(labelPassword, gbc);

        JPasswordField campoPassword = new JPasswordField();
        gbc.gridx = 1;
        add(campoPassword, gbc);

        JButton botonLogin = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(botonLogin, gbc);

        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText();
                String password = new String(campoPassword.getPassword());

                if (verificarLogin(usuario, password)) {
                    new MenuPrincipalGUI().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton botonSalir = new JButton("Salir");
        gbc.gridy = 4;
        add(botonSalir, gbc);

        botonSalir.addActionListener(e -> System.exit(0));
    }

    private void cargarCredenciales() {
        credenciales = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("usuarios.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    credenciales.put(partes[0], partes[1]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las credenciales: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean verificarLogin(String usuario, String password) {
        return credenciales.containsKey(usuario) && credenciales.get(usuario).equals(password);
    }
}

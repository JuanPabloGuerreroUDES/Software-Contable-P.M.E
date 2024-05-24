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
public class AgregarClienteGUI extends JFrame {
    private List<Cliente> clientes;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private GestorDeClientes gestor;

    public AgregarClienteGUI() {
        super("Agregar Clientes");
        this.clientes = new ArrayList<>();
        this.gestor = new GestorDeClientes(clientes);

        inicializarComponentes();

        // Cargar clientes desde el archivo CSV al iniciar
        cargarClientes();

       
        
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Guardar clientes en el archivo CSV al cerrar
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
        String[] columnas = {"Nombre", "Identificación", "Número de Teléfono"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);

        // Panel para la tabla
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel para formularios y botones
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campo y etiqueta Nombre
        JLabel labelNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(labelNombre, gbc);

        JTextField campoNombre = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoNombre, gbc);

        // Campo y etiqueta Identificación
        JLabel labelIdentificacion = new JLabel("Identificación:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(labelIdentificacion, gbc);

        JTextField campoIdentificacion = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoIdentificacion, gbc);

        // Campo y etiqueta Número de Teléfono
        JLabel labelNumeroTelefono = new JLabel("Número de Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFormulario.add(labelNumeroTelefono, gbc);

        JTextField campoNumeroTelefono = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoNumeroTelefono, gbc);

        // Botón agregar cliente
        JButton botonAgregar = new JButton("Agregar Cliente");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelFormulario.add(botonAgregar, gbc);

        // Botón borrar seleccionados
        JButton botonBorrarSeleccionados = new JButton("Borrar seleccionados");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelFormulario.add(botonBorrarSeleccionados, gbc);

        // Botón borrar por Identificación
        JLabel labelIdBorrar = new JLabel("Identificación a borrar:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelFormulario.add(labelIdBorrar, gbc);

        JTextField campoIdBorrar = new JTextField(10);
        gbc.gridx = 1;
        panelFormulario.add(campoIdBorrar, gbc);

        JButton botonBorrarPorId = new JButton("Borrar por Identificación");
        gbc.gridx = 0;
        gbc.gridy = 6;
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
        String nombre = campoNombre.getText();
        String identificacion = campoIdentificacion.getText();
        String numeroTelefono = campoNumeroTelefono.getText();

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || identificacion.isEmpty() || numeroTelefono.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que el nombre solo contenga letras
        if (!nombre.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(null, "El nombre solo puede contener letras y espacios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que la identificación y el número de teléfono solo contengan números
        if (!identificacion.matches("\\d+") || !numeroTelefono.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "La identificación y el número de teléfono solo pueden contener números", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar que la Identificación no esté duplicada
        if (buscarClientePorIdentificacion(identificacion) != null) {
            JOptionPane.showMessageDialog(null, "La Identificación ya existe. Por favor, ingrese una Identificación diferente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente(nombre, identificacion, numeroTelefono);
        clientes.add(cliente);
        agregarClienteATabla(cliente);
        // Limpiar campos
        campoNombre.setText("");
        campoIdentificacion.setText("");
        campoNumeroTelefono.setText("");
    } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});

        // Funcionalidades del botón borrar por Identificación
        botonBorrarPorId.addActionListener(e -> {
            String identificacion = campoIdBorrar.getText();
            borrarClientePorIdentificacion(identificacion);
            campoIdBorrar.setText("");
        });

        // Funcionalidades del botón borrar seleccionados
        botonBorrarSeleccionados.addActionListener(e -> borrarClientesSeleccionados());

        // Funcionalidad del botón volver
        botonVolver.addActionListener(e -> {
            guardarYSalir();
        });
    }

    private void cargarClientes() {
        gestor.cargarClientesDesdeCSV("clientes.csv");
        modeloTabla.setRowCount(0);  // Limpiar tabla antes de cargar
        for (Cliente cliente : clientes) {
            agregarClienteATabla(cliente);
        }
    }

    private void agregarClienteATabla(Cliente cliente) {
        modeloTabla.addRow(new Object[]{cliente.getNombre(), cliente.getIdentificacion(), cliente.getNumeroTelefono()});
    }

    private void borrarClientePorIdentificacion(String identificacion) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getIdentificacion().equals(identificacion)) {
                clientes.remove(i);
                modeloTabla.removeRow(i);
                break;
            }
        }
    }

    private void borrarClientesSeleccionados() {
        int[] filasSeleccionadas = tabla.getSelectedRows();
        List<Cliente> clientesABorrar = new ArrayList<>();

        for (int fila : filasSeleccionadas) {
            String identificacion = (String) modeloTabla.getValueAt(fila, 1);
            clientesABorrar.add(buscarClientePorIdentificacion(identificacion));
        }

        for (Cliente cliente : clientesABorrar) {
            clientes.remove(cliente);
            modeloTabla.removeRow(buscarIndiceEnTabla(cliente.getIdentificacion()));
        }
    }

    private Cliente buscarClientePorIdentificacion(String identificacion) {
    for (Cliente cliente : clientes) {
        if (cliente.getIdentificacion().equals(identificacion)) {
            return cliente;
        }
    }
    return null;
}

    private int buscarIndiceEnTabla(String identificacion) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (modeloTabla.getValueAt(i, 1).equals(identificacion)) {
                return i;
            }
        }
        return -1;
    }

    private void guardarYSalir() {
        gestor.guardarClientesEnCSV("clientes.csv");
        System.out.println("Clientes guardados en clientes.csv antes de salir.");
        new MenuPrincipalGUI().setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new AgregarClienteGUI().setVisible(true));
    }
}
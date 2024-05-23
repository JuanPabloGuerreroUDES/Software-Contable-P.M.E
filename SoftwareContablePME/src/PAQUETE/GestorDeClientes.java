/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juang
 */
public class GestorDeClientes {
    private List<Cliente> clientes;

    public GestorDeClientes() {
        this.clientes = new ArrayList<>();
    }

    public GestorDeClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void guardarClientesEnCSV(String archivo) {
        try (FileWriter fileWriter = new FileWriter(archivo)) {
            fileWriter.append("Nombre,Identificacion,NumeroTelefono\n");  // Añadir cabecera al CSV
            for (Cliente cliente : clientes) {
                fileWriter.append(cliente.toString()).append("\n");
            }
            System.out.println("Clientes guardados en " + archivo);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public void cargarClientesDesdeCSV(String archivo) {
        clientes.clear();  // Limpiar la lista de clientes antes de cargar nuevos
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("Nombre")) {
                    continue;  // Omitir cabecera y líneas vacías
                }
                String[] campos = linea.split(",");
                if (campos.length >= 3) {  // Asegurar que haya suficientes campos
                    Cliente cliente = new Cliente(
                        campos[0], 
                        campos[1], 
                        campos[2]
                    );
                    agregarCliente(cliente);
                } else {
                    System.out.println("Línea inválida en el CSV: " + linea);
                }
            }
            System.out.println("Clientes cargados desde " + archivo);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
}
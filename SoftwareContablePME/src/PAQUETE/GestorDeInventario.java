/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juang
 */
public class GestorDeInventario {
    private List<Producto> productos;

    public GestorDeInventario() {
        this.productos = new ArrayList<>();
    }

    public GestorDeInventario(List<Producto> productos) {
        this.productos = productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void guardarProductosEnCSV(String archivo) {
        try (FileWriter fileWriter = new FileWriter(archivo)) {
            fileWriter.append("ID,Nombre,Cantidad,Precio,StockMinimo,Proveedor\n");  // Añadir cabecera al CSV
            for (Producto producto : productos) {
                fileWriter.append(producto.toString()).append("\n");
            }
            System.out.println("Inventario guardado en " + archivo);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public void cargarProductosDesdeCSV(String archivo) {
        productos.clear();  // Limpiar la lista de productos antes de cargar nuevos
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.startsWith("ID")) {
                    continue;  // Omitir cabecera y líneas vacías
                }
                String[] campos = linea.split(",");
                if (campos.length >= 6) {  // Asegurar que haya suficientes campos
                    Producto producto = new Producto(
                        campos[0], 
                        campos[1], 
                        Integer.parseInt(campos[2]), 
                        Double.parseDouble(campos[3]),
                        Integer.parseInt(campos[4]),
                        campos[5]
                    );
                    agregarProducto(producto);
                } else {
                    System.out.println("Línea inválida en el CSV: " + linea);
                }
            }
            System.out.println("Inventario cargado desde " + archivo);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
    
    public void guardarProductosBajosStockEnCSV(String archivo, List<Producto> productosBajosStock) {
        try (FileWriter fileWriter = new FileWriter(archivo)) {
            fileWriter.append("ID,Nombre,Cantidad,StockMinimo,Proveedor\n");  // Añadir cabecera al CSV
            for (Producto producto : productosBajosStock) {
                fileWriter.append(producto.getId()).append(",")
                          .append(producto.getNombre()).append(",")
                          .append(String.valueOf(producto.getCantidad())).append(",")
                          .append(String.valueOf(producto.getStockMinimo())).append(",")
                          .append(producto.getProveedor()).append("\n");
            }
            System.out.println("Productos bajos de stock guardados en " + archivo);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }


    public List<Producto> getProductos() {
        return productos;
    }

    // Métodos para buscar productos
    public Producto buscarProductoPorId(String id) {
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                return producto;
            }
        }
        return null;
    }

    public Producto buscarProductoPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }
}


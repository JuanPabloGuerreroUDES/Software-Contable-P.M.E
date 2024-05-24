/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    public int ultimaIdVenta = 0;  // Inicializa la variable para mantener el último ID de venta.

    public GestorDeInventario() {
        this.productos = new ArrayList<>();
        cargarVentasDesdeCSV("ventas.csv"); // Asegúrate de cargar el último ID al iniciar el gestor.
    }

    public GestorDeInventario(List<Producto> productos) {
        this.productos = productos;
        cargarVentasDesdeCSV("ventas.csv"); // Asegúrate de cargar el último ID al iniciar el gestor.
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

    public void cargarVentasDesdeCSV(String archivo) {
        File file = new File(archivo);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length > 0) {
                        try {
                            int currentId = Integer.parseInt(data[0].trim()); // Elimina espacios en blanco.
                            ultimaIdVenta = Math.max(ultimaIdVenta, currentId);
                        } catch (NumberFormatException e) {
                            System.out.println("Error al parsear ID de venta: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
            }
        }
    }

    public void guardarVentaEnCSV(String archivo, int idVenta, String cliente, String detallesProductos, double totalValor) {
        System.out.println("Guardando venta: ID=" + idVenta + ", Cliente=" + cliente + ", Productos=" + detallesProductos + ", Total=" + totalValor + ", Estado=activo");
        File tempFile = new File("temp_" + archivo);
        File originalFile = new File(archivo);

        // Leer las líneas existentes
        List<String> existingLines = new ArrayList<>();
        if (originalFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(originalFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    existingLines.add(line);
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo de ventas: " + e.getMessage());
            }
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(tempFile))) {
            // Escribe la cabecera si el archivo está vacío
            if (existingLines.isEmpty()) {
                out.println("ID Venta,Cliente,Detalles Productos,Valor Total,Estado");
            }

            // Escribe la nueva venta al principio con estado "activo"
            out.printf("%d,%s,\"%s\",%.2f,activo%n", idVenta, cliente, detallesProductos, totalValor);

            // Copia las ventas antiguas
            for (String line : existingLines) {
                if (!line.startsWith("ID Venta")) {  // Evitar copiar la cabecera de nuevo
                    out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de ventas: " + e.getMessage());
        }

        // Reemplazar el archivo original con el nuevo
        if (originalFile.exists()) {
            originalFile.delete();
        }
        tempFile.renameTo(originalFile);
    }

    public boolean cambiarEstadoVenta(int idVenta, String nuevoEstado) {
        File archivoOriginal = new File("ventas.csv");
        File archivoTemporal = new File("temp_ventas.csv");

        boolean encontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(archivoOriginal));
             PrintWriter pw = new PrintWriter(new FileWriter(archivoTemporal))) {
            
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");
                if (campos.length >= 5 && campos[0].trim().equals(String.valueOf(idVenta))) {
                    // Cambiar el estado de la venta
                    campos[4] = nuevoEstado;
                    linea = String.join(",", campos);
                    encontrado = true;
                }
                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al modificar el archivo de ventas: " + e.getMessage());
            return false;
        }

        // Reemplazar el archivo original con el archivo temporal
        if (archivoOriginal.exists()) {
            archivoOriginal.delete();
        }
        archivoTemporal.renameTo(archivoOriginal);

        return encontrado;
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
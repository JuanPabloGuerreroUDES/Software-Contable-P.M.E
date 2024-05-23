/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author juang
 */
public class Producto {
    private String id;
    private String nombre;
    private int cantidad;
    private double precio;
    private int stockMinimo;
    private String proveedor;

    public Producto(String id, String nombre, int cantidad, double precio, int stockMinimo, String proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.stockMinimo = stockMinimo;
        this.proveedor = proveedor;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    

    @Override
    public String toString() {
        return id + "," + nombre + "," + cantidad + "," + precio + "," + stockMinimo + "," + proveedor;
    }
}

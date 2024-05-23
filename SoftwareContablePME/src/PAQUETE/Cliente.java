/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

/**
 *
 * @author juang
 */
public class Cliente {
    private String nombre;
    private String identificacion;
    private String numeroTelefono;

    public Cliente(String nombre, String identificacion, String numeroTelefono) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.numeroTelefono = numeroTelefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    @Override
    public String toString() {
        return nombre + "," + identificacion + "," + numeroTelefono;
    }
}

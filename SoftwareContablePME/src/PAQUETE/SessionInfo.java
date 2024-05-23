/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PAQUETE;

/**
 *
 * @author juang
 */
public class SessionInfo {
    public static int tipoUsuario; // 1 para usuario1, 2 para usuario2

    public static void setTipoUsuario(int tipo) {
        tipoUsuario = tipo;
    }

    public static int getTipoUsuario() {
        return tipoUsuario;
    }
}

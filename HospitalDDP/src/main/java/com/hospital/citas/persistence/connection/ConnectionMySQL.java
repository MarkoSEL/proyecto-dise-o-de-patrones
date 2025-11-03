/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.persistence.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * NOTA: Para que esto funcione, debes asegurarte de tener el driver de MySQL
 * (el archivo .jar) añadido a las "Libraries" del proyecto en NetBeans.
 */


/**
 * Implementación concreta del Patrón Factory para MySQL.
 */

public class ConnectionMySQL implements IConnection{

    // --- Datos de tu conexión ---
    // (Estos datos deberían ir en un archivo de propiedades, 
    // pero los ponemos aquí por simplicidad para el ejemplo)
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "db_hospital_citas"; // El nombre de tu BD
    private static final String USER = "root";
    private static final String PASS = "tu_password"; // Cambia esto
    // --- Fin de datos de conexión ---

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;
    private Connection cnx = null;

    @Override
    public Connection conectar() {
        try {
            // 1. Cargamos el Driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Establecemos la conexión
            cnx = DriverManager.getConnection(URL, USER, PASS);
            
            // (Opcional) Mensaje de éxito
            // System.out.println("Conexión a MySQL exitosa.");
            
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al cargar el Driver de MySQL: " + e.getMessage(), 
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Error al conectar a la base de datos: " + e.getMessage(), 
                "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
        return cnx;
    }

    @Override
    public void desconectar(Connection cn) {
        if (cn != null) {
            try {
                cn.close();
                // System.out.println("Desconectado de MySQL.");
            } catch (SQLException e) {
                 JOptionPane.showMessageDialog(null, 
                    "Error al cerrar la conexión: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}

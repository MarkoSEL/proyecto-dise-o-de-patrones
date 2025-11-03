/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.persistence.connection;

/**
 * Patrón Factory para crear conexiones.
 * * También aplica el Patrón Singleton para asegurar que solo exista
 * una instancia de esta fábrica en toda la aplicación.
 */
public class ConnectionFactory {
    // --- Implementación del Singleton ---
    
    // 1. Instancia única estática y privada
    private static ConnectionFactory instancia;

    // 2. Constructor privado para evitar instanciación externa
    private ConnectionFactory() { }

    // 3. Método estático público para obtener la instancia
    public static ConnectionFactory getInstancia() {
        if (instancia == null) {
            instancia = new ConnectionFactory();
        }
        return instancia;
    }
    
    // --- Fin del Singleton ---

    
    /**
     * El método Factory.
     * Recibe un tipo de motor y devuelve la implementación de conexión correcta.
     * * @param motorDB "MYSQL", "SQLSERVER", "POSTGRES", etc.
     * @return una instancia que implementa IConnection.
     */
    public IConnection getConexion(String motorDB) {
        
        if (motorDB == null) {
            System.err.println("No se especificó motor de DB. Retornando null.");
            return null;
        }

        // Usamos .equalsIgnoreCase() para ser flexibles (acepta "MYSQL", "mysql", "MySQL")
        if (motorDB.equalsIgnoreCase("MYSQL")) {
            return new ConnectionMySQL();
            
        } else if (motorDB.equalsIgnoreCase("SQLSERVER")) {
            // (Aquí iría "return new ConnectionSQLServer();" si la tuviéramos)
            System.err.println("Motor SQL Server no implementado.");
            return null;
            
        } else if (motorDB.equalsIgnoreCase("POSTGRES")) {
            // (Aquí iría "return new ConnectionPostgres();")
            System.err.println("Motor PostgreSQL no implementado.");
            return null;
        }
        
        // Si no coincide con ninguno
        System.err.println("El motor de DB '" + motorDB + "' no es soportado.");
        return null;
    }
}

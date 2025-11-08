/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hospital.citas.persistence.connection;

import java.sql.Connection;

/**
 * Interfaz que define el contrato para las conexiones a la base de datos.
 * Cumple con el Principio de Inversión de Dependencia (DIP),
 * ya que las capas superiores (DAO) dependerán de esta abstracción,
 * no de una implementación concreta.
 */


public interface IConnection {
    /**
     * Establece y retorna una conexión activa a la base de datos.
     * @return un objeto Connection de java.sql
     */
    Connection conectar();
    
    /**
     * Cierra la conexión a la base de datos.
     * @param cn La conexión a cerrar.
     */
    void desconectar(Connection cn);
}

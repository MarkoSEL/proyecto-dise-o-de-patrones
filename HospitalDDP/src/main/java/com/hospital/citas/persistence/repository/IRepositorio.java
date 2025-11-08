/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hospital.citas.persistence.repository;

import java.util.List;

/**
 * Interfaz Genérica (Patrón Repositorio)
 * Define las operaciones CRUD básicas.
 * Cumple con el Principio de Segregación de Interfaces (ISP).
 *
 * @param <T> La clase de Entidad (ej: Paciente, Medico)
 */
public interface IRepositorio<T> {
    /**
     * Inserta un nuevo objeto en la base de datos.
     * @param entidad El objeto a insertar.
     * @return true si fue exitoso, false si no.
     */
    boolean insertar(T entidad);

    /**
     * Actualiza un objeto existente en la base de datos.
     * @param entidad El objeto con los datos actualizados.
     * @return true si fue exitoso, false si no.
     */
    boolean actualizar(T entidad);
    
    /**
     * Elimina un registro de la base de datos por su ID.
     * @param id El ID del registro a eliminar.
     * @return true si fue exitoso, false si no.
     */
    boolean eliminar(int id);
    
    /**
     * Busca y retorna un objeto por su ID.
     * @param id El ID a buscar.
     * @return El objeto encontrado, o null si no existe.
     */
    T buscarPorId(int id);
    
    /**
     * Retorna una lista de todos los objetos de la tabla.
     * @return Una lista (List<T>) de objetos.
     */
    List<T> listarTodos();
}

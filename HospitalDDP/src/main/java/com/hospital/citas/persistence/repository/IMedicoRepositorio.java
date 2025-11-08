/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hospital.citas.persistence.repository;

import com.hospital.citas.model.entities.Medico;
import java.util.List;

/**
 * Interfaz específica para Medico.
 * Hereda el CRUD de IRepositorio<Medico> y añade métodos propios.
 * Cumple con el Principio Abierto/Cerrado (OCP).
 */
public interface IMedicoRepositorio extends IRepositorio<Medico> {
    // Método específico (requisito de tu plan)
    /**
     * Busca médicos que pertenezcan a una especialidad específica.
     * @param idEspecialidad El ID de la especialidad.
     * @return Una lista de Médicos.
     */
    List<Medico> buscarPorEspecialidad(int idEspecialidad);
    
    // (Podríamos añadir más, ej: buscarPorNombre)
}

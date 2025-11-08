/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hospital.citas.persistence.repository;

import com.hospital.citas.model.entities.Cita;
import java.time.LocalDateTime;
import java.util.List;

// Hereda el CRUD genérico
public interface ICitaRepositorio extends IRepositorio<Cita> {
    // Métodos específicos (requisito de tu plan)
    boolean verificarDisponibilidad(int idMedico, LocalDateTime fechaHora);
    List<Cita> buscarPorPaciente(int idPaciente);
    List<Cita> buscarPorFecha(LocalDateTime fecha);
}

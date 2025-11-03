/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hospital.citas.persistence.repository;

import com.hospital.citas.model.entities.Paciente;

/**
 *
 * @author ADRI-KI
 */
public interface IPacienteRepositorio extends IRepositorio<Paciente> {
    // Métodos específicos
    Paciente buscarPorDNI(String dni);
    Paciente buscarPorHistoriaClinica(String nroHistoria);
}

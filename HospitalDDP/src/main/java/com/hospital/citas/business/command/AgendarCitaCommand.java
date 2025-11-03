/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.business.command;

import com.hospital.citas.model.entities.Cita;
import com.hospital.citas.persistence.repository.ICitaRepositorio;

/**
 * Comando concreto para encapsular la acción de insertar una Cita.
 * Sigue el Principio de Responsabilidad Única (SRP)
 */
public class AgendarCitaCommand implements ICommand {
    private final Cita citaAGuardar;
    private final ICitaRepositorio citaDAO;

    /**
     * El comando necesita la cita que va a guardar y el DAO para guardarla.
     * @param citaAGuardar La entidad Cita (creada con el Builder)
     * @param citaDAO El DAO de la Capa 2
     */
    public AgendarCitaCommand(Cita citaAGuardar, ICitaRepositorio citaDAO) {
        this.citaAGuardar = citaAGuardar;
        this.citaDAO = citaDAO;
    }

    @Override
    public boolean execute() {
        // La única responsabilidad de este comando es llamar al método insertar del DAO.
        // Aquí se podría añadir logging, validaciones finales, etc.
        System.out.println("COMMAND: Ejecutando guardado de cita para: " + 
                            citaAGuardar.getPaciente().getUsuario().getNombre());
        
        return this.citaDAO.insertar(citaAGuardar);
    }
}

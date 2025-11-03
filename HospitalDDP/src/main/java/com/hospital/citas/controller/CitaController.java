/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.controller;

import com.hospital.citas.business.facade.AgendamientoCitaFacade;
import java.time.LocalDateTime;

/**
 * Patrón Controlador (GRASP).
 * Recibe las solicitudes de la capa de Vista (UI / JFrame)
 * y delega la tarea al Facade. No contiene lógica de negocio.
 */
public class CitaController {

    // El controlador depende del Facade
    private final AgendamientoCitaFacade facade;

    public CitaController() {
        this.facade = new AgendamientoCitaFacade();
    }

    /**
     * Este es el método que la VISTA (JFrame) llamará.
     * Recibe los datos (ej: de JTextBox, JComboBox) y los pasa al Facade.
     * * @return Un mensaje simple para mostrar al usuario (ej: en un JOptionPane).
     */
    public String procesarAgendamientoCita(String idPacienteStr, String idMedicoStr, 
                                           String idConsultorioStr, String idTerminalistaStr,
                                           LocalDateTime fechaHora, String nroActo, 
                                           int orden, int total) {
        
        try {
            // 1. Tarea del Controlador: Convertir y validar datos de la vista
            int idPaciente = Integer.parseInt(idPacienteStr);
            int idMedico = Integer.parseInt(idMedicoStr);
            int idConsultorio = Integer.parseInt(idConsultorioStr);
            int idTerminalista = Integer.parseInt(idTerminalistaStr);
            
            // 2. Tarea del Controlador: Delegar al Facade
            String respuesta = facade.agendarCita(
                idPaciente, idMedico, idConsultorio, idTerminalista,
                fechaHora, nroActo, orden, total
            );
            
            // 3. Tarea del Controlador: Retornar respuesta a la Vista
            return respuesta;

        } catch (NumberFormatException e) {
            return "Error: Los IDs deben ser números válidos.";
        } catch (Exception e) {
            return "Error en el Controlador: " + e.getMessage();
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.business.facade;

import com.hospital.citas.business.command.AgendarCitaCommand;
import com.hospital.citas.business.command.ICommand;
import com.hospital.citas.model.entities.Cita;
import com.hospital.citas.model.entities.Consultorio;
import com.hospital.citas.model.entities.Medico;
import com.hospital.citas.model.entities.Paciente;
import com.hospital.citas.model.entities.Usuario;
import com.hospital.citas.persistence.repository.*; // Importamos todas las interfaces DAO
import java.time.LocalDateTime;

/**
 * Patrón Facade (Fachada).
 * Simplifica el complejo proceso de agendamiento.
 * Es el cerebro de la operación, coordinando las Capas 1, 2 y 3.
 */
public class AgendamientoCitaFacade {

    // --- Dependencias (DIP) ---
    // El Facade depende de las ABSTRACCIONES de la Capa 2 (las interfaces)
    private final IPacienteRepositorio pacienteDAO;
    private final IMedicoRepositorio medicoDAO;
    private final ICitaRepositorio citaDAO;
    // (Añadimos DAOs para las otras entidades)
    private final IRepositorio<Consultorio> consultorioDAO; 
    private final IRepositorio<Usuario> usuarioDAO;

    // --- Constructor ---
    // Usamos Inyección de Dependencias (en un framework se haría automático)
    public AgendamientoCitaFacade() {
        // En este ejemplo, instanciamos los DAOs aquí
        // (En un proyecto avanzado, esto lo manejaría un framework como Spring)
        this.pacienteDAO = new PacienteDAO(); // Asumimos que existen PacienteDAO, etc.
        this.medicoDAO = new MedicoDAO();
        this.citaDAO = new CitaDAO();         // Asumimos que existe CitaDAO
        this.consultorioDAO = new ConsultorioDAO(); // Asumimos que existe ConsultorioDAO
        this.usuarioDAO = new UsuarioDAO();     // Asumimos que existe UsuarioDAO
    }

    /**
     * El método principal del Facade. Recibe IDs y datos crudos.
     * Retorna un mensaje de éxito o error.
     */
    public String agendarCita(int idPaciente, int idMedico, int idConsultorio, int idTerminalista, 
                              LocalDateTime fechaHoraCita, String nroActoMedico, 
                              int nroOrden, int totalOrden) {
        
        try {
            // --- 1. Coordinación: Búsqueda de entidades (usando DAOs de Capa 2) ---
            
            // 1a. Buscar Paciente
            Paciente paciente = pacienteDAO.buscarPorId(idPaciente);
            if (paciente == null) return "Error: Paciente no encontrado.";

            // 1b. Buscar Medico
            Medico medico = medicoDAO.buscarPorId(idMedico);
            if (medico == null) return "Error: Médico no encontrado.";

            // 1c. Buscar Consultorio
            Consultorio consultorio = consultorioDAO.buscarPorId(idConsultorio);
            if (consultorio == null) return "Error: Consultorio no encontrado.";

            // 1d. Buscar Terminalista (el Usuario que registra)
            Usuario terminalista = usuarioDAO.buscarPorId(idTerminalista);
            if (terminalista == null) return "Error: Terminalista no válido.";

            // --- 2. Coordinación: Lógica de Negocio (Validación) ---
            
            // 2a. Verificar disponibilidad (método específico de ICitaRepositorio)
            if (!citaDAO.verificarDisponibilidad(idMedico, fechaHoraCita)) {
                return "Error: Médico no disponible en esa fecha/hora.";
            }

            // --- 3. Coordinación: Creación de la Entidad (usando Builders de Capa 1) ---
            Cita nuevaCita = new Cita.Builder(0, fechaHoraCita, paciente, medico, consultorio, terminalista)
                                    .conNroActoMedico(nroActoMedico)
                                    .conCodigoAutogenerado("CODE" + idPaciente + fechaHoraCita.getYear()) // Lógica de ejemplo
                                    .conEstado("CONFIRMADA")
                                    .conNroOrden(nroOrden)
                                    .conTotalOrden(totalOrden)
                                    // .conTipoAtencion(...) // (Si se necesitara)
                                    .build();

            // --- 4. Coordinación: Ejecución (usando Patrón Command) ---
            ICommand comando = new AgendarCitaCommand(nuevaCita, citaDAO);
            boolean resultado = comando.execute();

            // --- 5. Respuesta ---
            if (resultado) {
                return "Éxito: Cita registrada para " + paciente.getUsuario().getNombre();
            } else {
                return "Error: No se pudo guardar la cita en la BD.";
            }

        } catch (Exception e) {
            // Manejo de cualquier error inesperado
            return "Error inesperado en el Facade: " + e.getMessage();
        }
    }
    
    // (Aquí irían otros métodos del Facade, ej: cancelarCita(...))
}

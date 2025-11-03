/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.model.entities;

import java.time.LocalDateTime; // Usamos la clase moderna para fecha y hora

public class Cita {

    private final int idCita;
    private final LocalDateTime fechaHoraCita;
    private final LocalDateTime fechaCreacion;
    private final String nroActoMedico;
    private final String codigoAutogenerado;
    private final String estado;
    private final int nroOrden;
    private final int totalOrden;

    // Relaciones (los objetos completos)
    private final Paciente paciente;
    private final Medico medico;
    private final Consultorio consultorio;
    private final TipoAtencion tipoAtencion;
    private final Usuario terminalista; // Quien registró la cita

    // Constructor privado
    private Cita(Builder builder) {
        this.idCita = builder.idCita;
        this.fechaHoraCita = builder.fechaHoraCita;
        this.fechaCreacion = builder.fechaCreacion;
        this.nroActoMedico = builder.nroActoMedico;
        this.codigoAutogenerado = builder.codigoAutogenerado;
        this.estado = builder.estado;
        this.nroOrden = builder.nroOrden;
        this.totalOrden = builder.totalOrden;
        this.paciente = builder.paciente;
        this.medico = builder.medico;
        this.consultorio = builder.consultorio;
        this.tipoAtencion = builder.tipoAtencion;
        this.terminalista = builder.terminalista;
    }

    // --- Getters (Omitidos por brevedad) ---

    // --- Clase Builder Interna Estática ---
    public static class Builder {

        // Campos Obligatorios para una cita
        private final int idCita;
        private final LocalDateTime fechaHoraCita;
        private final Paciente paciente;
        private final Medico medico;
        private final Consultorio consultorio;
        private final Usuario terminalista;

        // Campos Opcionales
        private LocalDateTime fechaCreacion = LocalDateTime.now(); // Valor por defecto
        private String nroActoMedico;
        private String codigoAutogenerado;
        private String estado = "PENDIENTE"; // Valor por defecto
        private int nroOrden;
        private int totalOrden;
        private TipoAtencion tipoAtencion;

        public Builder(int idCita, LocalDateTime fechaHoraCita, Paciente paciente, Medico medico, Consultorio consultorio, Usuario terminalista) {
            this.idCita = idCita;
            this.fechaHoraCita = fechaHoraCita;
            this.paciente = paciente;
            this.medico = medico;
            this.consultorio = consultorio;
            this.terminalista = terminalista;
        }

        // Métodos "fluent"
        public Builder conFechaCreacion(LocalDateTime val) { this.fechaCreacion = val; return this; }
        public Builder conNroActoMedico(String val) { this.nroActoMedico = val; return this; }
        public Builder conCodigoAutogenerado(String val) { this.codigoAutogenerado = val; return this; }
        public Builder conEstado(String val) { this.estado = val; return this; }
        public Builder conNroOrden(int val) { this.nroOrden = val; return this; }
        public Builder conTotalOrden(int val) { this.totalOrden = val; return this; }
        public Builder conTipoAtencion(TipoAtencion val) { this.tipoAtencion = val; return this; }

        public Cita build() {
            return new Cita(this);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.model.entities;

/**
 *
 * @author ADRI-KI
 */
public class Medico {
    private final int idMedico;
    private final String cmp; // Colegiatura Médica
    private final Usuario usuario; // Datos personales (Nombre, Apellido)
    private final Especialidad especialidad; // Su especialidad

    private Medico(Builder builder) {
        this.idMedico = builder.idMedico;
        this.cmp = builder.cmp;
        this.usuario = builder.usuario;
        this.especialidad = builder.especialidad;
    }

    // --- Getters ---
    public int getIdMedico() { return idMedico; }
    public String getCmp() { return cmp; }
    public Usuario getUsuario() { return usuario; }
    public Especialidad getEspecialidad() { return especialidad; }

    // --- Clase Builder Interna Estática ---
    public static class Builder {

        // Campos obligatorios
        private final int idMedico;
        private final Usuario usuario;
        private final Especialidad especialidad;
        
        // Campos opcionales
        private String cmp;

        public Builder(int idMedico, Usuario usuario, Especialidad especialidad) {
            this.idMedico = idMedico;
            this.usuario = usuario;
            this.especialidad = especialidad;
        }

        public Builder conCmp(String cmp) {
            this.cmp = cmp;
            return this;
        }

        public Medico build() {
            return new Medico(this);
        }
    }
}

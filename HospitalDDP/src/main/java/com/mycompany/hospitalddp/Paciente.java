/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.model.entities;

/**
 *
 * @author ADRI-KI
 */
public class Paciente {
    private final int idPaciente;
    private final String nroHistoriaClinica;
    private final String dni;
    private final String tipoAsegurado;
    private final String parentesco;
    private final String tipoPaciente;
    private final Usuario usuario; // El objeto Usuario con sus datos (nombre, apellido)

    // El constructor es PRIVADO. Solo el Builder puede llamarlo.
    private Paciente(Builder builder) {
        this.idPaciente = builder.idPaciente;
        this.nroHistoriaClinica = builder.nroHistoriaClinica;
        this.dni = builder.dni;
        this.tipoAsegurado = builder.tipoAsegurado;
        this.parentesco = builder.parentesco;
        this.tipoPaciente = builder.tipoPaciente;
        this.usuario = builder.usuario;
    }

    // --- Solo Getters (para inmutabilidad) ---
    public int getIdPaciente() { return idPaciente; }
    public String getNroHistoriaClinica() { return nroHistoriaClinica; }
    public String getDni() { return dni; }
    public String getTipoAsegurado() { return tipoAsegurado; }
    public String getParentesco() { return parentesco; }
    public String getTipoPaciente() { return tipoPaciente; }
    public Usuario getUsuario() { return usuario; }

    // --- Clase Builder Interna Estática ---
    public static class Builder {

        // Atributos del Builder
        private int idPaciente;
        private String nroHistoriaClinica;
        private String dni;
        private String tipoAsegurado;
        private String parentesco;
        private String tipoPaciente;
        private Usuario usuario;

        // Constructor del Builder (podemos poner campos obligatorios aquí)
        public Builder(int idPaciente, String nroHistoriaClinica, Usuario usuario) {
            this.idPaciente = idPaciente;
            this.nroHistoriaClinica = nroHistoriaClinica;
            this.usuario = usuario;
        }

        // Métodos "fluent" para setear los atributos opcionales
        public Builder conDni(String dni) {
            this.dni = dni;
            return this; // Retorna el propio builder
        }
        
        public Builder conTipoAsegurado(String tipoAsegurado) {
            this.tipoAsegurado = tipoAsegurado;
            return this;
        }

        public Builder conParentesco(String parentesco) {
            this.parentesco = parentesco;
            return this;
        }

        public Builder conTipoPaciente(String tipoPaciente) {
            this.tipoPaciente = tipoPaciente;
            return this;
        }

        // El método final que construye el objeto Paciente
        public Paciente build() {
            // Aquí podríamos agregar validaciones antes de crear el objeto
            return new Paciente(this);
        }
    }
}

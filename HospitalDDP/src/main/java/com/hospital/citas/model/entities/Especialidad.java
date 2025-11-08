/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.model.entities;

public class Especialidad {
    private int idEspecialidad;
    private String nombre; // Ej: "CEXT - CARDIOLOGIA"

    public Especialidad(int idEspecialidad, String nombre) {
        this.idEspecialidad = idEspecialidad;
        this.nombre = nombre;
    }
    
    // --- Getters y Setters ---
    public int getIdEspecialidad() { return idEspecialidad; }
    public String getNombre() { return nombre; }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.model.entities;

/**
 *
 * @author ADRI-KI
 */
public class Consultorio {
    private int idConsultorio;
    private String codigo; // Ej: "ECO1"

    public Consultorio(int idConsultorio, String codigo) {
        this.idConsultorio = idConsultorio;
        this.codigo = codigo;
    }
    
    // --- Getters y Setters ---
    public int getIdConsultorio() { return idConsultorio; }
    public String getCodigo() { return codigo; }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.model.entities;

public class TipoAtencion {
    private int idTipoAtencion;
    private String descCorta; // Ej: "ATENC.PROCEDIM."
    private String descLarga; // Ej: "ECOCARDIOGRAMA TRANSTORAC"

    public TipoAtencion(int idTipoAtencion, String descCorta, String descLarga) {
        this.idTipoAtencion = idTipoAtencion;
        this.descCorta = descCorta;
        this.descLarga = descLarga;
    }
    
    // --- Getters y Setters ---
    public int getIdTipoAtencion() { return idTipoAtencion; }
    public String getDescCorta() { return descCorta; }
    public String getDescLarga() { return descLarga; }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hospital.citas.business.command;

/**
 * Interfaz para el Patrón Command.
 * Define una operación que puede ser ejecutada.
 */
public interface ICommand {
    /**
     * Ejecuta la acción del comando.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean execute();
    
    // Opcional: Podríamos añadir un método undo() si el plan lo requiriera
    // void undo();
}

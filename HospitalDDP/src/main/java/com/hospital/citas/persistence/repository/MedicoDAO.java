/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.citas.persistence.repository;

import com.hospital.citas.model.entities.Especialidad;
import com.hospital.citas.model.entities.Medico;
import com.hospital.citas.model.entities.Usuario;
import com.hospital.citas.persistence.connection.ConnectionFactory;
import com.hospital.citas.persistence.connection.IConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Implementación concreta del DAO para la entidad Medico.
 * Implementa la interfaz específica IMedicoRepositorio.
 */
public class MedicoDAO implements IMedicoRepositorio {

    // --- Dependencias (DIP) ---
    // Depende de las abstracciones de la Capa 3, no de clases concretas
    private final IConnection conexionDB;
    private Connection cnx;

    public MedicoDAO() {
        // 1. Obtenemos la Fábrica (Singleton)
        ConnectionFactory factory = ConnectionFactory.getInstancia();
        // 2. Pedimos la conexión que necesitamos (MySQL)
        this.conexionDB = factory.getConexion("MYSQL");
    }

    /**
     * Este método privado se encarga de "construir" un objeto Medico
     * completo a partir de un ResultSet.
     * ¡Aquí usamos los Builders de la Capa 1!
     */
    private Medico construirMedicoDesdeResultSet(ResultSet rs) throws SQLException {
        // 1. Construimos los objetos base
        Usuario u = new Usuario(
            rs.getInt("u_id_usuario"),
            rs.getString("u_nombre"),
            rs.getString("u_apellido"),
            rs.getString("u_correo"),
            rs.getString("u_rol")
        );
        
        Especialidad e = new Especialidad(
            rs.getInt("e_id_especialidad"),
            rs.getString("e_nombre")
        );

        // 2. Usamos el Builder de Medico (Capa 1)
        return new Medico.Builder(rs.getInt("m_id_medico"), u, e)
                         .conCmp(rs.getString("m_cmp"))
                         .build();
    }
    
    // --- Implementación del CRUD Básico ---

    @Override
    public boolean insertar(Medico medico) {
        String sql = "INSERT INTO Doctores (id_usuario, id_especialidad, cmp) VALUES (?, ?, ?)";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, medico.getUsuario().getIdUsuario());
            ps.setInt(2, medico.getEspecialidad().getIdEspecialidad());
            ps.setString(3, medico.getCmp());
            
            int filasAfectadas = ps.executeUpdate();
            
            // Opcional: Recuperar el ID generado (si la PK es autoincremental)
            // if (filasAfectadas > 0) { ... }

            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar médico: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean actualizar(Medico medico) {
        String sql = "UPDATE Doctores SET id_usuario = ?, id_especialidad = ?, cmp = ? WHERE id_doctor = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            
            ps.setInt(1, medico.getUsuario().getIdUsuario());
            ps.setInt(2, medico.getEspecialidad().getIdEspecialidad());
            ps.setString(3, medico.getCmp());
            ps.setInt(4, medico.getIdMedico());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar médico: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Doctores WHERE id_doctor = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar médico: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public Medico buscarPorId(int id) {
        // Unimos las 3 tablas (Doctores, Usuarios, Especialidades)
        String sql = "SELECT " +
                     "d.id_doctor AS m_id_medico, d.cmp AS m_cmp, " +
                     "u.id_usuario AS u_id_usuario, u.nombre AS u_nombre, u.apellido AS u_apellido, u.correo AS u_correo, u.rol AS u_rol, " +
                     "e.id_especialidad AS e_id_especialidad, e.nombre AS e_nombre " +
                     "FROM Doctores d " +
                     "JOIN Usuarios u ON d.id_usuario = u.id_usuario " +
                     "JOIN Especialidades e ON d.id_especialidad = e.id_especialidad " +
                     "WHERE d.id_doctor = ?";
        
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Usamos el método helper y los builders
                return construirMedicoDesdeResultSet(rs);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar médico: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return null; // No encontrado
    }

    @Override
    public List<Medico> listarTodos() {
        String sql = "SELECT " +
                     "d.id_doctor AS m_id_medico, d.cmp AS m_cmp, " +
                     "u.id_usuario AS u_id_usuario, u.nombre AS u_nombre, u.apellido AS u_apellido, u.correo AS u_correo, u.rol AS u_rol, " +
                     "e.id_especialidad AS e_id_especialidad, e.nombre AS e_nombre " +
                     "FROM Doctores d " +
                     "JOIN Usuarios u ON d.id_usuario = u.id_usuario " +
                     "JOIN Especialidades e ON d.id_especialidad = e.id_especialidad";
        
        List<Medico> lista = new ArrayList<>();
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirMedicoDesdeResultSet(rs));
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar médicos: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }

    // --- Implementación del Método Específico ---
    
    @Override
    public List<Medico> buscarPorEspecialidad(int idEspecialidad) {
        String sql = "SELECT " +
                     "d.id_doctor AS m_id_medico, d.cmp AS m_cmp, " +
                     "u.id_usuario AS u_id_usuario, u.nombre AS u_nombre, u.apellido AS u_apellido, u.correo AS u_correo, u.rol AS u_rol, " +
                     "e.id_especialidad AS e_id_especialidad, e.nombre AS e_nombre " +
                     "FROM Doctores d " +
                     "JOIN Usuarios u ON d.id_usuario = u.id_usuario " +
                     "JOIN Especialidades e ON d.id_especialidad = e.id_especialidad " +
                     "WHERE d.id_especialidad = ?"; // La condición específica
        
        List<Medico> lista = new ArrayList<>();
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, idEspecialidad);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirMedicoDesdeResultSet(rs));
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar por especialidad: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }
}

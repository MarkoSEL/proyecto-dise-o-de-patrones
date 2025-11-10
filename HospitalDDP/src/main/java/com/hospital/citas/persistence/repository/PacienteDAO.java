package com.hospital.citas.persistence.repository;

import com.hospital.citas.model.entities.Paciente;
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
 * Implementación concreta del DAO para la entidad Paciente.
 * Implementa la interfaz específica IPacienteRepositorio.
 */
public class PacienteDAO implements IPacienteRepositorio {

    private final IConnection conexionDB;
    private Connection cnx;

    public PacienteDAO() {
        // 1. Obtenemos la Fábrica (Singleton)
        ConnectionFactory factory = ConnectionFactory.getInstancia();
        // 2. Pedimos la conexión que necesitamos (MySQL)
        this.conexionDB = factory.getConexion("MYSQL");
    }

    /**
     * Método helper para construir un Paciente desde un ResultSet.
     * Utiliza el Builder de la Capa 1.
     */
    private Paciente construirPacienteDesdeResultSet(ResultSet rs) throws SQLException {
        // 1. Construimos el Usuario base
        Usuario u = new Usuario(
            rs.getInt("u_id_usuario"),
            rs.getString("u_nombre"),
            rs.getString("u_apellido"),
            rs.getString("u_correo"),
            rs.getString("u_rol")
        );

        // 2. Usamos el Builder de Paciente (Capa 1)
        return new Paciente.Builder(rs.getInt("p_id_paciente"), rs.getString("p_nro_historia_clinica"), u)
                           .conDni(rs.getString("p_dni"))
                           .conTipoAsegurado(rs.getString("p_tipo_asegurado"))
                           .conParentesco(rs.getString("p_parentesco"))
                           .conTipoPaciente(rs.getString("p_tipo_paciente"))
                           .build();
    }
    
    // --- Implementación del CRUD Básico ---

    @Override
    public boolean insertar(Paciente paciente) {
        // Asumimos que el Usuario ya fue insertado y tenemos su ID
        String sql = "INSERT INTO Pacientes (id_usuario, nro_historia_clinica, dni, tipo_asegurado, parentesco, tipo_paciente) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, paciente.getUsuario().getIdUsuario());
            ps.setString(2, paciente.getNroHistoriaClinica());
            ps.setString(3, paciente.getDni());
            ps.setString(4, paciente.getTipoAsegurado());
            ps.setString(5, paciente.getParentesco());
            ps.setString(6, paciente.getTipoPaciente());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar paciente: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean actualizar(Paciente paciente) {
        String sql = "UPDATE Pacientes SET id_usuario = ?, nro_historia_clinica = ?, dni = ?, tipo_asegurado = ?, parentesco = ?, tipo_paciente = ? WHERE id_paciente = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            
            ps.setInt(1, paciente.getUsuario().getIdUsuario());
            ps.setString(2, paciente.getNroHistoriaClinica());
            ps.setString(3, paciente.getDni());
            ps.setString(4, paciente.getTipoAsegurado());
            ps.setString(5, paciente.getParentesco());
            ps.setString(6, paciente.getTipoPaciente());
            ps.setInt(7, paciente.getIdPaciente());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar paciente: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Ojo: Esto podría requerir lógica de eliminación en cascada o validaciones
        String sql = "DELETE FROM Pacientes WHERE id_paciente = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar paciente: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public Paciente buscarPorId(int id) {
        String sql = "SELECT " +
                     "p.id_paciente AS p_id_paciente, p.nro_historia_clinica AS p_nro_historia_clinica, " +
                     "p.dni AS p_dni, p.tipo_asegurado AS p_tipo_asegurado, p.parentesco AS p_parentesco, p.tipo_paciente AS p_tipo_paciente, " +
                     "u.id_usuario AS u_id_usuario, u.nombre AS u_nombre, u.apellido AS u_apellido, u.correo AS u_correo, u.rol AS u_rol " +
                     "FROM Pacientes p " +
                     "JOIN Usuarios u ON p.id_usuario = u.id_usuario " +
                     "WHERE p.id_paciente = ?";
        
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPacienteDesdeResultSet(rs);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar paciente: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return null; // No encontrado
    }

    @Override
    public List<Paciente> listarTodos() {
        String sql = "SELECT " +
                     "p.id_paciente AS p_id_paciente, p.nro_historia_clinica AS p_nro_historia_clinica, " +
                     "p.dni AS p_dni, p.tipo_asegurado AS p_tipo_asegurado, p.parentesco AS p_parentesco, p.tipo_paciente AS p_tipo_paciente, " +
                     "u.id_usuario AS u_id_usuario, u.nombre AS u_nombre, u.apellido AS u_apellido, u.correo AS u_correo, u.rol AS u_rol " +
                     "FROM Pacientes p " +
                     "JOIN Usuarios u ON p.id_usuario = u.id_usuario";
        
        List<Paciente> lista = new ArrayList<>();
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirPacienteDesdeResultSet(rs));
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar pacientes: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }

    // --- Implementación de Métodos Específicos ---

    @Override
    public Paciente buscarPorDNI(String dni) {
        String sql = "SELECT " +
                     "p.id_paciente AS p_id_paciente, p.nro_historia_clinica AS p_nro_historia_clinica, " +
                     "p.dni AS p_dni, p.tipo_asegurado AS p_tipo_asegurado, p.parentesco AS p_parentesco, p.tipo_paciente AS p_tipo_paciente, " +
                     "u.id_usuario AS u_id_usuario, u.nombre AS u_nombre, u.apellido AS u_apellido, u.correo AS u_correo, u.rol AS u_rol " +
                     "FROM Pacientes p " +
                     "JOIN Usuarios u ON p.id_usuario = u.id_usuario " +
                     "WHERE p.dni = ?";
        
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, dni);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPacienteDesdeResultSet(rs);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar paciente por DNI: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return null; // No encontrado
    }

    @Override
    public Paciente buscarPorHistoriaClinica(String nroHistoria) {
         String sql = "SELECT " +
                     "p.id_paciente AS p_id_paciente, p.nro_historia_clinica AS p_nro_historia_clinica, " +
                     "p.dni AS p_dni, p.tipo_asegurado AS p_tipo_asegurado, p.parentesco AS p_parentesco, p.tipo_paciente AS p_tipo_paciente, " +
                     "u.id_usuario AS u_id_usuario, u.nombre AS u_nombre, u.apellido AS u_apellido, u.correo AS u_correo, u.rol AS u_rol " +
                     "FROM Pacientes p " +
                     "JOIN Usuarios u ON p.id_usuario = u.id_usuario " +
                     "WHERE p.nro_historia_clinica = ?";
        
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, nroHistoria);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirPacienteDesdeResultSet(rs);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar paciente por H.C.: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return null; // No encontrado
    }
}

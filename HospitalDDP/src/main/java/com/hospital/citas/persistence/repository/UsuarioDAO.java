package com.hospital.citas.persistence.repository;

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
 * Implementación concreta del DAO para la entidad Usuario.
 * Implementa la interfaz genérica IRepositorio<Usuario>.
 */
public class UsuarioDAO implements IRepositorio<Usuario> {
    
    private final IConnection conexionDB;
    private Connection cnx;

    public UsuarioDAO() {
        ConnectionFactory factory = ConnectionFactory.getInstancia();
        this.conexionDB = factory.getConexion("MYSQL");
    }

    private Usuario construirUsuarioDesdeResultSet(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id_usuario"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("correo"),
            rs.getString("rol")
        );
    }
    
    @Override
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombre, apellido, correo, password, rol) VALUES (?, ?, ?, ?, ?)";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, "12345"); // Password de ejemplo, ¡debería estar encriptada!
            ps.setString(5, usuario.getRol());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar usuario: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE Usuarios SET nombre = ?, apellido = ?, correo = ?, rol = ? WHERE id_usuario = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getRol());
            ps.setInt(5, usuario.getIdUsuario());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Usuarios WHERE id_usuario = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE id_usuario = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirUsuarioDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM Usuarios";
        List<Usuario> lista = new ArrayList<>();
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirUsuarioDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar usuarios: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }
}

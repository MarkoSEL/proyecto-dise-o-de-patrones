package com.hospital.citas.persistence.repository;

import com.hospital.citas.model.entities.Consultorio;
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
 * Implementación concreta del DAO para la entidad Consultorio.
 * Implementa la interfaz genérica IRepositorio<Consultorio>.
 */
public class ConsultorioDAO implements IRepositorio<Consultorio> {

    private final IConnection conexionDB;
    private Connection cnx;

    public ConsultorioDAO() {
        ConnectionFactory factory = ConnectionFactory.getInstancia();
        this.conexionDB = factory.getConexion("MYSQL");
    }
    
    private Consultorio construirConsultorioDesdeResultSet(ResultSet rs) throws SQLException {
        return new Consultorio(
            rs.getInt("id_consultorio"),
            rs.getString("codigo")
        );
    }
    
    @Override
    public boolean insertar(Consultorio consultorio) {
        String sql = "INSERT INTO Consultorios (codigo) VALUES (?)";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, consultorio.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar consultorio: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean actualizar(Consultorio consultorio) {
        String sql = "UPDATE Consultorios SET codigo = ? WHERE id_consultorio = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, consultorio.getCodigo());
            ps.setInt(2, consultorio.getIdConsultorio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar consultorio: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Consultorios WHERE id_consultorio = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar consultorio: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public Consultorio buscarPorId(int id) {
        String sql = "SELECT * FROM Consultorios WHERE id_consultorio = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return construirConsultorioDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar consultorio: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return null;
    }

    @Override
    public List<Consultorio> listarTodos() {
        String sql = "SELECT * FROM Consultorios";
        List<Consultorio> lista = new ArrayList<>();
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(construirConsultorioDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar consultorios: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }
}

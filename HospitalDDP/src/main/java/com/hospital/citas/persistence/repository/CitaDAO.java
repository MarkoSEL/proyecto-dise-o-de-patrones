package com.hospital.citas.persistence.repository;

import com.hospital.citas.model.entities.*; // Importamos todas las entidades
import com.hospital.citas.persistence.connection.ConnectionFactory;
import com.hospital.citas.persistence.connection.IConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Implementación concreta del DAO para la entidad Cita.
 * Implementa la interfaz específica ICitaRepositorio.
 */
public class CitaDAO implements ICitaRepositorio {

    private final IConnection conexionDB;
    private Connection cnx;

    public CitaDAO() {
        ConnectionFactory factory = ConnectionFactory.getInstancia();
        this.conexionDB = factory.getConexion("MYSQL");
    }

    /**
     * Query base para unir todas las tablas necesarias para construir una Cita.
     * Es complejo, por eso se define una sola vez.
     */
    private final String SQL_SELECT_CITAS = "SELECT " +
        "c.id_cita, c.fecha_hora_cita, c.fecha_creacion, c.nro_acto_medico, c.codigo_autogenerado, c.estado, c.nro_orden, c.total_orden, " +
        "p.id_paciente AS p_id_paciente, p.nro_historia_clinica AS p_nro_historia_clinica, p.dni AS p_dni, p.tipo_asegurado AS p_tipo_asegurado, p.parentesco AS p_parentesco, p.tipo_paciente AS p_tipo_paciente, " +
        "up.id_usuario AS up_id_usuario, up.nombre AS up_nombre, up.apellido AS up_apellido, up.correo AS up_correo, up.rol AS up_rol, " +
        "m.id_doctor AS m_id_medico, m.cmp AS m_cmp, " +
        "um.id_usuario AS um_id_usuario, um.nombre AS um_nombre, um.apellido AS um_apellido, um.correo AS um_correo, um.rol AS um_rol, " +
        "e.id_especialidad AS e_id_especialidad, e.nombre AS e_nombre, " +
        "con.id_consultorio AS con_id_consultorio, con.codigo AS con_codigo, " +
        "ta.id_tipo_atencion AS ta_id_tipo_atencion, ta.descripcion_corta AS ta_desc_corta, ta.descripcion_larga AS ta_desc_larga, " +
        "ut.id_usuario AS ut_id_usuario, ut.nombre AS ut_nombre, ut.apellido AS ut_apellido, ut.correo AS ut_correo, ut.rol AS ut_rol " +
        "FROM Citas c " +
        "JOIN Pacientes p ON c.id_paciente = p.id_paciente " +
        "JOIN Usuarios up ON p.id_usuario = up.id_usuario " +
        "JOIN Doctores m ON c.id_doctor = m.id_doctor " +
        "JOIN Usuarios um ON m.id_usuario = um.id_usuario " +
        "JOIN Especialidades e ON m.id_especialidad = e.id_especialidad " +
        "JOIN Consultorios con ON c.id_consultorio = con.id_consultorio " +
        "JOIN Usuarios ut ON c.id_terminalista = ut.id_usuario " +
        "LEFT JOIN TipoAtencion ta ON c.id_tipo_atencion = ta.id_tipo_atencion "; // LEFT JOIN por si es opcional


    /**
     * Método helper para construir una Cita completa desde un ResultSet.
     * Utiliza los Builders de la Capa 1.
     */
    private Cita construirCitaDesdeResultSet(ResultSet rs) throws SQLException {
        
        // 1. Construir Usuario (Paciente)
        Usuario uPaciente = new Usuario(
            rs.getInt("up_id_usuario"), rs.getString("up_nombre"), rs.getString("up_apellido"),
            rs.getString("up_correo"), rs.getString("up_rol")
        );

        // 2. Construir Paciente
        Paciente paciente = new Paciente.Builder(rs.getInt("p_id_paciente"), rs.getString("p_nro_historia_clinica"), uPaciente)
            .conDni(rs.getString("p_dni"))
            .conTipoAsegurado(rs.getString("p_tipo_asegurado"))
            .conParentesco(rs.getString("p_parentesco"))
            .conTipoPaciente(rs.getString("p_tipo_paciente"))
            .build();

        // 3. Construir Usuario (Medico)
        Usuario uMedico = new Usuario(
            rs.getInt("um_id_usuario"), rs.getString("um_nombre"), rs.getString("um_apellido"),
            rs.getString("um_correo"), rs.getString("um_rol")
        );

        // 4. Construir Especialidad
        Especialidad especialidad = new Especialidad(rs.getInt("e_id_especialidad"), rs.getString("e_nombre"));

        // 5. Construir Medico
        Medico medico = new Medico.Builder(rs.getInt("m_id_medico"), uMedico, especialidad)
            .conCmp(rs.getString("m_cmp"))
            .build();

        // 6. Construir Consultorio
        Consultorio consultorio = new Consultorio(rs.getInt("con_id_consultorio"), rs.getString("con_codigo"));

        // 7. Construir Usuario (Terminalista)
        Usuario terminalista = new Usuario(
            rs.getInt("ut_id_usuario"), rs.getString("ut_nombre"), rs.getString("ut_apellido"),
            rs.getString("ut_correo"), rs.getString("ut_rol")
        );
        
        // 8. Construir TipoAtencion (puede ser nulo)
        TipoAtencion tipoAtencion = null;
        if(rs.getObject("ta_id_tipo_atencion") != null) {
            tipoAtencion = new TipoAtencion(
                rs.getInt("ta_id_tipo_atencion"), 
                rs.getString("ta_desc_corta"), 
                rs.getString("ta_desc_larga")
            );
        }

        // 9. Construir Cita (final)
        Cita.Builder citaBuilder = new Cita.Builder(
            rs.getInt("id_cita"),
            rs.getTimestamp("fecha_hora_cita").toLocalDateTime(),
            paciente, medico, consultorio, terminalista
        )
        .conFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime())
        .conNroActoMedico(rs.getString("nro_acto_medico"))
        .conCodigoAutogenerado(rs.getString("codigo_autogenerado"))
        .conEstado(rs.getString("estado"))
        .conNroOrden(rs.getInt("nro_orden"))
        .conTotalOrden(rs.getInt("total_orden"));
        
        if (tipoAtencion != null) {
            citaBuilder.conTipoAtencion(tipoAtencion);
        }

        return citaBuilder.build();
    }


    @Override
    public boolean insertar(Cita cita) {
        String sql = "INSERT INTO Citas (id_paciente, id_doctor, id_consultorio, id_terminalista, id_tipo_atencion, " +
                     "fecha_hora_cita, fecha_creacion, nro_acto_medico, codigo_autogenerado, estado, nro_orden, total_orden) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, cita.getPaciente().getIdPaciente());
            ps.setInt(2, cita.getMedico().getIdMedico());
            ps.setInt(3, cita.getConsultorio().getIdConsultorio());
            ps.setInt(4, cita.getTerminalista().getIdUsuario());
            
            if (cita.getTipoAtencion() != null) {
                ps.setInt(5, cita.getTipoAtencion().getIdTipoAtencion());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            
            ps.setTimestamp(6, Timestamp.valueOf(cita.getFechaHoraCita()));
            ps.setTimestamp(7, Timestamp.valueOf(cita.getFechaCreacion()));
            ps.setString(8, cita.getNroActoMedico());
            ps.setString(9, cita.getCodigoAutogenerado());
            ps.setString(10, cita.getEstado());
            ps.setInt(11, cita.getNroOrden());
            ps.setInt(12, cita.getTotalOrden());

            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar cita: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean actualizar(Cita cita) {
        String sql = "UPDATE Citas SET id_paciente = ?, id_doctor = ?, id_consultorio = ?, id_terminalista = ?, id_tipo_atencion = ?, " +
                     "fecha_hora_cita = ?, fecha_creacion = ?, nro_acto_medico = ?, codigo_autogenerado = ?, estado = ?, nro_orden = ?, total_orden = ? " +
                     "WHERE id_cita = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            
            ps.setInt(1, cita.getPaciente().getIdPaciente());
            ps.setInt(2, cita.getMedico().getIdMedico());
            ps.setInt(3, cita.getConsultorio().getIdConsultorio());
            ps.setInt(4, cita.getTerminalista().getIdUsuario());
            
            if (cita.getTipoAtencion() != null) {
                ps.setInt(5, cita.getTipoAtencion().getIdTipoAtencion());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            
            ps.setTimestamp(6, Timestamp.valueOf(cita.getFechaHoraCita()));
            ps.setTimestamp(7, Timestamp.valueOf(cita.getFechaCreacion()));
            ps.setString(8, cita.getNroActoMedico());
            ps.setString(9, cita.getCodigoAutogenerado());
            ps.setString(10, cita.getEstado());
            ps.setInt(11, cita.getNroOrden());
            ps.setInt(12, cita.getTotalOrden());
            ps.setInt(13, cita.getIdCita()); // WHERE

            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar cita: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Citas WHERE id_cita = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar cita: " + e.getMessage());
            return false;
        } finally {
            this.conexionDB.desconectar(cnx);
        }
    }

    @Override
    public Cita buscarPorId(int id) {
        String sql = SQL_SELECT_CITAS + " WHERE c.id_cita = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return construirCitaDesdeResultSet(rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar cita: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return null;
    }

    @Override
    public List<Cita> listarTodos() {
        List<Cita> lista = new ArrayList<>();
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(SQL_SELECT_CITAS);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirCitaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar citas: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }

    // --- Métodos Específicos de ICitaRepositorio ---

    @Override
    public boolean verificarDisponibilidad(int idMedico, LocalDateTime fechaHora) {
        String sql = "SELECT COUNT(*) FROM Citas WHERE id_doctor = ? AND fecha_hora_cita = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, idMedico);
            ps.setTimestamp(2, Timestamp.valueOf(fechaHora));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // True si count es 0 (disponible)
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar disponibilidad: " + e.getMessage());
            return false; // Por seguridad, si hay error, decimos que NO está disponible
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return false;
    }

    @Override
    public List<Cita> buscarPorPaciente(int idPaciente) {
        List<Cita> lista = new ArrayList<>();
        String sql = SQL_SELECT_CITAS + " WHERE c.id_paciente = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirCitaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar citas por paciente: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }

    @Override
    public List<Cita> buscarPorFecha(LocalDateTime fecha) {
        List<Cita> lista = new ArrayList<>();
        // Buscamos por FECHA, ignorando la hora
        String sql = SQL_SELECT_CITAS + " WHERE DATE(c.fecha_hora_cita) = ?";
        try {
            this.cnx = this.conexionDB.conectar();
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(fecha.toLocalDate()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(construirCitaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar citas por fecha: " + e.getMessage());
        } finally {
            this.conexionDB.desconectar(cnx);
        }
        return lista;
    }
}

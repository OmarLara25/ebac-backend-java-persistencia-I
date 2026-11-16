package com.ebac.modulo59.model;

import com.ebac.modulo59.dto.Telefono;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelefonoModel implements OperacionesCRUD<Telefono> {

    public final Connection conn;

    public TelefonoModel(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Telefono guardar(Telefono telefono) throws SQLException {

        String sql = "INSERT INTO telefonos(idUsuario, numero, tipo) VALUES (?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, telefono.getIdUsuario());
        ps.setString(2, telefono.getNumero());
        ps.setString(3, telefono.getTipo());

        int elementosInsertados = ps.executeUpdate();

        if (elementosInsertados == 1) {
            return telefono;
        }

        throw new SQLException("Algo salió mal al ejecutar el INSERT");
    }

    @Override
    public Telefono actualizarPorId(Telefono telefono) throws SQLException {

        String sql = "UPDATE telefonos SET idUsuario = ?, numero = ?, tipo = ? WHERE idTelefono = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, telefono.getIdUsuario());
        ps.setString(2, telefono.getNumero());
        ps.setString(3, telefono.getTipo());
        ps.setInt(4, telefono.getIdTelefono());

        int elementosActualizados = ps.executeUpdate();

        if (elementosActualizados == 1) {
            return telefono;
        }

        throw new SQLException("Algo salió mal al ejecutar el UPDATE");
    }

    @Override
    public int eliminarPorId(int id) throws SQLException {

        String sql = "DELETE FROM telefonos WHERE idTelefono = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        return ps.executeUpdate();
    }

    @Override
    public Telefono obtenerPorId(int id) throws SQLException {

        String sql = "SELECT * FROM telefonos WHERE idTelefono = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        Telefono telefono = new Telefono();

        while (rs.next()) {
            telefono.setIdTelefono(rs.getInt("idTelefono"));
            telefono.setIdUsuario(rs.getInt("idUsuario"));
            telefono.setNumero(rs.getString("numero"));
            telefono.setTipo(rs.getString("tipo"));
        }

        return telefono;
    }
}
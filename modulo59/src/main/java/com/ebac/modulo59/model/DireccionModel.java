package com.ebac.modulo59.model;

import com.ebac.modulo59.dto.Direccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DireccionModel implements OperacionesCRUD<Direccion> {

    public final Connection conn;

    public DireccionModel(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Direccion guardar(Direccion direccion) throws SQLException {

        String sql = "INSERT INTO direcciones(idUsuario, calle, numero, estado) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, direccion.getIdUsuario());
        ps.setString(2, direccion.getCalle());
        ps.setInt(3, direccion.getNumero());
        ps.setString(4, direccion.getEstado());

        int elementosInsertados = ps.executeUpdate();

        if (elementosInsertados == 1) {
            return direccion;
        }

        throw new SQLException("Algo salió mal al ejecutar el INSERT");
    }

    @Override
    public Direccion actualizarPorId(Direccion direccion) throws SQLException {

        String sql = "UPDATE direcciones SET idUsuario = ?, calle = ?, numero = ?, estado = ? WHERE idDireccion = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, direccion.getIdUsuario());
        ps.setString(2, direccion.getCalle());
        ps.setInt(3, direccion.getNumero());
        ps.setString(4, direccion.getEstado());
        ps.setInt(5, direccion.getIdDireccion());

        int elementosActualizados = ps.executeUpdate();

        if (elementosActualizados == 1) {
            return direccion;
        }

        throw new SQLException("Algo salió mal al ejecutar el UPDATE");
    }

    @Override
    public int eliminarPorId(int id) throws SQLException {

        String sql = "DELETE FROM direcciones WHERE idDireccion = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        return ps.executeUpdate();
    }

    @Override
    public Direccion obtenerPorId(int id) throws SQLException {

        String sql = "SELECT * FROM direcciones WHERE idDireccion = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        Direccion direccion = new Direccion();

        while (rs.next()) {
            direccion.setIdDireccion(rs.getInt("idDireccion"));
            direccion.setIdUsuario(rs.getInt("idUsuario"));
            direccion.setCalle(rs.getString("calle"));
            direccion.setNumero(rs.getInt("numero"));
            direccion.setEstado(rs.getString("estado"));
        }

        return direccion;
    }
}
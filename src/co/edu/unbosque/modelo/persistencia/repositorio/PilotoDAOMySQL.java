package co.edu.unbosque.modelo.persistencia.repositorio;

import co.edu.unbosque.modelo.mysql.ConexionMySQL;
import co.edu.unbosque.modelo.persistencia.PilotoDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PilotoDAOMySQL implements InterfaceDAO<PilotoDTO> {

    @Override
    public List<PilotoDTO> getAll() {
        ArrayList<PilotoDTO> res = new ArrayList<>();
        String sql = "SELECT codigo, nombre, horas_vuelo, base_codigo FROM piloto";

        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                res.add(new PilotoDTO(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("horas_vuelo"),
                        rs.getString("base_codigo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public PilotoDTO findById(String id) {
        String sql = "SELECT codigo, nombre, horas_vuelo, base_codigo FROM piloto WHERE codigo = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PilotoDTO(
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getInt("horas_vuelo"),
                            rs.getString("base_codigo")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(PilotoDTO x) {
        String sql = "INSERT INTO piloto(codigo, nombre, horas_vuelo, base_codigo) VALUES(?, ?, ?, ?)";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, x.getCodigo());
            ps.setString(2, x.getNombre());
            ps.setInt(3, x.getHorasVuelo());
            ps.setString(4, x.getCodigoBase());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(PilotoDTO x) {
        String sql = "UPDATE piloto SET nombre = ?, horas_vuelo = ?, base_codigo = ? WHERE codigo = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, x.getNombre());
            ps.setInt(2, x.getHorasVuelo());
            ps.setString(3, x.getCodigoBase());
            ps.setString(4, x.getCodigo());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM piloto WHERE codigo = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
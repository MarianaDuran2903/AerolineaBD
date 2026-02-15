package co.edu.unbosque.modelo.persistencia.repositorio;

import co.edu.unbosque.modelo.mysql.ConexionMySQL;
import co.edu.unbosque.modelo.persistencia.AvionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvionDAOMySQL implements InterfaceDAO<AvionDTO>{

    @Override
    public List<AvionDTO> getAll() {
        ArrayList<AvionDTO> res = new ArrayList<>();
        String sql = "SELECT num_cola, tipo, base_codigo, horas_regreso FROM avion";

        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                res.add(new AvionDTO(
                        rs.getString("num_cola"),
                        rs.getString("tipo"),
                        rs.getString("base_codigo"),
                        rs.getInt("horas_regreso")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public AvionDTO findById(String id) {
        String sql = "SELECT num_cola, tipo, base_codigo, horas_regreso FROM avion WHERE num_cola = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AvionDTO(
                            rs.getString("num_cola"),
                            rs.getString("tipo"),
                            rs.getString("base_codigo"),
                            rs.getInt("horas_regreso")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(AvionDTO x) {
        String sql = "INSERT INTO avion(num_cola, tipo, base_codigo, horas_regreso) VALUES(?, ?, ?, ?)";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, x.getNumCola());
            ps.setString(2, x.getTipo());
            ps.setString(3, x.getCodigoBase());
            ps.setInt(4, x.getHorasRegreso());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(AvionDTO x) {
        String sql = "UPDATE avion SET tipo = ?, base_codigo = ?, horas_regreso = ? WHERE num_cola = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, x.getTipo());
            ps.setString(2, x.getCodigoBase());
            ps.setInt(3, x.getHorasRegreso());
            ps.setString(4, x.getNumCola());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM avion WHERE num_cola = ?";
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
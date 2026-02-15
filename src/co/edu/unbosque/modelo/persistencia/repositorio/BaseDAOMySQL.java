package co.edu.unbosque.modelo.persistencia.repositorio;

import co.edu.unbosque.modelo.mysql.ConexionMySQL;
import co.edu.unbosque.modelo.persistencia.BaseDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDAOMySQL implements InterfaceDAO<BaseDTO>  {

    @Override
    public List<BaseDTO> getAll() {
        ArrayList<BaseDTO> res = new ArrayList<>();
        String sql = "SELECT codigo, nombre FROM base";

        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                res.add(new BaseDTO(rs.getString("codigo"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public BaseDTO findById(String id) {
        String sql = "SELECT codigo, nombre FROM base WHERE codigo = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BaseDTO(rs.getString("codigo"), rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(BaseDTO x) {
        String sql = "INSERT INTO base(codigo, nombre) VALUES(?, ?)";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, x.getCodigo());
            ps.setString(2, x.getNombre());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(BaseDTO x) {
        String sql = "UPDATE base SET nombre = ? WHERE codigo = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, x.getNombre());
            ps.setString(2, x.getCodigo());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM base WHERE codigo = ?";
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
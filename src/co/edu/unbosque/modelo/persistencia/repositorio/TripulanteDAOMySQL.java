package co.edu.unbosque.modelo.persistencia.repositorio;

import co.edu.unbosque.modelo.mysql.ConexionMySQL;
import co.edu.unbosque.modelo.persistencia.TripulanteDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripulanteDAOMySQL implements InterfaceDAO<TripulanteDTO>  {

    @Override
    public List<TripulanteDTO> getAll() {
        ArrayList<TripulanteDTO> res = new ArrayList<>();
        String sql = "SELECT codigo, nombre, base_codigo FROM tripulante";

        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                String base = rs.getString("base_codigo");
                String tels = cargarTelefonosCsv(con, codigo);

                res.add(new TripulanteDTO(codigo, nombre, tels, base));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public TripulanteDTO findById(String id) {
        String sql = "SELECT codigo, nombre, base_codigo FROM tripulante WHERE codigo = ?";
        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String codigo = rs.getString("codigo");
                    String nombre = rs.getString("nombre");
                    String base = rs.getString("base_codigo");
                    String tels = cargarTelefonosCsv(con, codigo);
                    return new TripulanteDTO(codigo, nombre, tels, base);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(TripulanteDTO x) {
        String sqlTrip = "INSERT INTO tripulante(codigo, nombre, base_codigo) VALUES(?, ?, ?)";
        String sqlTel = "INSERT INTO tripulante_telefono(tripulante_codigo, telefono) VALUES(?, ?)";

        try (Connection con = ConexionMySQL.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlTrip)) {
                ps.setString(1, x.getCodigo());
                ps.setString(2, x.getNombre());
                ps.setString(3, x.getCodigoBase());
                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            List<String> telefonos = csvALista(x.getTelefonosCsv());
            try (PreparedStatement psTel = con.prepareStatement(sqlTel)) {
                for (String t : telefonos) {
                    psTel.setString(1, x.getCodigo());
                    psTel.setString(2, t);
                    psTel.addBatch();
                }
                psTel.executeBatch();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(TripulanteDTO x) {
        String sqlTrip = "UPDATE tripulante SET nombre = ?, base_codigo = ? WHERE codigo = ?";
        String sqlDelTels = "DELETE FROM tripulante_telefono WHERE tripulante_codigo = ?";
        String sqlInsTel = "INSERT INTO tripulante_telefono(tripulante_codigo, telefono) VALUES(?, ?)";

        try (Connection con = ConexionMySQL.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlTrip)) {
                ps.setString(1, x.getNombre());
                ps.setString(2, x.getCodigoBase());
                ps.setString(3, x.getCodigo());
                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            try (PreparedStatement psDel = con.prepareStatement(sqlDelTels)) {
                psDel.setString(1, x.getCodigo());
                psDel.executeUpdate();
            }

            List<String> telefonos = csvALista(x.getTelefonosCsv());
            try (PreparedStatement psIns = con.prepareStatement(sqlInsTel)) {
                for (String t : telefonos) {
                    psIns.setString(1, x.getCodigo());
                    psIns.setString(2, t);
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        String sqlDelTels = "DELETE FROM tripulante_telefono WHERE tripulante_codigo = ?";
        String sqlDelTrip = "DELETE FROM tripulante WHERE codigo = ?";

        try (Connection con = ConexionMySQL.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(sqlDelTels)) {
                ps1.setString(1, id);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = con.prepareStatement(sqlDelTrip)) {
                ps2.setString(1, id);
                int filas = ps2.executeUpdate();
                if (filas != 1) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String cargarTelefonosCsv(Connection con, String codigoTripulante) throws SQLException {
        String sql = "SELECT telefono FROM tripulante_telefono WHERE tripulante_codigo = ?";
        ArrayList<String> tels = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoTripulante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String t = rs.getString("telefono");
                    if (t != null && !t.trim().isEmpty()) tels.add(t.trim());
                }
            }
        }
        return String.join(",", tels);
    }

    private List<String> csvALista(String csv) {
        ArrayList<String> res = new ArrayList<>();
        if (csv == null) return res;
        String t = csv.trim();
        if (t.isEmpty()) return res;

        String[] parts = t.split(",");
        for (String p : parts) {
            if (p != null && !p.trim().isEmpty()) res.add(p.trim());
        }
        return res;
    }
}
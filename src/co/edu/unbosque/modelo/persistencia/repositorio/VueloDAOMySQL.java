package co.edu.unbosque.modelo.persistencia.repositorio;

import co.edu.unbosque.modelo.mysql.ConexionMySQL;
import co.edu.unbosque.modelo.persistencia.VueloDTO;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class VueloDAOMySQL implements InterfaceDAO<VueloDTO> {

    @Override
    public List<VueloDTO> getAll() {
        ArrayList<VueloDTO> res = new ArrayList<>();
        String sql = "SELECT id, numero_vuelo, origen, destino, fecha_hora, num_cola, piloto_codigo FROM vuelo";

        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String numero = rs.getString("numero_vuelo");
                String origen = rs.getString("origen");
                String destino = rs.getString("destino");
                String fechaIso = rs.getTimestamp("fecha_hora").toLocalDateTime().toString();
                String numCola = rs.getString("num_cola");
                String piloto = rs.getString("piloto_codigo");
                String tripCsv = cargarTripulantesCsv(con, id);

                res.add(new VueloDTO(numero, origen, destino, fechaIso, numCola, piloto, tripCsv));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public VueloDTO findById(String idNumeroVuelo) {
        String sql = "SELECT id, numero_vuelo, origen, destino, fecha_hora, num_cola, piloto_codigo " +
                "FROM vuelo WHERE numero_vuelo = ?";

        try (Connection con = ConexionMySQL.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idNumeroVuelo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String numero = rs.getString("numero_vuelo");
                    String origen = rs.getString("origen");
                    String destino = rs.getString("destino");
                    String fechaIso = rs.getTimestamp("fecha_hora").toLocalDateTime().toString();
                    String numCola = rs.getString("num_cola");
                    String piloto = rs.getString("piloto_codigo");
                    String tripCsv = cargarTripulantesCsv(con, id);

                    return new VueloDTO(numero, origen, destino, fechaIso, numCola, piloto, tripCsv);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean add(VueloDTO x) {
        String sqlInsVuelo = "INSERT INTO vuelo(numero_vuelo, origen, destino, fecha_hora, num_cola, piloto_codigo) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        String sqlInsVT = "INSERT INTO vuelo_tripulante(vuelo_id, tripulante_codigo) VALUES(?, ?)";

        try (Connection con = ConexionMySQL.getConnection()) {
            con.setAutoCommit(false);

            int vueloId;

            try (PreparedStatement ps = con.prepareStatement(sqlInsVuelo, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, x.getNumeroVuelo());
                ps.setString(2, x.getOrigen());
                ps.setString(3, x.getDestino());
                ps.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.parse(x.getFechaHoraIso())));
                ps.setString(5, x.getNumColaAvion());
                ps.setString(6, x.getCodigoPiloto());

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        con.rollback();
                        return false;
                    }
                    vueloId = keys.getInt(1);
                }
            }

            List<String> trip = csvALista(x.getCodigosTripulantesCsv());
            try (PreparedStatement psVT = con.prepareStatement(sqlInsVT)) {
                for (String t : trip) {
                    psVT.setInt(1, vueloId);
                    psVT.setString(2, t);
                    psVT.addBatch();
                }
                psVT.executeBatch();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(VueloDTO x) {

        String sqlGetId = "SELECT id FROM vuelo WHERE numero_vuelo = ?";
        String sqlUpd = "UPDATE vuelo SET origen=?, destino=?, fecha_hora=?, num_cola=?, piloto_codigo=? WHERE numero_vuelo=?";
        String sqlDelVT = "DELETE FROM vuelo_tripulante WHERE vuelo_id = ?";
        String sqlInsVT = "INSERT INTO vuelo_tripulante(vuelo_id, tripulante_codigo) VALUES(?, ?)";

        try (Connection con = ConexionMySQL.getConnection()) {
            con.setAutoCommit(false);

            Integer vueloId = null;

            try (PreparedStatement ps = con.prepareStatement(sqlGetId)) {
                ps.setString(1, x.getNumeroVuelo());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) vueloId = rs.getInt("id");
                }
            }

            if (vueloId == null) {
                con.rollback();
                return false;
            }

            try (PreparedStatement ps = con.prepareStatement(sqlUpd)) {
                ps.setString(1, x.getOrigen());
                ps.setString(2, x.getDestino());
                ps.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.parse(x.getFechaHoraIso())));
                ps.setString(4, x.getNumColaAvion());
                ps.setString(5, x.getCodigoPiloto());
                ps.setString(6, x.getNumeroVuelo());

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            try (PreparedStatement psDel = con.prepareStatement(sqlDelVT)) {
                psDel.setInt(1, vueloId);
                psDel.executeUpdate();
            }

            List<String> trip = csvALista(x.getCodigosTripulantesCsv());
            try (PreparedStatement psIns = con.prepareStatement(sqlInsVT)) {
                for (String t : trip) {
                    psIns.setInt(1, vueloId);
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
    public boolean deleteById(String numeroVuelo) {
        String sqlGetId = "SELECT id FROM vuelo WHERE numero_vuelo = ?";
        String sqlDelVT = "DELETE FROM vuelo_tripulante WHERE vuelo_id = ?";
        String sqlDelVuelo = "DELETE FROM vuelo WHERE id = ?";

        try (Connection con = ConexionMySQL.getConnection()) {
            con.setAutoCommit(false);

            Integer vueloId = null;

            try (PreparedStatement ps = con.prepareStatement(sqlGetId)) {
                ps.setString(1, numeroVuelo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) vueloId = rs.getInt("id");
                }
            }

            if (vueloId == null) {
                con.rollback();
                return false;
            }

            try (PreparedStatement ps1 = con.prepareStatement(sqlDelVT)) {
                ps1.setInt(1, vueloId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = con.prepareStatement(sqlDelVuelo)) {
                ps2.setInt(1, vueloId);
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

    private String cargarTripulantesCsv(Connection con, int vueloId) throws SQLException {
        String sql = "SELECT tripulante_codigo FROM vuelo_tripulante WHERE vuelo_id = ?";
        ArrayList<String> cods = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vueloId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String c = rs.getString("tripulante_codigo");
                    if (c != null && !c.trim().isEmpty()) cods.add(c.trim());
                }
            }
        }
        return String.join(",", cods);
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
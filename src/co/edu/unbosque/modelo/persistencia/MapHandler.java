package co.edu.unbosque.modelo.persistencia;

import co.edu.unbosque.modelo.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapHandler {

    public BaseDTO baseAModeloDTO(Base b) {
        if (b == null) return null;
        return new BaseDTO(b.getCodigo(), b.getNombre());
    }

    public Base baseDTOAModelo(BaseDTO dto) {
        if (dto == null) return null;
        return new Base(dto.getCodigo(), dto.getNombre());
    }

    public AvionDTO avionAModeloDTO(Avion a) {
        if (a == null) return null;
        String codigoBase = (a.getBase() == null) ? null : a.getBase().getCodigo();
        return new AvionDTO(a.getNumCola(), a.getTipo(), codigoBase, a.getHorasRegreso());
    }

    public Avion avionDTOAModelo(AvionDTO dto) {
        if (dto == null) return null;
        Base base = (dto.getCodigoBase() == null || dto.getCodigoBase().trim().isEmpty())
                ? null
                : new Base(dto.getCodigoBase().trim(), null);
        return new Avion(dto.getNumCola(), dto.getTipo(), base, dto.getHorasRegreso());
    }

    public PilotoDTO pilotoAModeloDTO(Piloto p) {
        if (p == null) return null;
        String codigoBase = (p.getBase() == null) ? null : p.getBase().getCodigo();
        return new PilotoDTO(p.getCodigo(), p.getNombre(), p.getHorasVuelo(), codigoBase);
    }

    public Piloto pilotoDTOAModelo(PilotoDTO dto) {
        if (dto == null) return null;
        Base base = (dto.getCodigoBase() == null || dto.getCodigoBase().trim().isEmpty())
                ? null
                : new Base(dto.getCodigoBase().trim(), null);
        return new Piloto(dto.getCodigo(), dto.getNombre(), dto.getHorasVuelo(), base);
    }

    public TripulanteDTO tripulanteAModeloDTO(Tripulante t) {
        if (t == null) return null;

        String codigoBase = (t.getBase() == null) ? null : t.getBase().getCodigo();
        String telefonosCsv = listaACsv(t.getTelefonos());

        return new TripulanteDTO(t.getCodigo(), t.getNombre(), telefonosCsv, codigoBase);
    }

    public Tripulante tripulanteDTOAModelo(TripulanteDTO dto) {
        if (dto == null) return null;

        Base base = (dto.getCodigoBase() == null || dto.getCodigoBase().trim().isEmpty())
                ? null
                : new Base(dto.getCodigoBase().trim(), null);

        List<String> telefonos = csvALista(dto.getTelefonosCsv());

        return new Tripulante(dto.getCodigo(), dto.getNombre(), telefonos, base);
    }

    public VueloDTO vueloAModeloDTO(Vuelo v) {
        if (v == null) return null;

        String fechaHoraIso = (v.getFechaHora() == null) ? "" : v.getFechaHora().toString();
        String numCola = (v.getAvion() == null) ? null : v.getAvion().getNumCola();
        String codigoPiloto = (v.getPiloto() == null) ? null : v.getPiloto().getCodigo();

        String tripCsv = "";
        if (v.getTripulacion() != null && !v.getTripulacion().isEmpty()) {
            ArrayList<String> codigos = new ArrayList<>();
            for (Tripulante t : v.getTripulacion()) {
                if (t != null && t.getCodigo() != null) codigos.add(t.getCodigo());
            }
            tripCsv = String.join(",", codigos);
        }

        return new VueloDTO(
                v.getNumeroVuelo(),
                v.getOrigen(),
                v.getDestino(),
                fechaHoraIso,
                numCola,
                codigoPiloto,
                tripCsv
        );
    }

    public Vuelo vueloDTOAModelo(VueloDTO dto) {
        if (dto == null) return null;

        LocalDateTime fh = null;
        if (dto.getFechaHoraIso() != null && !dto.getFechaHoraIso().trim().isEmpty()) {
            fh = LocalDateTime.parse(dto.getFechaHoraIso().trim());
        }

        Avion avion = (dto.getNumColaAvion() == null || dto.getNumColaAvion().trim().isEmpty())
                ? null
                : new Avion(dto.getNumColaAvion().trim(), null, null, 0);

        Piloto piloto = (dto.getCodigoPiloto() == null || dto.getCodigoPiloto().trim().isEmpty())
                ? null
                : new Piloto(dto.getCodigoPiloto().trim(), null, 0, null);

        List<Tripulante> tripulacion = new ArrayList<>();
        List<String> codigos = csvALista(dto.getCodigosTripulantesCsv());
        for (String c : codigos) {
            if (!c.trim().isEmpty()) tripulacion.add(new Tripulante(c.trim(), null, null, null));
        }

        return new Vuelo(
                dto.getNumeroVuelo(),
                dto.getOrigen(),
                dto.getDestino(),
                fh,
                avion,
                piloto,
                tripulacion
        );
    }

    private String listaACsv(List<String> lista) {
        if (lista == null || lista.isEmpty()) return "";
        ArrayList<String> limpia = new ArrayList<>();
        for (String s : lista) {
            if (s != null && !s.trim().isEmpty()) limpia.add(s.trim());
        }
        return String.join(",", limpia);
    }

    private List<String> csvALista(String csv) {
        ArrayList<String> res = new ArrayList<>();
        if (csv == null) return res;
        String t = csv.trim();
        if (t.isEmpty()) return res;
        res.addAll(Arrays.asList(t.split(",")));

        for (int i = 0; i < res.size(); i++) res.set(i, res.get(i).trim());
        res.removeIf(x -> x == null || x.trim().isEmpty());
        return res;
    }
}
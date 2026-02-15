package co.edu.unbosque.modelo;

import co.edu.unbosque.modelo.excepciones.AerolineaException;
import co.edu.unbosque.modelo.excepciones.NoEncontradoException;
import co.edu.unbosque.modelo.excepciones.OperacionNoPermitidaException;
import co.edu.unbosque.modelo.persistencia.*;
import co.edu.unbosque.modelo.persistencia.repositorio.*;

import java.util.ArrayList;
import java.util.List;

public class Aerolinea {

    private final BaseDAOMySQL baseDAO;
    private final AvionDAOMySQL avionDAO;
    private final PilotoDAOMySQL pilotoDAO;
    private final TripulanteDAOMySQL tripulanteDAO;
    private final VueloDAOMySQL vueloDAO;
    private final MapHandler mapper;

    public Aerolinea() {
        this.baseDAO = new BaseDAOMySQL();
        this.avionDAO = new AvionDAOMySQL();
        this.pilotoDAO = new PilotoDAOMySQL();
        this.tripulanteDAO = new TripulanteDAOMySQL();
        this.vueloDAO = new VueloDAOMySQL();
        this.mapper = new MapHandler();
    }

    private void validarCodigo(String codigo, String campo) throws AerolineaException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new OperacionNoPermitidaException("El " + campo + " no puede estar vacío.");
        }
    }

    private void validarTexto(String texto, String campo) throws AerolineaException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new OperacionNoPermitidaException("El " + campo + " no puede estar vacío.");
        }
    }

    private void validarNoNegativo(int n, String campo) throws AerolineaException {
        if (n < 0) {
            throw new OperacionNoPermitidaException("El " + campo + " no puede ser negativo.");
        }
    }

    private void validarBaseExiste(String codigoBase) throws AerolineaException {
        validarCodigo(codigoBase, "código de base");
        if (baseDAO.findById(codigoBase) == null) {
            throw new NoEncontradoException("No existe la base con código " + codigoBase);
        }
    }

    private void validarTelefonoMinimo(List<String> telefonos) throws AerolineaException {
        if (telefonos == null || telefonos.isEmpty()) {
            throw new OperacionNoPermitidaException("Debe registrar al menos un teléfono.");
        }
        boolean hayUnoValido = false;
        for (String t : telefonos) {
            if (t != null && !t.trim().isEmpty()) {
                hayUnoValido = true;
                break;
            }
        }
        if (!hayUnoValido) {
            throw new OperacionNoPermitidaException("Debe registrar al menos un teléfono válido.");
        }
    }

    private void validarOrigenDestino(String origen, String destino) throws AerolineaException {
        validarCodigo(origen, "origen");
        validarCodigo(destino, "destino");
        String o = origen.trim().toUpperCase();
        String d = destino.trim().toUpperCase();
        if (o.equals(d)) {
            throw new OperacionNoPermitidaException("El origen y el destino no pueden ser iguales.");
        }
    }

    private void validarFechaHoraIso(String fechaHoraIso) throws AerolineaException {
        validarTexto(fechaHoraIso, "fecha y hora (ISO)");
        try {
            java.time.LocalDateTime.parse(fechaHoraIso.trim());
        } catch (Exception e) {
            throw new OperacionNoPermitidaException("FechaHora inválida. Usa formato ISO: yyyy-MM-ddTHH:mm (ej: 2026-02-15T14:30)");
        }
    }

    private void validarExisteAvion(String numCola) throws AerolineaException {
        validarCodigo(numCola, "número de cola del avión");
        if (avionDAO.findById(numCola.trim().toUpperCase()) == null) {
            throw new NoEncontradoException("No existe avión con número de cola " + numCola);
        }
    }

    private void validarExistePiloto(String codigoPiloto) throws AerolineaException {
        validarCodigo(codigoPiloto, "código del piloto");
        if (pilotoDAO.findById(codigoPiloto.trim().toUpperCase()) == null) {
            throw new NoEncontradoException("No existe piloto con código " + codigoPiloto);
        }
    }

    private void validarTripulantesExisten(List<String> codigosTripulantes) throws AerolineaException {
        if (codigosTripulantes == null || codigosTripulantes.isEmpty()) {
            throw new OperacionNoPermitidaException("Debe registrar al menos 1 tripulante en el vuelo.");
        }
        for (String c : codigosTripulantes) {
            if (c == null || c.trim().isEmpty()) continue;
            String cod = c.trim().toUpperCase();
            if (tripulanteDAO.findById(cod) == null) {
                throw new NoEncontradoException("No existe tripulante con código " + cod);
            }
        }
    }

    private String listaACsvCodigos(List<String> codigos) {
        if (codigos == null || codigos.isEmpty()) return "";
        ArrayList<String> limpia = new ArrayList<>();
        for (String c : codigos) {
            if (c != null && !c.trim().isEmpty()) limpia.add(c.trim().toUpperCase());
        }
        return String.join(",", limpia);
    }

    // BASE
    public void registrarBase(String codigo, String nombre) throws AerolineaException {
        validarCodigo(codigo, "código de base");
        validarTexto(nombre, "nombre de base");

        String cod = codigo.trim().toUpperCase();

        if (baseDAO.findById(cod) != null) {
            throw new OperacionNoPermitidaException("Ya existe una base con código " + cod);
        }

        Base modelo = new Base(cod, nombre.trim());
        if (!baseDAO.add(mapper.baseAModeloDTO(modelo))) {
            throw new OperacionNoPermitidaException("No fue posible registrar la base.");
        }
    }

    public void actualizarBase(String codigo, String nuevoNombre) throws AerolineaException {
        validarCodigo(codigo, "código de base");
        validarTexto(nuevoNombre, "nombre de base");

        String cod = codigo.trim().toUpperCase();

        BaseDTO existente = baseDAO.findById(cod);
        if (existente == null) {
            throw new NoEncontradoException("No existe base con código " + cod);
        }

        Base actualizado = new Base(cod, nuevoNombre.trim());
        if (!baseDAO.update(mapper.baseAModeloDTO(actualizado))) {
            throw new OperacionNoPermitidaException("No fue posible actualizar la base.");
        }
    }

    public void eliminarBase(String codigo) throws AerolineaException {
        validarCodigo(codigo, "código de base");

        String cod = codigo.trim().toUpperCase();

        if (baseDAO.findById(cod) == null) {
            throw new NoEncontradoException("No existe base con código " + cod);
        }

        for (AvionDTO a : avionDAO.getAll()) {
            if (a.getCodigoBase() != null && a.getCodigoBase().trim().equalsIgnoreCase(cod)) {
                throw new OperacionNoPermitidaException("No se puede eliminar: hay aviones asociados a la base " + cod);
            }
        }
        for (PilotoDTO p : pilotoDAO.getAll()) {
            if (p.getCodigoBase() != null && p.getCodigoBase().trim().equalsIgnoreCase(cod)) {
                throw new OperacionNoPermitidaException("No se puede eliminar: hay pilotos asociados a la base " + cod);
            }
        }
        for (TripulanteDTO t : tripulanteDAO.getAll()) {
            if (t.getCodigoBase() != null && t.getCodigoBase().trim().equalsIgnoreCase(cod)) {
                throw new OperacionNoPermitidaException("No se puede eliminar: hay tripulantes asociados a la base " + cod);
            }
        }

        if (!baseDAO.deleteById(cod)) {
            throw new OperacionNoPermitidaException("No fue posible eliminar la base.");
        }
    }

    public Base buscarBasePorCodigo(String codigo) throws AerolineaException {
        validarCodigo(codigo, "código de base");
        String cod = codigo.trim().toUpperCase();

        BaseDTO dto = baseDAO.findById(cod);
        if (dto == null) throw new NoEncontradoException("No existe base con código " + cod);
        return mapper.baseDTOAModelo(dto);
    }

    public List<Base> listarBases() {
        ArrayList<Base> res = new ArrayList<>();
        for (BaseDTO dto : baseDAO.getAll()) {
            res.add(mapper.baseDTOAModelo(dto));
        }
        return res;
    }

    // AVION
    public void registrarAvion(String numCola, String tipo, String codigoBase, int horasRegreso) throws AerolineaException {
        validarCodigo(numCola, "número de cola");
        validarTexto(tipo, "tipo de avión");
        validarBaseExiste(codigoBase);
        validarNoNegativo(horasRegreso, "horas de regreso");

        String cola = numCola.trim().toUpperCase();
        String base = codigoBase.trim().toUpperCase();

        if (avionDAO.findById(cola) != null) {
            throw new OperacionNoPermitidaException("Ya existe un avión con número de cola " + cola);
        }

        Avion modelo = new Avion(cola, tipo.trim(), new Base(base, null), horasRegreso);

        if (!avionDAO.add(mapper.avionAModeloDTO(modelo))) {
            throw new OperacionNoPermitidaException("No fue posible registrar el avión.");
        }
    }

    public void actualizarAvion(String numCola, String nuevoTipo, String nuevaBase, int nuevasHorasRegreso) throws AerolineaException {
        validarCodigo(numCola, "número de cola");
        validarTexto(nuevoTipo, "tipo de avión");
        validarBaseExiste(nuevaBase);
        validarNoNegativo(nuevasHorasRegreso, "horas de regreso");

        String cola = numCola.trim().toUpperCase();
        String base = nuevaBase.trim().toUpperCase();

        AvionDTO existente = avionDAO.findById(cola);
        if (existente == null) {
            throw new NoEncontradoException("No existe avión con número de cola " + cola);
        }

        Avion actualizado = new Avion(cola, nuevoTipo.trim(), new Base(base, null), nuevasHorasRegreso);

        if (!avionDAO.update(mapper.avionAModeloDTO(actualizado))) {
            throw new OperacionNoPermitidaException("No fue posible actualizar el avión.");
        }
    }

    public void eliminarAvion(String numCola) throws AerolineaException {
        validarCodigo(numCola, "número de cola");
        String cola = numCola.trim().toUpperCase();

        if (avionDAO.findById(cola) == null) {
            throw new NoEncontradoException("No existe avión con número de cola " + cola);
        }

        for (VueloDTO v : vueloDAO.getAll()) {
            if (v.getNumColaAvion() != null && v.getNumColaAvion().trim().equalsIgnoreCase(cola)) {
                throw new OperacionNoPermitidaException("No se puede eliminar: el avión está asignado al vuelo " + v.getNumeroVuelo());
            }
        }

        if (!avionDAO.deleteById(cola)) {
            throw new OperacionNoPermitidaException("No fue posible eliminar el avión.");
        }
    }

    public Avion buscarAvionPorNumCola(String numCola) throws AerolineaException {
        validarCodigo(numCola, "número de cola");
        String cola = numCola.trim().toUpperCase();

        AvionDTO dto = avionDAO.findById(cola);
        if (dto == null) throw new NoEncontradoException("No existe avión con número de cola " + cola);
        return mapper.avionDTOAModelo(dto);
    }

    public List<Avion> listarAviones() {
        ArrayList<Avion> res = new ArrayList<>();
        for (AvionDTO dto : avionDAO.getAll()) {
            res.add(mapper.avionDTOAModelo(dto));
        }
        return res;
    }

// PILOTO
public void registrarPiloto(String codigo, String nombre, int horasVuelo, String codigoBase) throws AerolineaException {
    validarCodigo(codigo, "código de piloto");
    validarTexto(nombre, "nombre");
    validarNoNegativo(horasVuelo, "horas de vuelo");
    validarBaseExiste(codigoBase);

    String cod = codigo.trim().toUpperCase();
    String base = codigoBase.trim().toUpperCase();

    if (pilotoDAO.findById(cod) != null) {
        throw new OperacionNoPermitidaException("Ya existe un piloto con código " + cod);
    }

    Piloto modelo = new Piloto(cod, nombre.trim(), horasVuelo, new Base(base, null));
    if (!pilotoDAO.add(mapper.pilotoAModeloDTO(modelo))) {
        throw new OperacionNoPermitidaException("No fue posible registrar el piloto.");
    }
}

    public void actualizarPiloto(String codigo, String nuevoNombre, int nuevasHorasVuelo, String nuevaBase) throws AerolineaException {
        validarCodigo(codigo, "código de piloto");
        validarTexto(nuevoNombre, "nombre");
        validarNoNegativo(nuevasHorasVuelo, "horas de vuelo");
        validarBaseExiste(nuevaBase);

        String cod = codigo.trim().toUpperCase();
        String base = nuevaBase.trim().toUpperCase();

        PilotoDTO existente = pilotoDAO.findById(cod);
        if (existente == null) {
            throw new NoEncontradoException("No existe piloto con código " + cod);
        }

        Piloto actualizado = new Piloto(cod, nuevoNombre.trim(), nuevasHorasVuelo, new Base(base, null));
        if (!pilotoDAO.update(mapper.pilotoAModeloDTO(actualizado))) {
            throw new OperacionNoPermitidaException("No fue posible actualizar el piloto.");
        }
    }

    public void eliminarPiloto(String codigo) throws AerolineaException {
        validarCodigo(codigo, "código de piloto");
        String cod = codigo.trim().toUpperCase();

        if (pilotoDAO.findById(cod) == null) {
            throw new NoEncontradoException("No existe piloto con código " + cod);
        }

        for (VueloDTO v : vueloDAO.getAll()) {
            if (v.getCodigoPiloto() != null && v.getCodigoPiloto().trim().equalsIgnoreCase(cod)) {
                throw new OperacionNoPermitidaException("No se puede eliminar: el piloto está asignado al vuelo " + v.getNumeroVuelo());
            }
        }

        if (!pilotoDAO.deleteById(cod)) {
            throw new OperacionNoPermitidaException("No fue posible eliminar el piloto.");
        }
    }

    public Piloto buscarPilotoPorCodigo(String codigo) throws AerolineaException {
        validarCodigo(codigo, "código de piloto");
        String cod = codigo.trim().toUpperCase();

        PilotoDTO dto = pilotoDAO.findById(cod);
        if (dto == null) throw new NoEncontradoException("No existe piloto con código " + cod);
        return mapper.pilotoDTOAModelo(dto);
    }

    public List<Piloto> listarPilotos() {
        ArrayList<Piloto> res = new ArrayList<>();
        for (PilotoDTO dto : pilotoDAO.getAll()) {
            res.add(mapper.pilotoDTOAModelo(dto));
        }
        return res;
    }

    // TRIPULANTE
    public void registrarTripulante(String codigo, String nombre, List<String> telefonos, String codigoBase) throws AerolineaException {
        validarCodigo(codigo, "código de tripulante");
        validarTexto(nombre, "nombre");
        validarTelefonoMinimo(telefonos);
        validarBaseExiste(codigoBase);

        String cod = codigo.trim().toUpperCase();
        String base = codigoBase.trim().toUpperCase();

        if (tripulanteDAO.findById(cod) != null) {
            throw new OperacionNoPermitidaException("Ya existe un tripulante con código " + cod);
        }

        Tripulante modelo = new Tripulante(cod, nombre.trim(), telefonos, new Base(base, null));
        if (!tripulanteDAO.add(mapper.tripulanteAModeloDTO(modelo))) {
            throw new OperacionNoPermitidaException("No fue posible registrar el tripulante.");
        }
    }

    public void actualizarTripulante(String codigo, String nuevoNombre, List<String> nuevosTelefonos, String nuevaBase) throws AerolineaException {
        validarCodigo(codigo, "código de tripulante");
        validarTexto(nuevoNombre, "nombre");
        validarTelefonoMinimo(nuevosTelefonos);
        validarBaseExiste(nuevaBase);

        String cod = codigo.trim().toUpperCase();
        String base = nuevaBase.trim().toUpperCase();

        TripulanteDTO existente = tripulanteDAO.findById(cod);
        if (existente == null) {
            throw new NoEncontradoException("No existe tripulante con código " + cod);
        }

        Tripulante actualizado = new Tripulante(cod, nuevoNombre.trim(), nuevosTelefonos, new Base(base, null));
        if (!tripulanteDAO.update(mapper.tripulanteAModeloDTO(actualizado))) {
            throw new OperacionNoPermitidaException("No fue posible actualizar el tripulante.");
        }
    }

    public void eliminarTripulante(String codigo) throws AerolineaException {
        validarCodigo(codigo, "código de tripulante");
        String cod = codigo.trim().toUpperCase();

        if (tripulanteDAO.findById(cod) == null) {
            throw new NoEncontradoException("No existe tripulante con código " + cod);
        }

        for (VueloDTO v : vueloDAO.getAll()) {
            String csv = (v.getCodigosTripulantesCsv() == null) ? "" : v.getCodigosTripulantesCsv();
            String[] parts = csv.split(",");
            for (String p : parts) {
                if (p != null && p.trim().equalsIgnoreCase(cod)) {
                    throw new OperacionNoPermitidaException("No se puede eliminar: el tripulante está asignado al vuelo " + v.getNumeroVuelo());
                }
            }
        }

        if (!tripulanteDAO.deleteById(cod)) {
            throw new OperacionNoPermitidaException("No fue posible eliminar el tripulante.");
        }
    }

    public Tripulante buscarTripulantePorCodigo(String codigo) throws AerolineaException {
        validarCodigo(codigo, "código de tripulante");
        String cod = codigo.trim().toUpperCase();

        TripulanteDTO dto = tripulanteDAO.findById(cod);
        if (dto == null) throw new NoEncontradoException("No existe tripulante con código " + cod);
        return mapper.tripulanteDTOAModelo(dto);
    }

    public List<Tripulante> listarTripulantes() {
        ArrayList<Tripulante> res = new ArrayList<>();
        for (TripulanteDTO dto : tripulanteDAO.getAll()) {
            res.add(mapper.tripulanteDTOAModelo(dto));
        }
        return res;
    }

    // VUELO

    public void registrarVuelo(String numeroVuelo,
                               String origen,
                               String destino,
                               String fechaHoraIso,
                               String numColaAvion,
                               String codigoPiloto,
                               List<String> codigosTripulantes) throws AerolineaException {

        validarCodigo(numeroVuelo, "número de vuelo");
        validarOrigenDestino(origen, destino);
        validarFechaHoraIso(fechaHoraIso);

        validarExisteAvion(numColaAvion);
        validarExistePiloto(codigoPiloto);
        validarTripulantesExisten(codigosTripulantes);

        String num = numeroVuelo.trim().toUpperCase();
        String o = origen.trim().toUpperCase();
        String d = destino.trim().toUpperCase();
        String cola = numColaAvion.trim().toUpperCase();
        String pil = codigoPiloto.trim().toUpperCase();
        String tripCsv = listaACsvCodigos(codigosTripulantes);

        if (vueloDAO.findById(num) != null) {
            throw new OperacionNoPermitidaException("Ya existe un vuelo con número " + num +
                    ". (En esta versión el número de vuelo es único)");
        }

        VueloDTO dto = new VueloDTO(num, o, d, fechaHoraIso.trim(), cola, pil, tripCsv);

        if (!vueloDAO.add(dto)) {
            throw new OperacionNoPermitidaException("No fue posible registrar el vuelo.");
        }
    }

    public void actualizarVuelo(String numeroVuelo,
                                String nuevoOrigen,
                                String nuevoDestino,
                                String nuevaFechaHoraIso,
                                String nuevoNumColaAvion,
                                String nuevoCodigoPiloto,
                                List<String> nuevosCodigosTripulantes) throws AerolineaException {

        validarCodigo(numeroVuelo, "número de vuelo");
        validarOrigenDestino(nuevoOrigen, nuevoDestino);
        validarFechaHoraIso(nuevaFechaHoraIso);

        validarExisteAvion(nuevoNumColaAvion);
        validarExistePiloto(nuevoCodigoPiloto);
        validarTripulantesExisten(nuevosCodigosTripulantes);

        String num = numeroVuelo.trim().toUpperCase();

        VueloDTO existente = vueloDAO.findById(num);
        if (existente == null) {
            throw new NoEncontradoException("No existe vuelo con número " + num);
        }

        String o = nuevoOrigen.trim().toUpperCase();
        String d = nuevoDestino.trim().toUpperCase();
        String cola = nuevoNumColaAvion.trim().toUpperCase();
        String pil = nuevoCodigoPiloto.trim().toUpperCase();
        String tripCsv = listaACsvCodigos(nuevosCodigosTripulantes);

        VueloDTO actualizado = new VueloDTO(num, o, d, nuevaFechaHoraIso.trim(), cola, pil, tripCsv);

        if (!vueloDAO.update(actualizado)) {
            throw new OperacionNoPermitidaException("No fue posible actualizar el vuelo.");
        }
    }

    public void eliminarVuelo(String numeroVuelo) throws AerolineaException {
        validarCodigo(numeroVuelo, "número de vuelo");
        String num = numeroVuelo.trim().toUpperCase();

        if (vueloDAO.findById(num) == null) {
            throw new NoEncontradoException("No existe vuelo con número " + num);
        }

        if (!vueloDAO.deleteById(num)) {
            throw new OperacionNoPermitidaException("No fue posible eliminar el vuelo.");
        }
    }

    public Vuelo buscarVueloPorNumero(String numeroVuelo) throws AerolineaException {
        validarCodigo(numeroVuelo, "número de vuelo");
        String num = numeroVuelo.trim().toUpperCase();

        VueloDTO dto = vueloDAO.findById(num);
        if (dto == null) throw new NoEncontradoException("No existe vuelo con número " + num);

        return mapper.vueloDTOAModelo(dto);
    }

    public List<Vuelo> listarVuelos() {
        ArrayList<Vuelo> res = new ArrayList<>();
        for (VueloDTO dto : vueloDAO.getAll()) {
            res.add(mapper.vueloDTOAModelo(dto));
        }
        return res;
    }
}
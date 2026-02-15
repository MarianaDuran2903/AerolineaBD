package co.edu.unbosque.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vuelo {
    private String numeroVuelo;
    private String origen;
    private String destino;
    private LocalDateTime fechaHora;
    private Avion avion;
    private Piloto piloto;
    private List<Tripulante> tripulacion;

    public Vuelo() {
        this.tripulacion = new ArrayList<>();
    }

    public Vuelo(String numeroVuelo, String origen, String destino, LocalDateTime fechaHora,
                 Avion avion, Piloto piloto, List<Tripulante> tripulacion) {
        this.numeroVuelo = numeroVuelo;
        this.origen = origen;
        this.destino = destino;
        this.fechaHora = fechaHora;
        this.avion = avion;
        this.piloto = piloto;
        this.tripulacion = (tripulacion != null) ? tripulacion : new ArrayList<>();
    }

    public String getNumeroVuelo() { return numeroVuelo; }
    public void setNumeroVuelo(String numeroVuelo) { this.numeroVuelo = numeroVuelo; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Avion getAvion() { return avion; }
    public void setAvion(Avion avion) { this.avion = avion; }

    public Piloto getPiloto() { return piloto; }
    public void setPiloto(Piloto piloto) { this.piloto = piloto; }

    public List<Tripulante> getTripulacion() { return tripulacion; }
    public void setTripulacion(List<Tripulante> tripulacion) {
        this.tripulacion = (tripulacion != null) ? tripulacion : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vuelo)) return false;
        Vuelo vuelo = (Vuelo) o;
        return Objects.equals(numeroVuelo, vuelo.numeroVuelo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroVuelo);
    }

    @Override
    public String toString() {
        return "Vuelo{" +
                "numeroVuelo='" + numeroVuelo + '\'' +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", fechaHora=" + fechaHora +
                ", avion=" + (avion != null ? avion.getNumCola() : null) +
                ", piloto=" + (piloto != null ? piloto.getCodigo() : null) +
                ", tripulacion=" + (tripulacion != null ? tripulacion.size() : 0) +
                '}';
    }
}

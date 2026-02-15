package co.edu.unbosque.modelo.persistencia;

public class VueloDTO {
    private String numeroVuelo;
    private String origen;
    private String destino;
    private String fechaHoraIso;
    private String numColaAvion;
    private String codigoPiloto;
    private String codigosTripulantesCsv;

    public VueloDTO() {}

    public VueloDTO(String numeroVuelo, String origen, String destino, String fechaHoraIso,
                    String numColaAvion, String codigoPiloto, String codigosTripulantesCsv) {
        this.numeroVuelo = numeroVuelo;
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraIso = fechaHoraIso;
        this.numColaAvion = numColaAvion;
        this.codigoPiloto = codigoPiloto;
        this.codigosTripulantesCsv = codigosTripulantesCsv;
    }

    public String getNumeroVuelo() { return numeroVuelo; }
    public void setNumeroVuelo(String numeroVuelo) { this.numeroVuelo = numeroVuelo; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getFechaHoraIso() { return fechaHoraIso; }
    public void setFechaHoraIso(String fechaHoraIso) { this.fechaHoraIso = fechaHoraIso; }

    public String getNumColaAvion() { return numColaAvion; }
    public void setNumColaAvion(String numColaAvion) { this.numColaAvion = numColaAvion; }

    public String getCodigoPiloto() { return codigoPiloto; }
    public void setCodigoPiloto(String codigoPiloto) { this.codigoPiloto = codigoPiloto; }

    public String getCodigosTripulantesCsv() { return codigosTripulantesCsv; }
    public void setCodigosTripulantesCsv(String codigosTripulantesCsv) { this.codigosTripulantesCsv = codigosTripulantesCsv; }
}

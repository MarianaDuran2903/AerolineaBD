package co.edu.unbosque.modelo.persistencia;

public class TripulanteDTO {
    private String codigo;
    private String nombre;
    private String telefonosCsv;
    private String codigoBase;

    public TripulanteDTO() {}

    public TripulanteDTO(String codigo, String nombre, String telefonosCsv, String codigoBase) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefonosCsv = telefonosCsv;
        this.codigoBase = codigoBase;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefonosCsv() { return telefonosCsv; }
    public void setTelefonosCsv(String telefonosCsv) { this.telefonosCsv = telefonosCsv; }

    public String getCodigoBase() { return codigoBase; }
    public void setCodigoBase(String codigoBase) { this.codigoBase = codigoBase; }
}

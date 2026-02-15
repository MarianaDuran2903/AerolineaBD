package co.edu.unbosque.modelo.persistencia;

public class PilotoDTO {
    private String codigo;
    private String nombre;
    private int horasVuelo;
    private String codigoBase;

    public PilotoDTO() {}

    public PilotoDTO(String codigo, String nombre, int horasVuelo, String codigoBase) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.horasVuelo = horasVuelo;
        this.codigoBase = codigoBase;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getHorasVuelo() { return horasVuelo; }
    public void setHorasVuelo(int horasVuelo) { this.horasVuelo = horasVuelo; }

    public String getCodigoBase() { return codigoBase; }
    public void setCodigoBase(String codigoBase) { this.codigoBase = codigoBase; }
}

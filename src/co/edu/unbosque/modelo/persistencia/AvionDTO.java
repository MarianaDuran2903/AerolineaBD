package co.edu.unbosque.modelo.persistencia;

public class AvionDTO {
    private String numCola;
    private String tipo;
    private String codigoBase;
    private int horasRegreso;

    public AvionDTO() {}

    public AvionDTO(String numCola, String tipo, String codigoBase, int horasRegreso) {
        this.numCola = numCola;
        this.tipo = tipo;
        this.codigoBase = codigoBase;
        this.horasRegreso = horasRegreso;
    }

    public String getNumCola() { return numCola; }
    public void setNumCola(String numCola) { this.numCola = numCola; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getCodigoBase() { return codigoBase; }
    public void setCodigoBase(String codigoBase) { this.codigoBase = codigoBase; }

    public int getHorasRegreso() { return horasRegreso; }
    public void setHorasRegreso(int horasRegreso) { this.horasRegreso = horasRegreso; }
}

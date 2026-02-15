package co.edu.unbosque.modelo;

import java.util.Objects;

public class Avion {
    private String numCola;
    private String tipo;
    private Base base;
    private int horasRegreso;

    public Avion() {}

    public Avion(String numCola, String tipo, Base base, int horasRegreso) {
        this.numCola = numCola;
        this.tipo = tipo;
        this.base = base;
        this.horasRegreso = horasRegreso;
    }

    public String getNumCola() { return numCola; }
    public void setNumCola(String numCola) { this.numCola = numCola; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }

    public int getHorasRegreso() { return horasRegreso; }
    public void setHorasRegreso(int horasRegreso) { this.horasRegreso = horasRegreso; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avion)) return false;
        Avion avion = (Avion) o;
        return Objects.equals(numCola, avion.numCola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCola);
    }

    @Override
    public String toString() {
        return "Avion{" +
                "numCola='" + numCola + '\'' +
                ", tipo='" + tipo + '\'' +
                ", base=" + (base != null ? base.getCodigo() : null) +
                ", horasRegreso=" + horasRegreso +
                '}';
    }
}
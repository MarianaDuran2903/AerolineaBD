package co.edu.unbosque.modelo;

import java.util.Objects;

public class Piloto {
    private String codigo;
    private String nombre;
    private int horasVuelo;
    private Base base;

    public Piloto() {}

    public Piloto(String codigo, String nombre, int horasVuelo, Base base) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.horasVuelo = horasVuelo;
        this.base = base;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getHorasVuelo() { return horasVuelo; }
    public void setHorasVuelo(int horasVuelo) { this.horasVuelo = horasVuelo; }

    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piloto)) return false;
        Piloto piloto = (Piloto) o;
        return Objects.equals(codigo, piloto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Piloto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", horasVuelo=" + horasVuelo +
                ", base=" + (base != null ? base.getCodigo() : null) +
                '}';
    }
}
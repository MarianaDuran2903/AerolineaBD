package co.edu.unbosque.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tripulante {
    private String codigo;
    private String nombre;
    private List<String> telefonos;
    private Base base;

    public Tripulante() {
        this.telefonos = new ArrayList<>();
    }

    public Tripulante(String codigo, String nombre, List<String> telefonos, Base base) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefonos = (telefonos != null) ? telefonos : new ArrayList<>();
        this.base = base;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<String> getTelefonos() { return telefonos; }
    public void setTelefonos(List<String> telefonos) {
        this.telefonos = (telefonos != null) ? telefonos : new ArrayList<>();
    }

    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tripulante)) return false;
        Tripulante that = (Tripulante) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Tripulante{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefonos=" + telefonos +
                ", base=" + (base != null ? base.getCodigo() : null) +
                '}';
    }
}

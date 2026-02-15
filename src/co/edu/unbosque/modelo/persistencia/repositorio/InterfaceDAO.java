package co.edu.unbosque.modelo.persistencia.repositorio;

import java.util.List;

public interface InterfaceDAO<T> {
    List<T> getAll();
    T findById(String id);
    boolean add(T x);
    boolean update(T x);
    boolean deleteById(String id);
}
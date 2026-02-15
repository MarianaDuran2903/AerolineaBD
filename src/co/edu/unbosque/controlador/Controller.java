package co.edu.unbosque.controlador;

import co.edu.unbosque.modelo.Aerolinea;
import co.edu.unbosque.modelo.excepciones.AerolineaException;
import co.edu.unbosque.vista.VistaConsola;

import java.util.List;

public class Controller {

    private final VistaConsola vista;
    private final Aerolinea aerolinea;
    private boolean running;

    public Controller() {
        vista = new VistaConsola();
        aerolinea = new Aerolinea();
        running = true;
    }

    public void run() {
        while (running) {
            mostrarMenuPrincipal();
        }
    }

    private void mostrarMenuPrincipal() {
        vista.mostrar("\n====== SISTEMA AEROLÍNEA ======");
        vista.mostrar("1. Bases");
        vista.mostrar("2. Aviones");
        vista.mostrar("3. Pilotos");
        vista.mostrar("4. Tripulantes");
        vista.mostrar("5. Vuelos");
        vista.mostrar("0. Salir");

        int op = vista.leerEntero("Seleccione opción: ");

        switch (op) {
            case 1 -> menuBases();
            case 2 -> menuAviones();
            case 3 -> menuPilotos();
            case 4 -> menuTripulantes();
            case 5 -> menuVuelos();
            case 0 -> running = false;
            default -> vista.mostrar("Opción inválida.");
        }
    }

    // BASE

    private void menuBases() {
        vista.mostrar("\n--- BASES ---");
        vista.mostrar("1. Registrar");
        vista.mostrar("2. Listar");
        vista.mostrar("0. Volver");

        int op = vista.leerEntero("Opción: ");

        try {
            switch (op) {
                case 1 -> {
                    String cod = vista.leerTexto("Código: ");
                    String nombre = vista.leerTexto("Nombre: ");
                    aerolinea.registrarBase(cod, nombre);
                    vista.mostrar("Base registrada.");
                }
                case 2 -> {
                    aerolinea.listarBases().forEach(b -> vista.mostrar(b.toString()));
                }
            }
        } catch (AerolineaException e) {
            vista.mostrar("Error: " + e.getMessage());
        }

        vista.pausa();
    }

    // AVIONES

    private void menuAviones() {
        vista.mostrar("\n--- AVIONES ---");
        vista.mostrar("1. Registrar");
        vista.mostrar("2. Listar");
        vista.mostrar("0. Volver");

        int op = vista.leerEntero("Opción: ");

        try {
            switch (op) {
                case 1 -> {
                    String cola = vista.leerTexto("Número de cola: ");
                    String tipo = vista.leerTexto("Tipo: ");
                    String base = vista.leerTexto("Base: ");
                    int horas = vista.leerEntero("Horas regreso: ");
                    aerolinea.registrarAvion(cola, tipo, base, horas);
                    vista.mostrar("Avión registrado.");
                }
                case 2 -> aerolinea.listarAviones()
                        .forEach(a -> vista.mostrar(a.toString()));
            }
        } catch (AerolineaException e) {
            vista.mostrar("Error: " + e.getMessage());
        }

        vista.pausa();
    }

    // PILOTOS

    private void menuPilotos() {
        vista.mostrar("\n--- PILOTOS ---");
        vista.mostrar("1. Registrar");
        vista.mostrar("2. Listar");
        vista.mostrar("0. Volver");

        int op = vista.leerEntero("Opción: ");

        try {
            switch (op) {
                case 1 -> {
                    String cod = vista.leerTexto("Código: ");
                    String nombre = vista.leerTexto("Nombre: ");
                    int horas = vista.leerEntero("Horas vuelo: ");
                    String base = vista.leerTexto("Base: ");
                    aerolinea.registrarPiloto(cod, nombre, horas, base);
                    vista.mostrar("Piloto registrado.");
                }
                case 2 -> aerolinea.listarPilotos()
                        .forEach(p -> vista.mostrar(p.toString()));
            }
        } catch (AerolineaException e) {
            vista.mostrar("Error: " + e.getMessage());
        }

        vista.pausa();
    }

    // TRIPULANTES

    private void menuTripulantes() {
        vista.mostrar("\n--- TRIPULANTES ---");
        vista.mostrar("1. Registrar");
        vista.mostrar("2. Listar");
        vista.mostrar("0. Volver");

        int op = vista.leerEntero("Opción: ");

        try {
            switch (op) {
                case 1 -> {
                    String cod = vista.leerTexto("Código: ");
                    String nombre = vista.leerTexto("Nombre: ");
                    List<String> telefonos = vista.leerLista("Teléfonos");
                    String base = vista.leerTexto("Base: ");
                    aerolinea.registrarTripulante(cod, nombre, telefonos, base);
                    vista.mostrar("Tripulante registrado.");
                }
                case 2 -> aerolinea.listarTripulantes()
                        .forEach(t -> vista.mostrar(t.toString()));
            }
        } catch (AerolineaException e) {
            vista.mostrar("Error: " + e.getMessage());
        }

        vista.pausa();
    }

    // VUELOS

    private void menuVuelos() {
        vista.mostrar("\n--- VUELOS ---");
        vista.mostrar("1. Registrar");
        vista.mostrar("2. Listar");
        vista.mostrar("0. Volver");

        int op = vista.leerEntero("Opción: ");

        try {
            switch (op) {
                case 1 -> {
                    String num = vista.leerTexto("Número vuelo: ");
                    String origen = vista.leerTexto("Origen: ");
                    String destino = vista.leerTexto("Destino: ");
                    String fecha = vista.leerTexto("FechaHora (ISO): ");
                    String avion = vista.leerTexto("Avión: ");
                    String piloto = vista.leerTexto("Piloto: ");
                    List<String> trip = vista.leerLista("Tripulantes");
                    aerolinea.registrarVuelo(num, origen, destino, fecha, avion, piloto, trip);
                    vista.mostrar("Vuelo registrado.");
                }
                case 2 -> aerolinea.listarVuelos()
                        .forEach(v -> vista.mostrar(v.toString()));
            }
        } catch (AerolineaException e) {
            vista.mostrar("Error: " + e.getMessage());
        }

        vista.pausa();
    }
}

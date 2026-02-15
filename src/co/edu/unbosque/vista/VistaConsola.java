package co.edu.unbosque.vista;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VistaConsola {

    private final Scanner sc;

    public VistaConsola() {
        sc = new Scanner(System.in);
    }

    public void mostrar(String mensaje) {
        System.out.println(mensaje);
    }

    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Debes ingresar un número entero.");
            }
        }
    }

    public boolean leerBoolean(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("S")) return true;
            if (input.equals("N")) return false;
            System.out.println("Responde S o N.");
        }
    }

    public List<String> leerLista(String mensaje) {
        System.out.print(mensaje + " (separados por coma): ");
        String input = sc.nextLine().trim();

        ArrayList<String> lista = new ArrayList<>();
        if (!input.isEmpty()) {
            String[] partes = input.split(",");
            for (String p : partes) {
                if (!p.trim().isEmpty()) {
                    lista.add(p.trim().toUpperCase());
                }
            }
        }
        return lista;
    }

    public void pausa() {
        System.out.println("\nPresiona ENTER para continuar...");
        sc.nextLine();
    }
}

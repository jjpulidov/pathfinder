package es.unex.cum.iiisa.io;

import es.unex.cum.iiisa.pathfinder.PathFinder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Estos nombres creo que hay que cogerlos por teclado directamente
        List<String> nombres1 = Arrays.asList("C1", "C2", "C3", "C4", "C5", "C6", "C7");
        List<String> nombres2 = Arrays.asList("N1", "N2", "N3", "N4");
        List<String> nombres3 = Arrays.asList("N1", "N2", "N3", "N4", "N5", "N6");

        // Aquí se procesa el fichero de entrada
        Entrada e = new Entrada(new File("ejemplo1.txt"));
        try {
            e.procesarFichero();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // Llamada al algoritmo con los valores r y q que entran por teclado, al igual que los nombres
        PathFinder pfnet = new PathFinder(e, 2, 6, nombres1);

        // Este método directamente genera los dos archivos de salida en la raíz del proyecto. Son los que habría que descargar
        pfnet.execute1("./");
    }
}

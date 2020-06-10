package es.unex.cum.iiisa.io;

import es.unex.cum.iiisa.pathfinder.PathFinder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Clase principal para probar el modelo sin necesidad de desplegar la aplicación web.
 */
public class Main {
    /**
     * Programa principal. Instancia los nombres de los nodos que deberían ser obtenidos de la web,
     * obtiene un fichero de entrada y r y q se modifican manualmente (deberían entrar desde la web también).
     * Después, se lanza el algoritmo y devuelve los ficheros a la raíz del proyecto.
     *
     * @param args
     */
    public static void main(String[] args) {
        // Nombres de los nodos
        List<String> nombres1 = Arrays.asList("C1", "C2", "C3", "C4", "C5", "C6", "C7");
        List<String> nombres2 = Arrays.asList("N1", "N2", "N3", "N4");
        List<String> nombres3 = Arrays.asList("N1", "N2", "N3", "N4", "N5", "N6");

        // Procesamiento del fichero de entrada
        Entrada e = new Entrada(new File("ejemplo1.txt"));
        try {
            e.procesarFichero();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        // Instanciación del algoritmo
        PathFinder pfnet = new PathFinder(e, 2, 6, nombres1);

        // Se lanza el algoritmo 1 (para lanzar el 2, execute2())
        pfnet.execute1("./");
    }
}

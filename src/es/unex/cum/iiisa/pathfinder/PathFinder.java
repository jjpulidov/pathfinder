package es.unex.cum.iiisa.pathfinder;

import es.unex.cum.iiisa.io.Entrada;
import es.unex.cum.iiisa.io.Salida;

import java.util.*;

/**
 * Clase que define los algoritmos pathfinder 3.4 (execute1()) y 3.5 (execute2()).
 */
public class PathFinder {
    /**
     * Parámetros r, q y número de nodos.
     */
    private final int q, r, n;
    /**
     * Lista con los nombres de los nodos.
     */
    private final List<String> nombres;
    /**
     * Matriz de pesos.
     */
    private final double[][] matrizPesos;
    /**
     * Identificador.
     */
    private final String id;
    /**
     * Objecto que contiene los atributos de entrada al algoritmo.
     */
    private final Entrada entrada;
    /**
     * Red pathfinder.
     */
    private double[][] pfnet;

    /**
     * Constructor parametrizado.
     *
     * @param entrada Atributos de entrada al algoritmo.
     * @param r       Parámetro r. Si r = 0 se considera infinito.
     * @param q       Parámetro q. Tiene que ser menor que n.
     * @param nombres Nombres de los nodos de la red.
     */
    public PathFinder(Entrada entrada, int r, int q, List<String> nombres) {
        this.entrada = entrada;
        this.r = r;
        this.q = q;
        this.matrizPesos = entrada.getMatriz();
        this.nombres = nombres;
        this.n = this.matrizPesos.length;
        this.id = entrada.getIdentificador();
    }

    /**
     * Algoritmo del punto 3.4.
     *
     * @param pathSalida Path de salida de los ficheros.
     * @return Salida del algoritmo.
     */
    public Salida execute1(String pathSalida) {
        // Obtención de la fecha de inicio del algoritmo
        Date fechaInicio = new Date();

        // Definición de la matriz de distancias
        double[][] matrizDistancias = new double[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizDistancias = inicializarMatriz(matrizDistancias);

        // Copia de los valores de la matriz de pesos en la matriz de distancias
        matrizDistancias = copiarMatriz(matrizPesos, matrizDistancias);

        // Definición de la matriz de orden mayor
        double[][] matrizOrdenSuperior = new double[n][n];

        // Inicialización de la matriz de orden mayor a 0
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        // Copia de los valores de la matriz de pesos en la matriz de orden mayor
        matrizOrdenSuperior = copiarMatriz(matrizPesos, matrizOrdenSuperior);

        // Bucle para obtener las matrices de orden q y comparar con la matriz de distancias
        for (int i = 1; i < q; i++) {
            matrizOrdenSuperior = generarMatrizOrdenSuperior(matrizOrdenSuperior);
            matrizDistancias = generarMatrizDistanciasMinimas(matrizDistancias, matrizOrdenSuperior);
        }

        // Se compara con la matriz de pesos obteniendo la PFNET
        compararConMatrizPesos(matrizDistancias);

        // Obtención de la fecha de finalización del algoritmo
        Date fechaFin = new Date();

        // Procesamiento de la salida del algoritmo para después generar los ficheros
        Salida salida = new Salida(entrada, pfnet, fechaInicio, fechaFin, id, pathSalida);
        salida.run();
        return salida;
    }

    /**
     * Inicialización de la matriz de entrada a 0 en todas las celdas.
     *
     * @param matrix Matriz.
     * @return Matriz inicializada a 0.
     */
    public double[][] inicializarMatriz(double[][] matrix) {
        for (double[] fila : matrix) {
            Arrays.fill(fila, 0);
        }

        return matrix;
    }

    /**
     * Copia de la matriz src a la matriz dst.
     *
     * @param src Matriz origen.
     * @param dst Matriz destino.
     * @return Matriz dst con los valores de src.
     */
    public double[][] copiarMatriz(double[][] src, double[][] dst) {
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                if (src[i][j] != 0)
                    dst[i][j] = src[i][j];
            }
        }

        return dst;
    }

    /**
     * Generación de la matriz de orden superior.
     *
     * @param matrizOrdenInferior Matriz de orden inferior.
     * @return Martiz de orden superior.
     */
    public double[][] generarMatrizOrdenSuperior(double[][] matrizOrdenInferior) {
        double[][] matrizOrdenSuperior = new double[n][n];
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        // Si r == 0 se considera infinito
        if (r == 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double pesoMin = Double.POSITIVE_INFINITY;
                    for (int k = 0; k < n; k++) {
                        double temp = Math.max(matrizOrdenInferior[k][j], matrizPesos[i][k]);
                        if (temp == 0)
                            continue;
                        if (pesoMin > temp)
                            pesoMin = temp;
                    }
                    if (pesoMin != Double.POSITIVE_INFINITY)
                        matrizOrdenSuperior[i][j] = pesoMin;
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double pesoMin = Double.POSITIVE_INFINITY;
                    for (int k = 0; k < n; k++) {
                        double temp = Math.pow(Math.pow(matrizPesos[i][k], r) + Math.pow(matrizOrdenInferior[k][j], r), 1.0 / r);
                        if (temp == 0)
                            continue;
                        if (pesoMin > temp)
                            pesoMin = temp;
                    }
                    if (pesoMin != Double.POSITIVE_INFINITY)
                        matrizOrdenSuperior[i][j] = pesoMin;
                }
            }
        }

        // Se devuelve la matriz de orden superior
        return matrizOrdenSuperior;
    }

    /**
     * Genración de la matriz de distancias mínimas.
     *
     * @param matrizDistancias    Matriz de distancias.
     * @param matrizOrdenSuperior Matriz de orden superior.
     * @return Matriz de distancias mínimas.
     */
    public double[][] generarMatrizDistanciasMinimas(double[][] matrizDistancias, double[][] matrizOrdenSuperior) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;
                if (matrizDistancias[i][j] > 0 && matrizOrdenSuperior[i][j] > 0)
                    matrizDistancias[i][j] = Math.min(matrizDistancias[i][j], matrizOrdenSuperior[i][j]);
            }
        }

        return matrizDistancias;
    }

    /**
     * Obtención de la PFNET tras comparar la matriz distancias con la matriz de pesos.
     *
     * @param matrizDistancias Matriz de distancias mínimas.
     */
    public void compararConMatrizPesos(double[][] matrizDistancias) {
        pfnet = new double[n][n];
        inicializarMatriz(pfnet);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrizPesos[i][j] == matrizDistancias[i][j])
                    pfnet[i][j] = matrizPesos[i][j];
            }
        }
    }

    /**
     * Algoritmo PFNET del punto 3.5.
     *
     * @param pathSalida Path de los ficheros de salida.
     */
    public Salida execute2(String pathSalida) {
        // Obtención de la fecha de inicio del algoritmo
        Date fechaInicio = new Date();

        // Obtención de las clases de los enlaces
        // Se utiliza un mapa con los valores de la matriz como clave y como valor una lista de enlaces
        Map<Double, List<Enlace>> clasesNoOrdenadas = new HashMap<>();
        for (int i = 0; i < matrizPesos.length; i++) {
            for (int j = 0; j < matrizPesos[i].length; j++) {
                if (j > i) {
                    if (!clasesNoOrdenadas.containsKey(matrizPesos[i][j])) {
                        clasesNoOrdenadas.put(matrizPesos[i][j], Collections.singletonList(new Enlace(i, j)));
                    } else {
                        List<Enlace> temp = new ArrayList<>(clasesNoOrdenadas.get(matrizPesos[i][j]));
                        temp.add(new Enlace(i, j));
                        clasesNoOrdenadas.put(matrizPesos[i][j], temp);
                    }
                }
            }
        }

        // Ordenación de las clases mediante un TreeMap
        Map<Double, List<Enlace>> clases = new TreeMap<>(clasesNoOrdenadas);
        // Se elimina la clase del valor 0
        clases.remove(clases.keySet().toArray()[0]);
        // Número de clases
        int numClases = clases.keySet().size();

        // Tabla de incidencia. Se utilizan listas para que sea dinámica
        List<int[]> incidencia = new ArrayList<>();

        // Lista con los enlaces para la clase com menor valor (la que se está procesando)
        List<Enlace> enlaces = clases.get(clases.keySet().toArray()[0]);

        // Se elimina la clase que se está procesando
        clases.remove(clases.keySet().toArray()[0]);
        numClases--;

        // Se genera una fila (lista) para cada enlace de la clase procesada con valor 0 en todas
        // sus posiciones menos en aquellas que pertenecen a los nodos del enlace
        for (Enlace enlace : enlaces) {
            int[] fila = new int[n];
            Arrays.fill(fila, 0);

            fila[enlace.getI()] = 1;
            fila[enlace.getJ()] = 1;

            incidencia.add(fila);
        }

        // Comprobación de la etiqueta del enlace
        for (Enlace enlace : enlaces) {
            int sum_i = 0;
            int sum_j = 0;
            for (int[] fila : incidencia) {
                sum_i += fila[enlace.getI()];
                sum_j += fila[enlace.getJ()];
            }

            if (sum_i == 1 || sum_j == 1) // Si la columna i o j suman 1 -> Etiqueta PRI
                enlace.setEtiqueta("PRI");
            else if (sum_i > 1 || sum_j > 1) // Si no, pero suman más de 1 -> Etiqueta SEC
                enlace.setEtiqueta("SEC");
            else    // Si no, no hay enlace
                enlace.setEtiqueta("no edge");
        }

        // Lista con las sublistas de enlaces que tienen conexión
        List<List<Integer>> nsl = new ArrayList<>();
        // Nodos restantes no tratados
        List<Integer> nodosRestantes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodosRestantes.add(i);
        }

        // Bucle para obtener las sublistas de nodos conectados
        while (!nodosRestantes.isEmpty()) {
            int nodoActual = enlaces.get(0).getI();
            List<Integer> subgrafo = new ArrayList<>();
            subgrafo.add(nodoActual);
            nodosRestantes.remove(nodoActual);

            List<Integer> temp = new ArrayList<>(nodosRestantes);

            for (int nodo : temp) {
                for (Enlace enlace : enlaces) {
                    if ((enlace.getI() == nodoActual && subgrafo.contains(enlace.getJ())) || (subgrafo.contains(enlace.getI()) && enlace.getJ() == nodoActual)) {
                        subgrafo.add(nodo);
                        nodosRestantes.remove(nodo);
                        break;
                    }
                }
            }
            nodosRestantes = temp;
            nsl.add(subgrafo);
        }

        // Si solo hay un subconjunto de nodos no se continúa
        if (nsl.size() != 1) {
            // Obtención de las siguiente clases y mismo procedimiento que el anterior
            for (int i = 1; i < numClases; i++) {
                enlaces = clases.get(clases.keySet().toArray()[i]);

                // Se genera una fila (lista) para cada enlace de la clase procesada con valor 0 en todas
                // sus posiciones menos en aquellas que pertenecen a los nodos del enlace
                for (Enlace enlace : enlaces) {
                    int[] fila = new int[n];
                    Arrays.fill(fila, 0);

                    fila[enlace.getI()] = 1;
                    fila[enlace.getJ()] = 1;

                    incidencia.add(fila);
                }

                // Comprobación de la etiqueta del enlace
                for (Enlace enlace : enlaces) {
                    int sum_i = 0;
                    int sum_j = 0;
                    for (int[] fila : incidencia) {
                        sum_i += fila[enlace.getI()];
                        sum_j += fila[enlace.getJ()];
                    }

                    if (sum_i == 1 || sum_j == 1) // Si la columna i o j suman 1 -> Etiqueta PRI
                        enlace.setEtiqueta("PRI");
                    else if (sum_i > 1 || sum_j > 1) // Si no, pero suman más de 1 -> Etiqueta SEC
                        enlace.setEtiqueta("SEC");
                    else    // Si no, no hay enlace
                        enlace.setEtiqueta("no edge");
                }

                nsl = new ArrayList<>();
                nodosRestantes = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    nodosRestantes.add(j);
                }

                // Bucle para obtener las sublistas de nodos conectados
                while (!nodosRestantes.isEmpty()) {
                    List<Integer> subgrafo = new ArrayList<>();
                    subgrafo.add(enlaces.get(0).getI());
                    subgrafo.add(enlaces.get(0).getJ());
                    nodosRestantes.remove(enlaces.get(0).getI());
                    nodosRestantes.remove(enlaces.get(0).getJ());

                    for (int nodo : nodosRestantes) {
                        for (Enlace enlace : enlaces) {
                            if ((enlace.getI() == nodo && subgrafo.contains(enlace.getJ())) || (subgrafo.contains(enlace.getI()) && enlace.getJ() == nodo)) {
                                subgrafo.add(nodo);
                                nodosRestantes.remove(nodo);
                                break;
                            }
                        }
                    }
                    nsl.add(subgrafo);
                }
            }
        }

        // Se etiquetan los enlaces no procesados como TER
        for (double peso : clases.keySet()) {
            for (Enlace enlace : clases.get(peso)) {
                enlace.setEtiqueta("TER");
                int[] temp = new int[n];
                Arrays.fill(temp, 0);

                temp[enlace.getI()] = 1;
                temp[enlace.getJ()] = 1;

                incidencia.add(temp);
            }
        }

        // Si no hay enlace para la red actual, se etiqueta como TER
        for (Enlace enlace : enlaces) {
            if (enlace.getEtiqueta().equals("no edge"))
                enlace.setEtiqueta("TER");
        }

        // Definición de la matriz de distancias
        double[][] matrizDistancias = new double[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizDistancias = inicializarMatriz(matrizDistancias);

        // Copia de los valores de la matriz de pesos en la matriz de distancias
        matrizDistancias = copiarMatriz(matrizPesos, matrizDistancias);

        // Definición de la matriz de orden mayor
        double[][] matrizOrdenSuperior = new double[n][n];

        // Inicialización de la matriz de orden mayor a 0
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        // Copia de los valores de la matriz de pesos en la matriz de orden mayor
        matrizOrdenSuperior = copiarMatriz(matrizPesos, matrizOrdenSuperior);

        // Bucle para obtener las matrices de orden q y comparar con la matriz de distancias
        for (int i = 1; i < q; i++) {
            matrizOrdenSuperior = generarMatrizOrdenSuperior(matrizOrdenSuperior);
            matrizDistancias = generarMatrizDistanciasMinimas(matrizDistancias, matrizOrdenSuperior);
        }

        // Se compara con la matriz de pesos obteniendo la PFNET
        compararConMatrizPesos(matrizDistancias);

        // Obtención de la fecha de finalización del algoritmo
        Date fechaFin = new Date();

        // Procesamiento de la salida del algoritmo para después generar los ficheros
        Salida salida = new Salida(entrada, pfnet, fechaInicio, fechaFin, id, pathSalida);
        salida.run();
        return salida;
    }

    /**
     * Clase que define un enlace entre dos nodos (i, j).
     */
    public static class Enlace {
        /**
         * Nodo origen.
         */
        protected int i;
        /**
         * Nodo destino.
         */
        protected int j;
        /**
         * Etiqueta ["PRI", "SEC", "TER", "no edge"]
         */
        protected String etiqueta;

        /**
         * Constructor parametrizado.
         *
         * @param i Nodo origen.
         * @param j Nodo destino.
         */
        public Enlace(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public void setEtiqueta(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        @Override
        public String toString() {
            return "Enlace{" + "i=" + i + ", j=" + j + ", etiqueta='" + etiqueta + '\'' + '}';
        }
    }
}

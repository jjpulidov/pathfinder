package es.unex.cum.iiisa.pathfinder;

import es.unex.cum.iiisa.io.Entrada;
import es.unex.cum.iiisa.io.Salida;

import java.util.*;

public class PathFinder {
    private final int q, r, n;
    private final List<String> nombres;
    private final double[][] matrizPesos;
    private final String id;
    private double[][] pfnet;
    private Entrada entrada;

    public PathFinder(Entrada entrada, int r, int q, List<String> nombres) {
        this.entrada = entrada;
        this.r = r;
        this.q = q;
        this.matrizPesos = entrada.getMatriz();
        this.nombres = nombres;
        this.n = this.matrizPesos.length;
        this.id = entrada.getIdentificador();
    }

    public void execute1(String pathSalida) {
        Date fechaInicio = new Date();

        // Definición de la matriz de distancias
        double[][] matrizDistancias = new double[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizDistancias = inicializarMatriz(matrizDistancias);

        // Copia de los valores de la matriz de pesos en la matriz de distancias
        matrizDistancias = copiarMatriz(matrizPesos, matrizDistancias);

        // Definición de la matriz de mayor orden
        double[][] matrizOrdenSuperior = new double[n][n];

        // Inicialización de la matriz de mayor orden a 0
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        // Copia de los valores de la matriz de pesos en la matriz de mayor orden
        matrizOrdenSuperior = copiarMatriz(matrizPesos, matrizOrdenSuperior);

        for (int i = 1; i < q; i++) {
            matrizOrdenSuperior = generarMatrizOrdenSuperior(matrizOrdenSuperior);
            matrizDistancias = generarMatrizDistanciasMinimas(matrizDistancias, matrizOrdenSuperior);
        }

        compararConMatrizPesos(matrizDistancias);

        Date fechaFin = new Date();

        Salida salida = new Salida(entrada, pfnet, fechaInicio, fechaFin, id, pathSalida);
        salida.run();
    }

    public double[][] inicializarMatriz(double[][] matrix) {
        for (double[] fila : matrix) {
            Arrays.fill(fila, 0);
        }

        return matrix;
    }

    public double[][] copiarMatriz(double[][] src, double[][] dst) {
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                double valor = src[i][j];
                if (valor != 0)
                    dst[i][j] = valor;
            }
        }

        return dst;
    }

    public double[][] generarMatrizOrdenSuperior(double[][] matrizOrdenInferior) {
        double[][] matrizOrdenSuperior = new double[n][n];
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        if (r == 0) {   // r == infinito
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

        return matrizOrdenSuperior;
    }

    public double[][] generarMatrizDistanciasMinimas(double[][] matrizDistancias, double[][] matrizOrdenSuperior) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    continue;
                double valueD = matrizDistancias[i][j];
                double valueH = matrizOrdenSuperior[i][j];
                if (valueD > 0 && valueH > 0)
                    matrizDistancias[i][j] = Math.min(valueD, valueH);
            }
        }

        return matrizDistancias;
    }

    public void compararConMatrizPesos(double[][] matrizDistancias) {
        pfnet = new double[n][n];
        inicializarMatriz(pfnet);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double valueW = matrizPesos[i][j];
                double valueD = matrizDistancias[i][j];
                if (valueW == valueD)
                    pfnet[i][j] = valueW;
            }
        }
    }

    public void execute2(String pathSalida) {
        Date fechaInicio = new Date();

        Map<Double, List<Enlace>> clasesNoOrdenadas = new HashMap<>();
        for (int i = 0; i < matrizPesos.length; i++) {
            for (int j = 0; j < matrizPesos[i].length; j++) {
                if (!clasesNoOrdenadas.containsKey(matrizPesos[i][j])) {
                    clasesNoOrdenadas.put(matrizPesos[i][j], Collections.singletonList(new Enlace(i, j)));
                } else {
                    List<Enlace> temp = new ArrayList<>(clasesNoOrdenadas.get(matrizPesos[i][j]));
                    temp.add(new Enlace(i, j));
                    clasesNoOrdenadas.put(matrizPesos[i][j], temp);
                }
            }
        }


        Map<Double, List<Enlace>> clases = new TreeMap<>(clasesNoOrdenadas);
        int numClases = clases.keySet().size();

        // Clase 0
        clases.remove(clases.keySet().toArray()[0]);

        List<int[]> incidencia = new ArrayList<>();
        List<Enlace> etiquetas = new ArrayList<>();
        List<Enlace> enlaces = clases.get(clases.keySet().toArray()[0]);
        etiquetas.addAll(enlaces);
        clases.remove(clases.keySet().toArray()[0]);

        for (Enlace enlace : enlaces) {
            int[] fila = new int[n];
            Arrays.fill(fila, 0);

            fila[enlace.getI()] = 1;
            fila[enlace.getJ()] = 1;

            incidencia.add(fila);
        }

        for (Enlace enlace : enlaces) {
            int sum_i = 0;
            int sum_j = 0;
            for (int[] fila : incidencia) {
                sum_i += fila[enlace.getI()];
                sum_j += fila[enlace.getJ()];
            }

            if (sum_i == 1 || sum_j == 1)
                enlace.setEtiqueta("PRI");
            else if (sum_i > 1 || sum_j > 1)
                enlace.setEtiqueta("SEC");
            else
                enlace.setEtiqueta("no edge");
        }

        List<List<Integer>> nsl = new ArrayList<>();
        List<Integer> nodosRestantes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodosRestantes.add(i);
        }

        int nodoActual = enlaces.get(0).getI();
        while (!nodosRestantes.isEmpty()) {
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

        if (nsl.size() == 1) {
            for (int i = 1; i < numClases - 1; i++) {
                enlaces = clases.get(clases.keySet().toArray()[i]);
                etiquetas.addAll(enlaces);
                clases.remove(clases.keySet().toArray()[0]);

                for (Enlace enlace : enlaces) {
                    int[] fila = new int[n];
                    Arrays.fill(fila, 0);

                    fila[enlace.getI()] = 1;
                    fila[enlace.getJ()] = 1;

                    incidencia.add(fila);
                }

                for (Enlace enlace : enlaces) {
                    int sum_i = 0;
                    int sum_j = 0;
                    for (int[] fila : incidencia) {
                        sum_i += fila[enlace.getI()];
                        sum_j += fila[enlace.getJ()];
                    }

                    if (sum_i == 1 || sum_j == 1)
                        enlace.setEtiqueta("PRI");
                    else if (sum_i > 1 || sum_j > 1)
                        enlace.setEtiqueta("SEC");
                    else
                        enlace.setEtiqueta("no edge");
                }

                nsl = new ArrayList<>();
                nodosRestantes = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    nodosRestantes.add(j);
                }

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

        for (double peso : clases.keySet()) {
            for (Enlace enlace : clases.get(peso)) {
                enlace.setEtiqueta("TER");
                int[] temp = new int[n];
                Arrays.fill(temp, 0);

                temp[enlace.getI()] = 1;
                temp[enlace.getJ()] = 1;

                incidencia.add(temp);
            }
            etiquetas.addAll(clases.get(peso));
        }

        for (Enlace enlace : enlaces) {
            if (enlace.getEtiqueta().equals("no edge"))
                enlace.setEtiqueta("TER");
        }


        double[][] matrizDistancias = new double[n][n];

        // Inicialización de la matriz de distancias a 0
        matrizDistancias = inicializarMatriz(matrizDistancias);

        // Copia de los valores de la matriz de pesos en la matriz de distancias
        matrizDistancias = copiarMatriz(matrizPesos, matrizDistancias);

        // Definición de la matriz de mayor orden
        double[][] matrizOrdenSuperior = new double[n][n];

        // Inicialización de la matriz de mayor orden a 0
        matrizOrdenSuperior = inicializarMatriz(matrizOrdenSuperior);

        // Copia de los valores de la matriz de pesos en la matriz de mayor orden
        matrizOrdenSuperior = copiarMatriz(matrizPesos, matrizOrdenSuperior);

        matrizOrdenSuperior = generarMatrizOrdenSuperior(matrizOrdenSuperior);
        matrizDistancias = generarMatrizDistanciasMinimas(matrizDistancias, matrizOrdenSuperior);

        compararConMatrizPesos(matrizDistancias);

        Date fechaFin = new Date();

        Salida salida = new Salida(entrada, pfnet, fechaInicio, fechaFin, id, pathSalida);
        salida.run();
    }

    public class Enlace {
        protected int i;
        protected int j;
        protected String etiqueta;

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
            return "Enlace{" +
                    "i=" + i +
                    ", j=" + j +
                    ", etiqueta='" + etiqueta + '\'' +
                    '}';
        }
    }
}

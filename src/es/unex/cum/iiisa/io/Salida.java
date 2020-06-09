package es.unex.cum.iiisa.io;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Salida {
    private final DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    private final DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
    private final DateFormat df3 = new SimpleDateFormat("hh:mm:ss");
    private final double[][] matriz;
    private final Date fechaInicio;
    private final Date fechaFin;
    private final String fichEst;
    private final String fichSal;
    private final Entrada entrada;

    public Salida(Entrada entrada, double[][] matriz, Date fechaInicio, Date fechaFin, String id) {
        this.entrada = entrada;
        this.matriz = matriz;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fichEst = "estadistica_" + id + ".txt";
        this.fichSal = "salida_" + id + ".txt";
    }

    public void run() {
        System.out.println();
        for (int i = 0; i< matriz.length; i++){
            for (int j = 0; j < matriz[i].length; j++){
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }

        try {
            FileWriter myWriter = new FileWriter(fichEst);
            myWriter.write(df2.format(fechaInicio) + "\n" + entrada.getIdentificador() + "\n" + df2.format(fechaInicio) + "\n" + df3.format(fechaInicio) + "\n" + df2.format(fechaFin) + "\n" + df3.format(fechaFin));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        List<String> pares = new ArrayList<>();

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != 0)
                    pares.add((i + 1) + "\t" + (j + 1) + "\t" + matriz[i][j]);
            }
        }

        List<String> lineas = new ArrayList<>();
        lineas.add(entrada.getIdentificador() + "_sal");
        lineas.add(entrada.getTipoValores());
        lineas.add(String.valueOf(entrada.getN()));
        lineas.add(String.valueOf(entrada.getNumDecimales()));
        lineas.add(String.valueOf(entrada.getValorMin()));
        lineas.add(String.valueOf(entrada.getValorMax()));
        lineas.add("List");
        lineas.add(String.valueOf(pares.size()));
        lineas.add(entrada.getTipoMatriz());
        lineas.addAll(pares);

        try {
            FileWriter myWriter = new FileWriter(fichSal);
            for (String linea : lineas)
                myWriter.write(linea + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

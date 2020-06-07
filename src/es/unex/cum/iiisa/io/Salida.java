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
    private final int[][] matriz;
    private final long tiempoEjecucion;
    private final Date fechaInicio;
    private final Date fechaFin;
    private final String id;
    private final String fichEst;
    private final String fichSal;

    public Salida(int[][] matriz, long tiempoEjecucion, Date fechaInicio, Date fechaFin, String id) {
        this.matriz = matriz;
        this.tiempoEjecucion = tiempoEjecucion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.id = id;
        this.fichEst = "estadistica_" + id + ".txt";
        this.fichSal = "salida_" + id + ".txt";
    }

    public void run() {
        try {
            FileWriter myWriter = new FileWriter(fichEst);
            myWriter.write(df2.format(fechaInicio) + "\n" + id + "\n" + df2.format(fechaInicio) + "\n" + df3.format(fechaInicio) + "\n" + df2.format(fechaFin) + "\n" + df3.format(fechaFin));
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        List<String> parejas = new ArrayList<>();

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] != 0)
                    parejas.add((i + 1) + "\t" + (j + 1) + "\t" + matriz[i][j]);
            }
        }

        try {
            FileWriter myWriter = new FileWriter(fichSal);
            for (String pareja : parejas)
                myWriter.write(pareja + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

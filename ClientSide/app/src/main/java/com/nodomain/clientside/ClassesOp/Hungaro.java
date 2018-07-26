package com.nodomain.clientside.ClassesOp;
import java.util.Arrays;

public class Hungaro {
    private final double[][] costos;
    private final int filas, cols, tam;
    private final double[] vectorPorTrabajador, vectorPorTrabajo;
    private final int[] minRelajacionPorTrabajador;
    private final double[] minRelajacionPorTrabajo;
    private final int[] trabajoPorTrabajador, trabajadorPorTrabajo;
    private final int[] trabajadorPorTrabajoPrimo;
    private final boolean[] trabajadorComprometido;


    public Hungaro(double[][] costos) {
        this.tam = Math.max(costos.length, costos[0].length);
        this.filas = costos.length;
        this.cols = costos[0].length;
        this.costos = new double[this.tam][this.tam];
        for (int w = 0; w < this.tam; w++) {
            if (w < costos.length) {
                this.costos[w] = Arrays.copyOf(costos[w], this.tam);
            } else {
                this.costos[w] = new double[this.tam];
            }
        }
        vectorPorTrabajador = new double[this.tam];
        vectorPorTrabajo = new double[this.tam];
        minRelajacionPorTrabajador = new int[this.tam];
        minRelajacionPorTrabajo = new double[this.tam];
        trabajadorComprometido = new boolean[this.tam];
        trabajadorPorTrabajoPrimo = new int[this.tam];
        trabajoPorTrabajador = new int[this.tam];
        Arrays.fill(trabajoPorTrabajador, -1);
        trabajadorPorTrabajo = new int[this.tam];
        Arrays.fill(trabajadorPorTrabajo, -1);
    }


    protected void solInicial() {
        for (int j = 0; j < tam; j++) {
            vectorPorTrabajo[j] = Double.POSITIVE_INFINITY;
        }
        for (int w = 0; w < tam; w++) {
            for (int j = 0; j < tam; j++) {
                if (costos[w][j] < vectorPorTrabajo[j]) {
                    vectorPorTrabajo[j] = costos[w][j];
                }
            }
        }
    }

    public int[] ejecutar() {

        reducir();
        solInicial();
        codicioso();

        int w = obtTrabajador();
        while (w < tam) {
            initializePhase(w);
            ejecutarFase();
            w = obtTrabajador();
        }
        int[] result = Arrays.copyOf(trabajoPorTrabajador, filas);
        for (w = 0; w < result.length; w++) {
            if (result[w] >= cols) {
                result[w] = -1;
            }
        }
        return result;
    }


    protected void ejecutarFase() {
        while (true) {
            int relTrabajador = -1, relTrabajo = -1;
            double relMinima = Double.POSITIVE_INFINITY;
            for (int j = 0; j < tam; j++) {
                if (trabajadorPorTrabajoPrimo[j] == -1) {
                    if (minRelajacionPorTrabajo[j] < relMinima) {
                        relMinima = minRelajacionPorTrabajo[j];
                        relTrabajador = minRelajacionPorTrabajador[j];
                        relTrabajo = j;
                    }
                }
            }
            if (relMinima > 0) {
                actRelajacion(relMinima);
            }
            trabajadorPorTrabajoPrimo[relTrabajo] = relTrabajador;
            if (trabajadorPorTrabajo[relTrabajo] == -1) {

                int trabajadorComp = relTrabajo;
                int trabajador = trabajadorPorTrabajoPrimo[trabajadorComp];
                while (true) {
                    int temp = trabajoPorTrabajador[trabajador];
                    aparear(trabajador, trabajadorComp);
                    trabajadorComp = temp;
                    if (trabajadorComp == -1) {
                        break;
                    }
                    trabajador = trabajadorPorTrabajoPrimo[trabajadorComp];
                }
                return;
            } else {

                int trabajadorAux = trabajadorPorTrabajo[relTrabajo];
                trabajadorComprometido[trabajadorAux] = true;
                for (int j = 0; j < tam; j++) {
                    if (trabajadorPorTrabajoPrimo[j] == -1) {
                        double relajacion = costos[trabajadorAux][j] - vectorPorTrabajador[trabajadorAux]
                                - vectorPorTrabajo[j];
                        if (minRelajacionPorTrabajo[j] > relajacion) {
                            minRelajacionPorTrabajo[j] = relajacion;
                            minRelajacionPorTrabajador[j] = trabajadorAux;
                        }
                    }
                }
            }
        }
    }


    protected int obtTrabajador() {
        int w;
        for (w = 0; w < tam; w++) {
            if (trabajoPorTrabajador[w] == -1) {
                break;
            }
        }
        return w;
    }

    protected void codicioso() {
        for (int w = 0; w < tam; w++) {
            for (int j = 0; j < tam; j++) {
                if (trabajoPorTrabajador[w] == -1 && trabajadorPorTrabajo[j] == -1
                        && costos[w][j] - vectorPorTrabajador[w] - vectorPorTrabajo[j] == 0) {
                    aparear(w, j);
                }
            }
        }
    }


    protected void initializePhase(int w) {
        Arrays.fill(trabajadorComprometido, false);
        Arrays.fill(trabajadorPorTrabajoPrimo, -1);
        trabajadorComprometido[w] = true;
        for (int j = 0; j < tam; j++) {
            minRelajacionPorTrabajo[j] = costos[w][j] - vectorPorTrabajador[w]
                    - vectorPorTrabajo[j];
            minRelajacionPorTrabajador[j] = w;
        }
    }


    protected void aparear(int w, int j) {
        trabajoPorTrabajador[w] = j;
        trabajadorPorTrabajo[j] = w;
    }


    protected void reducir() {
        for (int w = 0; w < tam; w++) {
            double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < tam; j++) {
                if (costos[w][j] < min) {
                    min = costos[w][j];
                }
            }
            for (int j = 0; j < tam; j++) {
                costos[w][j] -= min;
            }
        }
        double[] min = new double[tam];
        for (int j = 0; j < tam; j++) {
            min[j] = Double.POSITIVE_INFINITY;
        }
        for (int w = 0; w < tam; w++) {
            for (int j = 0; j < tam; j++) {
                if (costos[w][j] < min[j]) {
                    min[j] = costos[w][j];
                }
            }
        }
        for (int w = 0; w < tam; w++) {
            for (int j = 0; j < tam; j++) {
                costos[w][j] -= min[j];
            }
        }
    }


    protected void actRelajacion(double slack) {
        for (int w = 0; w < tam; w++) {
            if (trabajadorComprometido[w]) {
                vectorPorTrabajador[w] += slack;
            }
        }
        for (int j = 0; j < tam; j++) {
            if (trabajadorPorTrabajoPrimo[j] != -1) {
                vectorPorTrabajo[j] -= slack;
            } else {
                minRelajacionPorTrabajo[j] -= slack;
            }
        }
    }
}


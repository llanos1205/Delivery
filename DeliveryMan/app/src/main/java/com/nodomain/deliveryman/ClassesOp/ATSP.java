package com.nodomain.deliveryman.ClassesOp;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;

/**
 *
 * @author André
 */
public class ATSP {

    private double ady[][];
    int tam;
    int result[];
    int pos;
    boolean visitados[];
    private LatLng lista[];
    private RutaArco arcos[];
    private RutaArco ruta[];
    private int tamruta;

    public ATSP(int n, RutaArco array[], int cantarcos) {
        this.ady = new double[n][n];
        arcos = new RutaArco[cantarcos];
        lista = new LatLng[n];
        ruta = new RutaArco[n];
        tamruta = 0;
        tam = 0;
        result = new int[n + 1];
        pos = 0;
        visitados = new boolean[n];
        Arrays.fill(visitados, false);

        lista = new LatLng[n];
        for (int i = 0; i < cantarcos; i++) {
            if (i == 0) {
                lista[tam] = array[i].get_origin();
                tam++;
                lista[tam] = array[i].get_end();
                tam++;
            } else {
                if (!this.Existe(array[i].get_origin())) {
                    lista[tam] = array[i].get_origin();
                    tam++;
                }
                if (!this.Existe(array[i].get_end())) {
                    lista[tam] = array[i].get_end();
                    tam++;
                }

            }
        }
        for (int i = 0; i < tam; i++) {
            ady[i][i] = 0;
        }
        for (int i = 0; i < cantarcos; i++) {
            arcos[i] = array[i];
        }
        int i;
        int j;
        for (int k = 0; k < cantarcos; k++) {
            i = this.getIndice(arcos[k].get_origin());
            j = this.getIndice(arcos[k].get_end());
            ady[i][j] = arcos[k].get_distance();
        }

    }

    private RutaArco getArco(LatLng orig, LatLng dest) {
        for (int i = 0; i < arcos.length; i++) {
            if (arcos[i].get_end().latitude == dest.latitude && arcos[i].get_end().longitude == dest.longitude && arcos[i].get_origin().latitude == orig.latitude && arcos[i].get_origin().longitude == orig.longitude) {
                return arcos[i];
            }
        }
        return null;
    }

    public void indicesaArco() {
        RutaArco aux;

        for (int i = 0; i < (result.length - 1); i++) {
            aux = getArco(lista[result[i]], lista[result[i + 1]]);
            ruta[tamruta] = aux;
            tamruta++;
        }


    }

    private int getIndice(LatLng val) {
        for (int i = 0; i < tam; i++) {
            if (val.latitude == lista[i].latitude && val.longitude == lista[i].longitude) {
                return i;
            }
        }
        return -1;
    }

    private boolean Existe(LatLng val) {
        for (int i = 0; i < tam; i++) {
            if (val.latitude == lista[i].latitude && val.longitude == lista[i].longitude) {
                return true;
            }
        }

        return false;
    }

    int[] verticeCiclo(int[] asignaciones) {
        int cantCiclos = 0;
        int verticeAux[] = new int[tam];
        boolean visitados[] = new boolean[asignaciones.length];
        Arrays.fill(visitados, false);
        int pos = 0;
        for (int i = 0; i < asignaciones.length; i++) {
            if (visitados[i] == false) {
                visitados[i] = true;
                verticeAux[cantCiclos] = i;
                cantCiclos++;
                int k = asignaciones[i];
                while (visitados[k] == false) {
                    visitados[k] = true;
                    k = asignaciones[k];
                }
            }
        }
        int vertices[] = new int[cantCiclos];
        vertices = Arrays.copyOf(verticeAux, cantCiclos);
        return vertices;
    }

    public RutaArco[] ejecutar() {
        double cactus[][] = new double[tam][tam];
        double adyInducido[][] = new double[tam][tam];
        for (int i = 0; i < tam; i++) {
            adyInducido[i] = Arrays.copyOf(ady[i], tam);
            adyInducido[i][i] = Double.POSITIVE_INFINITY;
        }
        int k = tam;
        int traduccion[] = new int[tam];
        while (k > 1) {
            //Se forman los ciclos:
            boolean estaEnInducido[];
            Hungaro hungaro = new Hungaro(adyInducido);
            int asignacion[];
            asignacion = hungaro.ejecutar();

            //Se obtiene un vertice de cada ciclo
            int vertices[] = this.verticeCiclo(asignacion);

            if (k == tam) {
                traduccion = new int[vertices.length];
                for (int i = 0; i < traduccion.length; i++) {
                    traduccion[i] = vertices[i];
                }

                for (int i = 0; i < asignacion.length; i++) {
                    cactus[i][asignacion[i]] = ady[i][asignacion[i]];
                }

            } else {
                for (int i = 0; i < asignacion.length; i++) {
                    cactus[traduccion[i]][traduccion[asignacion[i]]] = ady[traduccion[i]][traduccion[asignacion[i]]];
                }

                int auxTrad[] = traduccion;
                traduccion = new int[vertices.length];
                for (int i = 0; i < traduccion.length; i++) {
                    traduccion[i] = auxTrad[vertices[i]];
                }

            }
            estaEnInducido = new boolean[asignacion.length];
            Arrays.fill(estaEnInducido, false);
            k = vertices.length;
            adyInducido = new double[k][k];
            for (int i = 0; i < vertices.length; i++) {
                for (int j = 0; j < vertices.length; j++) {

                    adyInducido[i][j] = ady[traduccion[i]][traduccion[j]];

                }
                adyInducido[i][i] = Double.POSITIVE_INFINITY;
            }
        }
//        for (int i = 0; i < tam; i++) {
//            for (int j = 0; j < tam; j++) {
//                System.out.print(cactus[i][j]);
//                System.out.print(" ");
//            }
//            System.out.print("\n");
//
//        }
        dfs(cactus);
        indicesaArco();
        return ruta;

    }

    private void dfs(double cactus[][]) {
        dfsrec(0, cactus);
        result[pos] = 0;
    }

    private void dfsrec(int n, double cactus[][]) {
        visitados[n] = true;
        result[pos] = n;
        pos++;

        for (int j = 0; j < tam; j++) {
            if (cactus[n][j] != 0 && visitados[j] == false) {
                dfsrec(j, cactus);
            }
        }

    }
}


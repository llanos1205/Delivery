package com.nodomain.clientside.ClassesOp;
import com.google.android.gms.maps.model.LatLng;

public class Prim {

    private double ady[][];
    private LatLng lista[];
    private RutaArco arcos[];
    private boolean visitados[];
    private double key[];
    private int n;
    private int padre[];
    private Vertice vert[];
    private int rutaindices[];
    private int tamrutaindices;
    private RutaArco ruta[];
    private int tamruta;

    public Prim(RutaArco array[], int tam, int cantarcos) {//inicializador: recibe un array[] de los arcos, tam que representa cuantos vertices hay en el grafo y cantarcos que es la cantidad de arcos en grafo
        arcos = new RutaArco[cantarcos];
        n = 0;
        tamruta = 0;
        tamrutaindices = 0;
        lista = new LatLng[tam];
        for (int i = 0; i < cantarcos; i++) {
            if (i == 0) {
                lista[n] = array[i].get_origin();
                n++;
                lista[n] = array[i].get_end();
                n++;
            } else {
                if (!this.Existe(array[i].get_origin())) {
                    lista[n] = array[i].get_origin();
                    n++;
                }
                if (!this.Existe(array[i].get_end())) {
                    lista[n] = array[i].get_end();
                    n++;
                }

            }
        }

        rutaindices = new int[tam+1];
        ruta = new RutaArco[tam];
        vert = new Vertice[tam];
        ady = new double[tam][tam];
        visitados = new boolean[tam];
        key = new double[tam];
        padre = new int[tam];
        for (int i = 0; i < tam; i++) {
            visitados[i] = false;
            key[i] = Double.MAX_VALUE;
            padre[i] = -1;
            vert[i] = new Vertice(i);
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

    private int getIndice(LatLng val) {
        for (int i = 0; i < n; i++) {
            if (val.latitude == lista[i].latitude && val.longitude == lista[i].longitude) {
                return i;
            }
        }
        return -1;
    }

    private boolean Existe(LatLng val) {
        for (int i = 0; i < n; i++) {
            if (val.latitude == lista[i].latitude && val.longitude == lista[i].longitude) {
                return true;
            }
        }

        return false;
    }

    public void preOrden() {
        preOrden(0);
        rutaindices[tamrutaindices] = 0;
        tamrutaindices++;

    }

    private void preOrden(int pos) {
        this.rutaindices[tamrutaindices] = pos;
        tamrutaindices++;
        for (int i = 0; i < vert[pos].getTam(); i++) {
            preOrden(vert[pos].getHijo(i));
        }
    }

    private int minKey() {
        double min = Double.MAX_VALUE;
        int min_indice = -1;

        for (int i = 0; i < n; i++) {
            if (!visitados[i] && key[i] < min) {
                min = key[i];
                min_indice = i;
            }
        }
        return min_indice;
    }

    private RutaArco getArco(LatLng orig, LatLng dest) {
        for (int i = 0; i < arcos.length; i++) {
            if (arcos[i].get_end().latitude == dest.latitude && arcos[i].get_end().longitude == dest.longitude && arcos[i].get_origin().latitude == orig.latitude && arcos[i].get_origin().longitude == orig.longitude) {
                return arcos[i];
            }
        }
        return null;
    }

    public RutaArco[] indicesaArco() {
        genMST();
        preOrden();
        RutaArco aux;
        for (int i = 0; i < (tamrutaindices - 1); i++) {
            aux = getArco(lista[rutaindices[i]], lista[rutaindices[i+1]]);
            ruta[tamruta] = aux;
            tamruta++;
        }
        return ruta;
    }

    public void genMST() {

        key[0] = 0;
        padre[0] = -1;
        int i = this.minKey();
        while (this.minKey() != -1) {
            i = this.minKey();
            visitados[i] = true;
            for (int j = 0; j < n; j++) {
                if (j != i && visitados[j] == false && ady[i][j] != 0) {
                    if (ady[i][j] < key[j]) {
                        if (padre[j] != -1) {
                            vert[padre[j]].elimHijo(j);
                        }
                        if (vert[i].getTam() > 0) {
                            int k = 0;
                            if (ady[i][j] < ady[i][vert[i].getHijo(0)]) {
                                vert[i].setHijo(j, 0);
                            } else {
                                for (k = 0; k < vert[i].getTam() - 1; k++) {
                                    if (ady[vert[i].getHijo(k)][j] < ady[vert[i].getHijo(k)][vert[i].getHijo(k + 1)]) {
                                        vert[i].setHijo(j, k + 1);
                                        break;
                                    }
                                }
                                if (k == vert[i].getTam() - 1) {
                                    vert[i].setHijo(j);
                                }
                            }
                        } else {
                            vert[i].setHijo(j);
                        }
                        padre[j] = i;
                        key[j] = ady[i][j];
                    }
                }
            }
        }
    }

}

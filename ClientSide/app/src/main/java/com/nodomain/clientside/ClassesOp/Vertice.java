package com.nodomain.clientside.ClassesOp;

public class Vertice  {

    private int id;
    private int hijos[];
    private int n;

    public Vertice(int identificador) {
        id = identificador;
        hijos=new int[99];
        n=0;
    }


    public void setId(int i){
        id=i;
    }
    public int getId(){
        return id;
    }
    public void elimHijo(int hijo){
        for(int i=0;i<n;i++){
            if(hijo==hijos[i]){
                for(int j=i;j<n-1;j++){
                    hijos[j]=hijos[j+1];
                }
            }
        }
        n--;
    }
    public void setHijo(int hijo) {
        //Ola
        hijos[n]=hijo;
        n++;
        //Comentario

    }

    public void setHijo(int hijo, int pos) {
        for(int i=n;i>pos;i--){
            hijos[i]=hijos[i-1];
        }
        hijos[pos]=hijo;
        n++;

    }

    public int getHijo(int pos) {
        return hijos[pos];

    }

    public int getTam(){
        return n;
    }

}
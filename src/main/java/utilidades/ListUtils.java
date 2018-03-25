package utilidades;

import entidades.Ficha;

import java.util.ArrayList;

public class ListUtils {

    /*public static ArrayList<Ficha> getCopiaSin(ArrayList<Ficha> listaFichas, Ficha objetivo){
        ArrayList<Ficha> resultado = new ArrayList<>();
        for (Ficha e : listaFichas) {
            if (e.getId() != objetivo.getId()) {
                resultado.add(new Ficha(e.getIzq(),e.getArr(),e.getDer(),e.getAbj(),e.getId()));
            }
        }
        return resultado;
    }*/

    public static ArrayList<Ficha> getCopia(ArrayList<Ficha> listaFichas){
        ArrayList<Ficha> resultado = new ArrayList<>();
        for(Ficha f : listaFichas){
            resultado.add(f.clone());
        }
        return resultado;
    }

    public static ArrayList<Ficha> getCopiaSin(){
        /**
         * ver como hacer este metodo para que haga este codigo:
         ArrayList<Ficha> aux = new ArrayList<Ficha>();
         for (Ficha e : fichas) {
            if (e.getId() != f.getId()) {
                aux.add(e);
            }
         }
         * */
        return null;
    }
}

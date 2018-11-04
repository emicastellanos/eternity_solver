package utilidades;

import entidades.Ficha;
import entidades.Tablero;

import java.util.ArrayList;
import java.util.List;

public class Utils {

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

    public static void backUbicarPrimera(Tablero tablero, ArrayList<Ficha> fichas) {
        boolean termino = false;

        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        f.setUsada(true);
                        tablero.aumentarPosicion();
                        termino = true;
                        break;
                    } else {
                        f.rotar();
                        tablero.eliminarUltima();
                    }
                }
            }
            if(termino){
                break;
            }
        }
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

    public Boolean validarSoluciones (ArrayList<Tablero> soluciones){
        for(Tablero solucion : soluciones) {
            if(solucion.esSolucionFinal()){
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static String imprimirTablerosSolucion(List<Tablero> tableros){
        StringBuffer buffer = new StringBuffer();
        int i = 1;
        for(Tablero t : tableros){
            buffer.append("\nSOLUCION # " + i +"\n");
            buffer.append(t.imprimirUsadas() + "\n");
            buffer.append(t.imprimirse() + "\n");
            i++;
        }
        return buffer.toString();
    }

    public static ArrayList<Ficha> getCopiaFichasSinUsar(ArrayList<Ficha> fichas) {
        ArrayList<Ficha> result = new ArrayList<>();
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                Ficha nueva = f.clone();
                result.add(nueva);
            }
        }
        return result;
    }
}

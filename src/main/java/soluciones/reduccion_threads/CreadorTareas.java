package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.Utils;

import java.util.ArrayList;

public class CreadorTareas {
    static final Logger resultLog = Logger.getLogger("resultadoLogger");

    /**
     * Backtracking que devuelve la cantidad de estados que se podrian obtener al llegar
     * a un nivel dado (nivelObjetivo)
     */
    public int backNivelParaAnalisis(ArrayList<Ficha> fichas, Integer nivel, Tablero tablero, Integer nivelObjetivo) {
        int cantidadTareas = 0;
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        nivel += 1;
                        f.setUsada(true);
                        tablero.aumentarPosicion();
                        if (nivel.equals(nivelObjetivo)) {
                            cantidadTareas++;
                        } else {
                            cantidadTareas+=backNivelParaAnalisis(fichas, nivel, tablero, nivelObjetivo);
                        }
                        tablero.retrocederPosicion();
                        nivel -= 1;
                        f.setUsada(false);
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }
        return cantidadTareas;
    }

    public int crearTareasInicialesParaAnalisis(Tablero tablero, ArrayList<Ficha> fichas, int nivelObjetivo, boolean ubicarPrimera){
        if (ubicarPrimera){
            Utils.backUbicarPrimera(tablero, fichas);
            return backNivelParaAnalisis(fichas, 1, tablero, nivelObjetivo);
        }else
            return backNivelParaAnalisis(fichas,0,tablero,nivelObjetivo);
    }

    /**
     * Backtracking que devuelve en forma de lista todas los ESTADOs que se puedan obtener al llegar
     * a un nivel dado (nivelObjetivo)
     */
    public ArrayList<Estado> backNivel(ArrayList<Ficha> fichas, Integer nivel, Tablero tablero, Integer nivelObjetivo) {
        ArrayList<Estado> resultado = new ArrayList<>();
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        nivel += 1;
                        f.setUsada(true);
                        tablero.aumentarPosicion();
                        if (nivel.equals(nivelObjetivo)) {
                            Estado estado = new Estado(tablero.clone(), Utils.getCopia(fichas), nivel );
                            resultado.add(estado);
                        } else {
                            resultado.addAll(backNivel(fichas, nivel, tablero, nivelObjetivo));
                        }
                        tablero.retrocederPosicion();
                        nivel -= 1;
                        f.setUsada(false);
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }
        return resultado;
    }


    //TODO Que BUSQUE y coloque la primera ficha una sola vez y a partir de eso conseguir las restantes hasta el nivelInicial
    /** nivelInicial = cantidad de fichas bien colocadas en los tableros devueltos */
    public ArrayList<Estado> crearTareasIniciales(Tablero tablero, ArrayList<Ficha> fichas, int nivelObjetivo){
        return backNivel(fichas,1,tablero,nivelObjetivo);
    }

}

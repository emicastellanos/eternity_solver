package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.Utils;

import java.util.ArrayList;

public class CreadorTareas {
    static final Logger resultLog = Logger.getLogger("resultadoLogger");

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
    public ArrayList<Estado> crearTareasIniciales(Tablero tablero, ArrayList<Ficha> fichas, int nivelInicial, boolean ubicarPrimera){
        if (ubicarPrimera){
            Utils.backUbicarPrimera(tablero, fichas);
            return backNivel(fichas,1,tablero,nivelInicial);
        }else
        return backNivel(fichas,0,tablero,nivelInicial);
    }

}

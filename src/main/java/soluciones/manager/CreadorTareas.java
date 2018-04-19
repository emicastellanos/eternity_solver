package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.Utils;

import java.util.ArrayList;

public class CreadorTareas {

    private Manager manager;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");

    //TODO Debe haber alguna alternativa mejor que pasar una referencia del manager
    public CreadorTareas (Manager m){
        manager = m;
    }

    /** como cuesta mucho arrancar estas tareas seria mas conveniente devolver algo que contenga
    un tablero (clonado), una copia de las fichas, y el nivel y que el manager se encargue de
    reutilizar los threads muertos pasandole esa info y volviendo a startear.
    * */
    public ArrayList<Tarea> crear(Estado estado){
        ArrayList<Tarea> resultado = new ArrayList<>();
        //resultLog.info("ENTRANDO A CREAR (TAREA) " + Thread.currentThread().getName() );
        for(Ficha ficha : estado.getFichas()){
            if(!ficha.isUsada()) {
                for(int i=0; i<4;i++) {
                    estado.getTablero().insertarFinal(ficha);
                    if (estado.getTablero().esSolucion()) {
                        ficha.setUsada(true);
                        Tablero clonado = estado.getTablero().clone();
                        clonado.aumentarPosicion();
                        resultado.add(new Tarea(clonado, Utils.getCopia(estado.getFichas()), null, estado.getNivel(), manager));
                        ficha.setUsada(false);
                    }
                    estado.getTablero().eliminarUltima();
                    ficha.rotar();
                }
            }
        }
        resultLog.info(Thread.currentThread().getName() + " CREO " + resultado.size() + " TAREAS");
        return resultado;
    }

    /**
     * Backtracking que devuelve en forma de lista todas los ESTADOs que se puedan obtener al llegar
     * a un nivel dado (nivelObjetivo)
     */
    public ArrayList<Tarea> backNivel(ArrayList<Ficha> fichas, Integer nivel, Tablero tablero, Integer nivelObjetivo) {
        ArrayList<Tarea> resultado = new ArrayList<>();
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        nivel += 1;
                        f.setUsada(true);
                        tablero.aumentarPosicion();
                        if (nivel.equals(nivelObjetivo)) {
                            Tarea estado = new Tarea(tablero.clone(), Utils.getCopia(fichas), null, nivel, manager);
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

    public ArrayList<Tarea> crearTareasIniciales(Tablero tablero, ArrayList<Ficha> fichas, int nivelInicial){
        //nivelInicial = cantidad de fichas bien colocadas en los tableros devueltos
        return backNivel(fichas,0,tablero,nivelInicial);
    }

}

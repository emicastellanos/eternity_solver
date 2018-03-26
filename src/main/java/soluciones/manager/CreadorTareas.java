package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.GeneradorFichas;
import utilidades.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class CreadorTareas {

    private Manager manager;

    public CreadorTareas (Manager m){
        manager = m;
    }

    /** como cuesta mucho arrancar estas tareas seria mas conveniente devolver algo que contenga
    un tablero (clonado), una copia de las fichas, y el nivel y que el manager se encargue de
    reutilizar los threads muertos pasandole esa info y volviendo a startear.
    * */
    public ArrayList<Tarea> crear(Estado estado){
        ArrayList<Tarea> resultado = new ArrayList<>();
        for(Ficha ficha : estado.getFichas()){
            estado.getTablero().insertarFinal(ficha);
            if(estado.getTablero().esSolucion()){
                Tablero clonado = estado.getTablero().clone();
                clonado.aumentarPosicion();
                resultado.add(new Tarea(clonado,
                        ListUtils.getCopiaSin(estado.getFichas(),ficha), String.valueOf(Tarea.NRO), estado.getNivel(),manager));
            }
            estado.getTablero().eliminarUltima();
        }
        return resultado;
    }

    /**
     * Backtracking que devuelve en forma de lista todas los estados que se puedan obtener al llegar
     * a un nivel dado (nivelObjetivo) y que seran convertidos en tareas poor el manager
     *
     */
    public ArrayList<Estado> backNivel(List<Ficha> fichas, Integer nivel, Tablero tablero, Integer nivelObjetivo) {
        ArrayList<Estado> resultado = new ArrayList<>();
        for (Ficha f : fichas) {
            tablero.insertarFinal(f);
            if(tablero.esSolucion()) {
                ArrayList<Ficha> aux = new ArrayList<>();
                for (Ficha e : fichas) {
                    if (e.getId() != f.getId()) {
                        aux.add(e);
                    }
                }
                nivel += 1;
                tablero.aumentarPosicion();
                if (nivel.equals(nivelObjetivo)) {
                    Estado tarea = new Estado(tablero.clone(), aux, nivel);
                    resultado.add(tarea);
                } else {
                    resultado.addAll(backNivel(aux, nivel, tablero, nivelObjetivo));
                }
                tablero.retrocederPosicion();
                nivel -= 1;
            }
            tablero.eliminarUltima();
        }

        return resultado;
    }

    public ArrayList<Estado> crearTareasIniciales(Tablero tablero, ArrayList<Ficha> fichas, int nivelInicial){
        //nivelInicial = cantidad de fichas bien colocadas en los tableros devueltos
        return backNivel(fichas,0,tablero,nivelInicial);
    }

}

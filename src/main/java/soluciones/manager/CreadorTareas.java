package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.GeneradorFichas;
import utilidades.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class CreadorTareas {

    /* como cuesta mucho arrancar estas tareas seria mas conveniente devolver algo que contenga
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
                        ListUtils.getCopiaSin(estado.getFichas(),ficha), String.valueOf(Tarea.NRO), estado.getNivel()));
            }
            estado.getTablero().eliminarUltima();
        }
        return resultado;
    }

    /**
     * Backtracking que devuelve en forma de lista todas las tareas que se puedan obtener al llegar
     * a un nivel dado (nivelObjetivo)
     */
    public ArrayList<Tarea> backNivel(List<Ficha> fichas, Integer nivel, Tablero tablero, Integer nivelObjetivo) {
        ArrayList<Tarea> resultado = new ArrayList<>();
        for (Ficha f : fichas) {
            tablero.insertarFinal(f);
            if(tablero.esSolucion()) {
                ArrayList<Ficha> aux = new ArrayList<Ficha>();
                for (Ficha e : fichas) {
                    if (e.getId() != f.getId()) {
                        aux.add(e);
                    }
                }
                nivel += 1;
                tablero.aumentarPosicion();
                if (nivel.equals(nivelObjetivo)) {
                    Tarea tarea = new Tarea(tablero.clone(), aux, String.valueOf(Tarea.NRO), nivel);
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

    public ArrayList<Tarea> crearTareasIniciales(Tablero tablero, ArrayList<Ficha> fichas, int nivelInicial){
        //nivelInicial = cantidad de fichas bien colocadas en los tableros devueltos
        return backNivel(fichas,0,tablero,nivelInicial);
    }

    public static void main(String[] args) {
        GeneradorFichas generadorFichas = new GeneradorFichas(5);
        ArrayList <Ficha> fichas = generadorFichas.getFichasUnicas();
        CreadorTareas ct = new CreadorTareas();
        Tablero tablero = new Tablero(5);
        ArrayList<Tarea> tareas = ct.crearTareasIniciales(tablero,fichas,2);

        System.out.println("fin");

    }

}

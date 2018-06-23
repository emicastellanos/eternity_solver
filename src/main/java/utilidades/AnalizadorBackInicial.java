package utilidades;

import entidades.Ficha;
import entidades.Tablero;
import soluciones.reduccion_threads.CreadorTareas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class AnalizadorBackInicial {

    private HashMap<Integer, TiempoCantidad> mapResultados;
    private CreadorTareas creadorTareas;
    private ArrayList<Ficha> fichas;
    private Tablero tablero;

    public class TiempoCantidad{
        BigDecimal tiempo;
        int cantidadEstados;

        public TiempoCantidad(BigDecimal tiempo, int cantidadEstados) {
            this.tiempo = tiempo;
            this.cantidadEstados = cantidadEstados;
        }

        public BigDecimal getTiempo() {
            return tiempo;
        }

        public void setTiempo(BigDecimal tiempo) {
            this.tiempo = tiempo;
        }

        public int getCantidadEstados() {
            return cantidadEstados;
        }

        public void setCantidadEstados(int cantidadEstados) {
            this.cantidadEstados = cantidadEstados;
        }
    }

    public AnalizadorBackInicial (Tablero tablero, ArrayList<Ficha> fichas){
        mapResultados = new HashMap<>();
        creadorTareas = new CreadorTareas();
        this.tablero = tablero;
        this.fichas = fichas;
    }

    public HashMap<Integer, TiempoCantidad> getMapResultados() {
        return mapResultados;
    }

    public void reiniciarEstructuras(){
        for (Ficha ficha: fichas){
            ficha.setUsada(false);
        }
        tablero.limpiarTablero();

    }

    public void analizarNiveles(boolean ponerPrimera){

        for (int i=0; i < 6; i++){
            long inicio = System.nanoTime();
            int cantEstados = creadorTareas.crearTareasIniciales(tablero,fichas,i,ponerPrimera).size();
            long fin = System.nanoTime();

            BigDecimal duracion = new BigDecimal((fin - inicio));
            BigDecimal duracionSeg = (duracion.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);

            mapResultados.put(i,new TiempoCantidad(duracionSeg,cantEstados));

            reiniciarEstructuras();

        }



    }

    public static void main (String[] args){
        int N = 5; /*** TAMAÑO DEL TABLERO ***/
        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(N,N);
        AnalizadorBackInicial analizadorBackInicial = new AnalizadorBackInicial(new Tablero(N),generadorFichas.getFichasUnicas());
        analizadorBackInicial.analizarNiveles(true);


    }




}

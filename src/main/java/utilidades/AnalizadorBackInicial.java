package utilidades;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import soluciones.reduccion_threads.CreadorTareas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class AnalizadorBackInicial {
    static final Logger resultLog = Logger.getLogger("analizadorLogger");
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

        for (int i=2; i <50; i++){
            long inicio = System.nanoTime();
            System.out.println("nivel " + i);
            int cantEstados = creadorTareas.crearTareasInicialesParaAnalisis(tablero, fichas, i, ponerPrimera);
            long fin = System.nanoTime();

            BigDecimal duracion = new BigDecimal((fin - inicio));
            BigDecimal duracionSeg = (duracion.divide(new BigDecimal(1000000000))).setScale(5, RoundingMode.HALF_UP);

            mapResultados.put(i,new TiempoCantidad(duracionSeg,cantEstados));

            reiniciarEstructuras();
        }
    }

    public static void main (String[] args){
        int N = 7; /*** TAMAÑO DEL TABLERO ***/
        int COLORES = N;
        GeneradorFichas generadorFichas = new GeneradorFichasUnicas(N,COLORES);
        AnalizadorBackInicial analizadorBackInicial = new AnalizadorBackInicial(new Tablero(N),generadorFichas.getFichasUnicas());
        analizadorBackInicial.analizarNiveles(true);

        Set<Integer> a = analizadorBackInicial.getMapResultados().keySet();
        resultLog.info("TAMAÑO TABLERO: " + N + "       -   COLORES: "+COLORES);
        for (Integer i : a ){
            resultLog.info("BACK_INICIAL N  |   CANT PEND   |   TIEMPO     ");
            resultLog.info(i +"  | " + analizadorBackInicial.getMapResultados().get(i).getCantidadEstados() + " | " + analizadorBackInicial.getMapResultados().get(i).getTiempo()) ;
        }


    }




}

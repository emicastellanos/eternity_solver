package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Estado {
    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    private Integer nivel;
    private String nombre;
    private static int nroTarea = 1;

    public Estado(Tablero t, ArrayList<Ficha> fichas, Integer nivel){
        this.tablero = t;
        this.nivel = new Integer(nivel);
        this.fichas = fichas;
        this.nombre = "NODO-"+nroTarea+"_nivel-"+nivel;
        this.nroTarea +=1;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    public Integer getNivel() {
        return nivel;
    }

    public String getNombre(){
        return nombre;
    }

}

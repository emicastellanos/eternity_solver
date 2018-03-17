package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Estado {
    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    private Integer nivel;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public Estado (Tablero t, ArrayList<Ficha> fichas, Integer nivel){
        tablero = t;
        this.nivel = new Integer(nivel);
        this.fichas = new ArrayList<>();
        Ficha aux;
        for(Ficha f : fichas){
            aux = new Ficha(f.getIzq(),f.getArr(),f.getDer(),f.getAbj(),f.getId());
            this.fichas.add(aux);
        }
        String todas ="";
        ArrayList<Ficha> todasFichas = tablero.getFichasUsadas();
        for(Ficha ficha : todasFichas){
            todas+= String.valueOf(ficha.getId()) + " - ";
        }
        resultLog.info(Thread.currentThread().getName() + " ESTADO (tablero): " + todas);
        todas="";
        for(Ficha ficha : fichas){
            todas+= String.valueOf(ficha.getId()) + " - ";
        }
        resultLog.info(Thread.currentThread().getName() + " ESTADO  (para usar): " + todas);
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
}

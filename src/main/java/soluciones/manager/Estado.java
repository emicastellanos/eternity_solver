package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;

import java.util.ArrayList;

public class Estado {
    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    private Integer nivel;

    public Estado (Tablero t, ArrayList<Ficha> fichas, Integer nivel){
        tablero = t;
        this.nivel = new Integer(nivel);
        this.fichas = new ArrayList<>();
        Ficha aux;
        for(Ficha f : fichas){
            aux = new Ficha(f.getIzq(),f.getArr(),f.getDer(),f.getAbj(),f.getId());
            this.fichas.add(aux);
        }
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

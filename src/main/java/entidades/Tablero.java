package entidades;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Tablero {
    private int N;
    private List<String> usadas;

    private Ficha[][] tablero;

    public class Posicion{
        int i;
        int j;

        public Posicion(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }
    }

    public Tablero(int n) {
        this.N = n;
        usadas = new ArrayList<>();
        tablero = new Ficha[N][N];

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                tablero[i][j] = null;
    }

    public int getTotalFichasPuestas() {
        int t = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tablero[i][j] != null) {
                    t++;
                }
        return t;
    }


    public void setPosicion(int fila, int columna, Ficha ficha) {
        tablero[fila][columna] = ficha;
        if(ficha!=null){
            usadas.add(ficha.getId());
        }
    }

    public void liberarPosicion(int fila, int columna) {
        if(tablero[fila][columna]!=null){
            usadas.remove(tablero[fila][columna].getId());
        }
        tablero[fila][columna] = null;
    }

    public synchronized String imprimirse() {
        String result = "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tablero[i][j] != null) {
                    result += tablero[i][j].imprimirse() + "|";
                } else {
                    result += "*-*-*-*|";
                }

            }
            result += "\n";
        }
        return result;
    }

    public Boolean estaLibre(int i, int j) {
        return tablero[i][j] == null;
    }


    public Ficha getPosicion(int fila, int col) {
        return tablero[fila][col];
    }


    public Boolean esSolucion(int fila, int columna, Ficha f) {

        if (fila == 0) {
            if (f.getArr() != 0)
                return false;
        }
        if (columna == 0) {
            if (f.getIzq() != 0)
                return false;
        }
        if (fila == N - 1) {
            if (f != null && f.getAbj() != 0) {
                return false;
            }
        }
        if (columna == N - 1) {
            if (f.getDer() != 0) {
                return false;
            }
        }
        if (fila != 0 && tablero[fila - 1][columna] != null && tablero[fila - 1][columna].getAbj() != f.getArr()) {
            return false;
        }
        if (columna != 0 && tablero[fila][columna - 1] != null && tablero[fila][columna - 1].getDer() != f.getIzq()) {
            return false;
        }

        return true;
    }

    public Boolean esSolucion() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tablero[i][j] != null) {
                    if (i == 0 && tablero[i][j].getArr() != 0) {
                        return false;
                    }
                    if (i == N - 1 && tablero[i][j].getAbj() != 0) {
                        return false;
                    }
                    if (j == 0 && tablero[i][j].getIzq() != 0) {
                        return false;
                    }
                    if (j == N - 1 && tablero[i][j].getDer() != 0) {
                        return false;
                    }
                    if (j >= 0 && j < N - 1) {
                        if (tablero[i][j + 1] != null && tablero[i][j].getDer() != tablero[i][j + 1].getIzq()) {
                            return false;
                        }
                    }
                    if (i >= 0 && i < N - 1) {
                        if (tablero[i + 1][j] != null && tablero[i][j].getAbj() != tablero[i + 1][j].getArr()) {
                            return false;
                        }
                    }
                    if (i > 0 && i < N - 1) {
                        if (tablero[i][j] != null && (tablero[i][j].getAbj() == 0 || tablero[i][j].getArr() == 0)) {
                            return false;
                        }
                    }
                    if (j > 0 && j < N - 1) {
                        if (tablero[i][j] != null && (tablero[i][j].getIzq() == 0 || tablero[i][j].getDer() == 0)) {
                            return false;
                        }
                    }
                } else return true;
            }
        }
        return true;
    }

    public Boolean esSolucionFinal() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tablero[i][j] == null)
                    return false;

        return true;
    }

    public void insertarFinal(Ficha f, Logger resultLog) {
        boolean seguir = true;
        for (int i = 0; i < N && seguir; i++)
            for (int j = 0; j < N && seguir; j++)
                if (tablero[i][j] == null) {
                    tablero[i][j] = f;
                    usadas.add(f.getId());
                    seguir = false;
                    resultLog.info("\n"+Thread.currentThread().getName() + "\nPONER [" + i + "," + j + "] \n "+this.imprimirse());
                    /*resultLog.info("PONER [" + i + "," + j + "] \n");
                    resultLog.info(this.imprimirse());*/
                }
    }

    public boolean estaUsada(Ficha f){
        return usadas.contains(f.getId());
    }

    public Posicion getUltimaPosicion () {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tablero[i][j] == null) {
                    return new Posicion(i, j);
                }
        return null;
    }

    public void eliminarUltima(Logger resultLog) {
        boolean seguir = true;
        Ficha eliminada = null;
        for (int i = 0; i < N && seguir; i++) {
            for (int j = 0; j < N && seguir; j++) {
                if (tablero[i][j] == null) {
                    if (j == 0) {
                        eliminada = tablero[i - 1][N - 1];
                        tablero[i - 1][N - 1] = null;
                        resultLog.info("ELIMINAR [" + String.valueOf(i - 1) + "," + String.valueOf(N-1) + "] " + Thread.currentThread().getName());
                    } else {
                        eliminada = tablero[i][j - 1] ;
                        tablero[i][j - 1] = null;
                        resultLog.info("ELIMINAR [" + i + "," + String.valueOf(j-1) + "] " + Thread.currentThread().getName());
                    }
                    seguir = false;
                } else {
                    if (j == N - 1 && i == N - 1) {
                        eliminada = tablero[i][j];
                        tablero[i][j] = null;
                        seguir = false;
                        resultLog.info("ELIMINAR [" + i + "," + j + "] " + Thread.currentThread().getName());
                    }
                }
            }
        }
        usadas.remove(eliminada.getId());
    }


    @Override
    public Tablero clone(){
        Tablero result = new Tablero(this.N);

        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                result.setPosicion(i,j,getPosicion(i,j));
            }
        }
        return result;
    }

    public String imprimirUsadas(){
        StringBuffer result = new StringBuffer();
        for(String s : usadas){
            result.append(s + ", ");
        }
        return result.toString();
    }

    public boolean entra (Ficha f){
        Posicion p = getUltimaPosicion();
        setPosicion(p.getI(),p.getJ(),f);
        boolean result = esSolucion();
        liberarPosicion(p.getI(),p.getJ());
        return result;
    }
}

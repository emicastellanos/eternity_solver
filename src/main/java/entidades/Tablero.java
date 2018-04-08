package entidades;

import java.util.ArrayList;

public class Tablero {
    private int N;

    private int fila;
    private int columna;

    private Ficha[][] tablero;


    public Tablero(int n) {
        this.N = n;
        tablero = new Ficha[N][N];
        fila = 0;
        columna = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                tablero[i][j] = null;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void aumentarPosicion() {
	    	if(columna != N-1) {
	    		columna++;
	    	}
	    	else {
	    		fila+=1;
	    		columna = 0;
	    	}
    }


    public ArrayList<Ficha> getFichasUsadas(){
        ArrayList<Ficha> usadas = new ArrayList<>();
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++){
                if(tablero[i][j]==null){
                    break;
                }
                usadas.add(tablero[i][j]);
            }
        return usadas;
    }



    
    public void retrocederPosicion() {
	    	if(columna != 0) {
	    		columna--;
	    	}
	    	else {
	    		columna = N-1;
	    		fila--;
	    	}
    }
    


    public Boolean esSolucion() {
    	
    	//arriba
        if (fila != 0 && tablero[fila-1][columna].getAbj() != tablero[fila][columna].getArr()) 
            return false;
        
        //izquierda
        if (columna != 0 && tablero[fila][columna-1].getDer() != tablero[fila][columna].getIzq()) 
            return false;
            
        if (columna != N-1 && tablero[fila][columna].getDer() == 0 )
        	return false;
        
        if (fila != N-1 && tablero[fila][columna].getAbj() == 0 )
        	return false;
       
        if (fila == 0 && tablero[fila][columna].getArr() != 0) 
                return false;
        
        if (columna == 0 && tablero[fila][columna].getIzq() != 0) 
                return false;
            
        
        if (fila == N-1 && tablero[fila][columna].getAbj() != 0) 
                return false;
           
        
        if (columna == N - 1 && tablero[fila][columna].getDer() != 0)
                return false;
        
        
        return true;
    }


    public Boolean esSolucionFinal() {
       if(fila == N-1 && columna == N-1) {
    	   		return true;
       }
       else {
    	   		return false;
       }
    }


    public void insertarFinal(Ficha f) {
       tablero[fila][columna] = f; 
    }


    public void eliminarUltima() {
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


    public String imprimirUsadas(){
        StringBuffer result = new StringBuffer();

        ArrayList<Ficha> todasFichas = getFichasUsadas();
        for(Ficha ficha : todasFichas) {
            result.append(String.valueOf(ficha.getId()) + "- ");
        }
        return result.toString();
    }
    


    /*public Boolean esSolucionVieja() {
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
    }*/

    /*public Boolean esSolucionFinalVieja() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tablero[i][j] == null)
                    return false;

        return true;
    }*/


    /*public void insertarFinalVieja(Ficha f) {

        boolean seguir = true;
        for (int i = 0; i < N && seguir; i++)
            for (int j = 0; j < N && seguir; j++)
                if (tablero[i][j] == null) {
                    tablero[i][j] = f;
                    seguir = false;
                    // resultLog.info("\n"+Thread.currentThread().getName() + "\nPONER [" + i + "," + j + "] \n "+this.imprimirse());
                    resultLog.info("PONER [" + i + "," + j + "] \n");
                    resultLog.info(this.imprimirse());
                }
    }*/




    /*public void eliminarUltimaVieja() {
        boolean seguir = true;
        Ficha eliminada = null;
        for (int i = 0; i < N && seguir; i++) {
            for (int j = 0; j < N && seguir; j++) {
                if (tablero[i][j] == null) {
                    if (j == 0) {
                        eliminada = tablero[i - 1][N - 1];
                        tablero[i - 1][N - 1] = null;
                        //resultLog.info("ELIMINAR [" + String.valueOf(i - 1) + "," + String.valueOf(N-1) + "] " + Thread.currentThread().getName());
                    } else {
                        eliminada = tablero[i][j - 1] ;
                        tablero[i][j - 1] = null;
                        //resultLog.info("ELIMINAR [" + i + "," + String.valueOf(j-1) + "] " + Thread.currentThread().getName());
                    }
                    seguir = false;
                } else {
                    if (j == N - 1 && i == N - 1) {
                        eliminada = tablero[i][j];
                        tablero[i][j] = null;
                        seguir = false;
                        //resultLog.info("ELIMINAR [" + i + "," + j + "] " + Thread.currentThread().getName());
                    }
                }
            }
        }
    }*/

    public Ficha getPosicion(int fila, int col) {
        return tablero[fila][col];
    }

    public void setPosicion(int fila, int col,Ficha f) {
        tablero[fila][col]=f;
    }


    @Override
    public Tablero clone() {
        Tablero result = new Tablero(this.N);
        result.setFila(this.getFila());
        result.setColumna(this.getColumna());
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Ficha clon = getPosicion(i, j);
                if(clon!=null){
                    result.setPosicion(i, j, clon.clone());
                }else {
                    result.setPosicion(i, j, null);
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (getPosicion(i,j).getId() != ((Tablero) o).getPosicion(i,j).getId()){
                    return false;
                }
            }
        }
        return true;
    }
}

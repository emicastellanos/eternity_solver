import java.util.ArrayList;

public class CodigosUsadosAlgunaVez {

    public static void crearFichasAutoMaximoColores(){
        ArrayList<Ficha> fichas = new ArrayList<Ficha>();
        int N = 4;
        // fichas y N deberian llegarle por parametro pero proximamente esto se borra
        Ficha ficha = null ;
        Ficha[][] tablero = new Ficha[N][N];
        int id = 1;
        int color = 1;
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                if(i==0 && j==0){
                    ficha = new Ficha(0,0,color,color+N-1,String.valueOf(id));
                    tablero[i][j] = ficha;
                }
                else{
                    if(i==0 && j!= N-1){
                        ficha = new Ficha(color-1,0,color,color+N-1,String.valueOf(id));
                        tablero[i][j] = ficha;
                    }
                    else{
                        if(i==0 && j==N-1){
                            ficha = new Ficha(color-1,0,0,color+N-1,String.valueOf(id));
                            tablero[i][j] = ficha;
                        }
                        else{
                            if(j==0 && i!=N-1){
                                color=tablero[i-1][N-1].getAbj()+1;
                                ficha = new Ficha(0,tablero[i-1][j].getAbj(),color,color+N-1,String.valueOf(id));
                                tablero[i][j] = ficha;
                            }
                            else{
                                if(j!=N-1 && i!=N-1){
                                    ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),color,color+N-1,String.valueOf(id));
                                    tablero[i][j] = ficha;
                                }
                                else{
                                    if(j==N-1 && i!=N-1){
                                        ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),0,color+N-1,String.valueOf(id));
                                        tablero[i][j] = ficha;
                                    }
                                    else{
                                        if(j==0 && i==N-1){
                                            color=tablero[i-1][N-1].getAbj()+1;
                                            ficha = new Ficha(0,tablero[i-1][j].getAbj(),color,0,String.valueOf(id));
                                            tablero[i][j] = ficha;
                                        }
                                        else{
                                            if(j!=N-1 && i==N-1){
                                                ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),color,0,String.valueOf(id));
                                                tablero[i][j] = ficha;
                                            }
                                            else{
                                                if(j==N-1 && i==N-1){
                                                    ficha = new Ficha(color-1,tablero[i-1][j].getAbj(),0,0,String.valueOf(id));
                                                    tablero[i][j] = ficha;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                id++;
                color++;
            }
        }
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                ficha =new Ficha(tablero[i][j].getIzq(), tablero[i][j].getArr(), tablero[i][j].getDer(), tablero[i][j].getAbj(), tablero[i][j].getId());
                fichas.add(ficha);
                ficha =new Ficha(tablero[i][j].getAbj(), tablero[i][j].getIzq(), tablero[i][j].getArr(), tablero[i][j].getDer(), tablero[i][j].getId());
                fichas.add(ficha);
                ficha =new Ficha(tablero[i][j].getDer(), tablero[i][j].getAbj(), tablero[i][j].getIzq(), tablero[i][j].getArr(), tablero[i][j].getId());
                fichas.add(ficha);
                ficha =new Ficha(tablero[i][j].getArr(), tablero[i][j].getDer(), tablero[i][j].getAbj(), tablero[i][j].getIzq(), tablero[i][j].getId());
                fichas.add(ficha);
            }
        }
    }

    /*private int N;
    private ArrayList<Ficha> fichas;

    private Boolean[] libres;
    private int TOTAL_FICHAS;/*

    /*
     * @param libres lista de libres
     * @return true si lista libres[i] == true para toda i
     */
    /*public Boolean todasUsadas(Boolean[] libres){
        for(int i=0; i<TOTAL_FICHAS; i++){
            if(libres[i])
                return false;
        }
        return true;
    }*/

    /*
     * @param pos: posicion a partir de la cual se debe empezar a buscar la siguiente ficha libre de la lista de fichas
     * @return
     * busca a partir de una posicion, si no encuentra, busca desde el comienzo
     */
    /*public Auxiliar getFichaLibre(Integer pos){
        for(int i=pos;i<TOTAL_FICHAS;i++){
            if(libres[i]){
                return new Auxiliar(fichas.get(i),i);
            }
        }
        for(Integer i=0;i<pos;i++){
            if(libres[i]){
                return new Auxiliar(fichas.get(i),i);
            }
        }
        return null;
    }*/

    /*cantidad de potenciales hijos de un nodo del arbol*/
    /*public Integer getCantidadLibres (Boolean[] libres){
        Integer res = 0;
        for(int i=0;i<libres.length;i++){
            if(libres[i])
                res++;
        }
        return res;
    }

    public String usadas (Boolean[] libres){
        String resultado = "";
        for (int i=0; i<TOTAL_FICHAS ; i++){
            if (!libres[i]){
                resultado += i+"-";
            }
        }
        return resultado;
    }

    public int totalUbicados(){
        return tablero.getTotalFichasPuestas();
    }

    public int totalUsados(){
        int t = 0;
        for (int i=0 ; i<TOTAL_FICHAS;i++){
            if(!libres[i])
                t+=1;
        }
        return t;

    }

    public Boolean detener(){
        return totalUbicados()!=totalUsados();
    }

    public void backtracking(Tablero tablero, int fila, int columna, Boolean[] libres, Integer ultimaUsada, Integer iteracion){

        Integer posibles = getCantidadLibres(libres);
        resultLog.info("*******");
        resultLog.info(tablero.imprimirse());
        if(detener()){
            resultLog.info("ACA SE DETUVO");
        }
        while (!todasUsadas(libres) && tablero.esSolucion() && posibles>0){
            resultLog.info("[" + fila + "," + columna + "]");
            Auxiliar aux = getFichaLibre(ultimaUsada);
            Ficha fichaUsada = aux.getF();
            resultLog.info("PONE EN [" + fila + "," + columna + "] <FICHA nro " + fichaUsada.getId() + ",pos en lista:" + ultimaUsada + ">: " + fichaUsada.imprimirse());
            if(!tablero.estaLibre(fila,columna)){
                resultLog.info("DETENER");
            }
            tablero.setPosicion(fila, columna, fichaUsada);

            libres[aux.getPosicionUsada()]=false;
            resultLog.info("fichas usadas (" + totalUsados() + ") : " + usadas(libres));
            if(columna<N-1){
                backtracking(tablero,fila,columna+1,libres, aux.getPosicionUsada(),iteracion+1);
            } else {
                backtracking(tablero, fila + 1, 0, libres, aux.getPosicionUsada(),iteracion+1);
            }
            posibles--;
            *//*o bien no es una solucion parcial o es una solucion parcial pero ademas quedan movimientos posibles
            esta situacion se da cuando se ingresa una ficha en una posicion que encaja pero impide que otras encajen, seria un nodo del arbol
            que aparentemente funciona pero necesitamos verificar todos sus hijos para evidenciar que no*//*
            if((!tablero.esSolucion() || tablero.esSolucion() && posibles>=0) &&(!tablero.esSolucionFinal())){
                tablero.liberarPosicion(fila, columna);
                libres[aux.getPosicionUsada()]=true;
                resultLog.info("SACA DE [" + fila + "," + columna + "] <FICHA nro " + fichaUsada.getId() + ",pos en lista:" + ultimaUsada + ">: " + fichaUsada.imprimirse());
                resultLog.info("fichas usadas (" + totalUsados() + ") : " + usadas(libres));
                if(aux.getPosicionUsada()<TOTAL_FICHAS-1){
                    ultimaUsada=aux.getPosicionUsada()+1;
                }else{
                    ultimaUsada=TOTAL_FICHAS;
                }
            }
        }
    }

    public void findSolution(){
        Integer fila = 0;
        Integer columna = 0;
        Integer ultimaUsada = 0;
        backtracking(tablero,fila,columna,libres,ultimaUsada,0);
        if(!tablero.esSolucionFinal()){
            resultLog.info("NO SE ENCONTRO SOLUCION");
        }else{
            resultLog.info("SOLUCION");
            resultLog.info(tablero.imprimirse());
        }
    }*/

}

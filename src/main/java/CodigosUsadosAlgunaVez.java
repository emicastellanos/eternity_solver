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

}

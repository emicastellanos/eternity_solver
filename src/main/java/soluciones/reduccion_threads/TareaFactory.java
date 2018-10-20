package soluciones.reduccion_threads;

public class TareaFactory implements TareaFactoryMethod {

    private String tipo;

    public TareaFactory(String tipo){
        this.tipo = tipo;
    }

    @Override
    public TareaAbs crearTarea(Estado estado, ManagerAbs m) {
        if ("BFS".equals(tipo)){
            return new TareaBFS(estado,m);
        }else if("DFS".equals(tipo)){
            return new TareaDFS(estado,m);
        }else return new TareaListaEspera(estado,m);
    }
}

package soluciones.reduccion_threads;

public class TareaFactory implements TareaFactoryMethod {

    private String tipo;

    public TareaFactory(String tipo){
        this.tipo = tipo;
    }

    @Override
    public TareaAbs crearTarea(Estado estado, Manager m) {
        if ("BFS".equals(tipo)){
            return new TareaBFS(estado,m);
        }else return new TareaDFS(estado,m);
    }
}

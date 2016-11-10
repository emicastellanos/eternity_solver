
public class Ficha {
    private int izq;
    private int arr;
    private int der;
    private int abj;
    private String id;

    public Ficha(int izq, int arr,int der, int abj,String id) {
        this.izq = izq;
        this.der = der;
        this.arr = arr;
        this.abj = abj;
        this.setId(String.format("%1$3s", id).replace(' ','0'));
    }

    public int getIzq() {
        return izq;
    }

    public void setIzq(int izq) {
        this.izq = izq;
    }

    public int getDer() {
        return der;
    }

    public void setDer(int der) {
        this.der = der;
    }

    public int getArr() {
        return arr;
    }

    public void setArr(int arr) {
        this.arr = arr;
    }

    public int getAbj() {
        return abj;
    }

    public void setAbj(int abj) {
        this.abj = abj;
    }

    public String imprimirse(){

        return izq+"-"+arr+"-"+der+"-"+abj;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

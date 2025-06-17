package Model;

public class Nota {

    private final int id;
    private final String valor;

    public Nota(int id, String valor) {
        this.id = id;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }
    public String getValor() {
        return valor;
    }

    public void setId(int id) {
    }
}

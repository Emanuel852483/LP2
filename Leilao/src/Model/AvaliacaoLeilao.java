package Model;

public class AvaliacaoLeilao {
    private static int proximoId = 1;

    private int id;
    private Cliente cliente;
    private Leilao leilao;
    private Nota nota;
    private String comentario;

    public AvaliacaoLeilao(Cliente cliente, Leilao leilao, Nota nota, String comentario) {
        this.id = proximoId++;
        this.cliente = cliente;
        this.leilao = leilao;
        this.nota = nota;
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getProximoId() {
        return proximoId;
    }

    public static void setProximoId(int proximoId) {
        AvaliacaoLeilao.proximoId = proximoId;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Leilao getLeilao() {
        return leilao;
    }

    public void setLeilao(Leilao leilao) {
        this.leilao = leilao;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
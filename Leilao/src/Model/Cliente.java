package Model;

public class Cliente {
    private String nome;
    private String email;
    private int idade; // para simplificar, usamos a idade diretamente

    public Cliente(String nome, String email, int idade) {
        this.nome = nome;
        this.email = email;
        this.idade = idade;
    }

    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public int getIdade() {
        return idade;
    }
}

package Model;

public class Cliente {
    private String nome;
    private String morada;
    private Localdate dataNascimento;
    private String email;
    private String password;

    //construtor
    public Cliente(String nome, String morada, LocalDate dataNascimento, String email, String password) {
        this.nome = nome;
        this.morada = morada;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.password = password;
    }

    //
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMorada() { return morada; }
    public void setMorada(String morada) { this.morada = morada; }

    public LocalDate getDataNascimento() { return this.dataNascimento; }
    public void setDataNascimento(Localdate dataNascimento) {this.dataNascimento = dataNascimento;}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}



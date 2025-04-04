package Model;

import java.time.LocalDate;

public class Cliente {
    private static int proximoId = 1;

    private int id;
    private String nome;
    private String morada;
    private LocalDate dataNascimento;
    private String email;
    private String password;
    private int lancesDisponiveis;
    private boolean isAdmin;

    // Construtor
    public Cliente(String nome, String morada, LocalDate dataNascimento, String email, String password, int lancesDisponiveis, boolean isAdmin) {
        this.id = proximoId++; // Atribui o próximo ID e incrementa o contador
        this.nome = nome;
        this.morada = morada;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.password = password;
        this.lancesDisponiveis = lancesDisponiveis;
        this.isAdmin = isAdmin;
    }

    // Getters e Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLancesDisponiveis() {
        return lancesDisponiveis;
    }

    public void setLancesDisponiveis(int lancesDisponiveis) {
        this.lancesDisponiveis = lancesDisponiveis;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}

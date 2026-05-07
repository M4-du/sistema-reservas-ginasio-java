package model;

public class Aula {
    private int id;
    private String nome;
    private String dataAula;
    private String horaAula;
    private int vagas;

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
    public String getDataAula() {
        return dataAula;
    }
    public void setDataAula(String dataAula) {
        this.dataAula = dataAula;
    }
    public String getHoraAula() {
        return horaAula;
    }
    public void setHoraAula(String horaAula) {
        this.horaAula = horaAula;
    }
    public int getVagas() {
        return vagas;
    }
    public void setVagas(int vagas) {
        this.vagas = vagas;
    }
}
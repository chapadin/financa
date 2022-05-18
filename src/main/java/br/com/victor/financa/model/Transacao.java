package br.com.victor.financa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Transacao implements Comparable<Transacao> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bancoOrigem;
    private String agenciaOrigem;
    private String contaOrigem;
    private String bancoDestino;
    private String agenciaDestino;
    private String contaDestino;
    private Double valorDaTransacao;
    private LocalDate dataDaTransacao;
    private LocalDateTime dataDeImportacao;
    @ManyToOne
    private Users user;

    public Transacao() {
    }

    public Transacao(String bancoOrigem, String agenciaOrigem, String contaOrigem, String bancoDestino,
            String agenciaDestino, String contaDestino, Double valorDaTransacao, LocalDate dataDaTransacao, LocalDateTime dataDeImportacao, Users user) {
        this.bancoOrigem = bancoOrigem;
        this.agenciaOrigem = agenciaOrigem;
        this.contaOrigem = contaOrigem;
        this.bancoDestino = bancoDestino;
        this.agenciaDestino = agenciaDestino;
        this.contaDestino = contaDestino;
        this.valorDaTransacao = valorDaTransacao;
        this.dataDaTransacao = dataDaTransacao;
        this.dataDeImportacao = dataDeImportacao;
        this.user = user;
    }

    public String getBancoOrigem() {
        return bancoOrigem;
    }

    public void setBancoOrigem(String bancoOrigem) {
        this.bancoOrigem = bancoOrigem;
    }

    public String getAgenciaOrigem() {
        return agenciaOrigem;
    }

    public void setAgenciaOrigem(String agenciaOrigem) {
        this.agenciaOrigem = agenciaOrigem;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getBancoDestino() {
        return bancoDestino;
    }

    public void setBancoDestino(String bancoDestino) {
        this.bancoDestino = bancoDestino;
    }

    public String getAgenciaDestino() {
        return agenciaDestino;
    }

    public void setAgenciaDestino(String agenciaDestino) {
        this.agenciaDestino = agenciaDestino;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public Double getValorDaTransacao() {
        return valorDaTransacao;
    }

    public void setValorDaTransacao(Double valorDaTransacao) {
        this.valorDaTransacao = valorDaTransacao;
    }

    public LocalDate getDataDaTransacao() {
        return dataDaTransacao;
    }

    public void setDataDaTransacao(LocalDate dataDaTransacao) {
        this.dataDaTransacao = dataDaTransacao;
    }

    public LocalDateTime getDataDeImportacao() {
        return dataDeImportacao;
    }

    public void setDataDeImportacao(LocalDateTime dataDeImportacao) {
        this.dataDeImportacao = dataDeImportacao;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "bancoOrigem='" + bancoOrigem + '\'' +
                ", agenciaOrigem=" + agenciaOrigem +
                ", contaOrigem='" + contaOrigem + '\'' +
                ", bancoDestino='" + bancoDestino + '\'' +
                ", agenciaDestino=" + agenciaDestino +
                ", contaDestino='" + contaDestino + '\'' +
                ", valorDaTransacao='" + valorDaTransacao + '\'' +
                ", dataDaTransacao=" + dataDaTransacao + '\'' +
                ", IdUser=" + user.getNome() +
                '}';
    }

    @Override
    public int compareTo(Transacao transacao) {
        return this.getDataDaTransacao().compareTo(transacao.dataDaTransacao);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

}

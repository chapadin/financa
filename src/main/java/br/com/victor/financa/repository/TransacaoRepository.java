package br.com.victor.financa.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.victor.financa.model.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findAll();

    @Query("SELECT t.dataDaTransacao FROM Transacao t")
    List<LocalDate> findByDatadeTransacao();

    @Query("SELECT t.dataDaTransacao FROM Transacao t WHERE t.dataDaTransacao = :data")
    Transacao findByData(LocalDate data);

    List<Transacao> findBydataDaTransacao(LocalDate data2);

    @Query("select e from Transacao e where month(e.dataDaTransacao) = ?1 AND year(e.dataDaTransacao) = ?2")
    List<Transacao> findByMonthAndYear(int mes, int ano);

    @Query("SELECT SUM(t.valorDaTransacao) FROM Transacao t WHERE (t.contaOrigem) = ?1 AND (t.agenciaOrigem) = ?2 AND month(t.dataDaTransacao) = ?3 AND year(t.dataDaTransacao) = ?4 AND (t.bancoOrigem) = ?5")
    Double somaConta(String contaOrigem, String agenciaOrigem, int mes, int ano, String banco);

    @Query("SELECT SUM(t.valorDaTransacao) FROM Transacao t WHERE (t.agenciaOrigem) = ?1 AND month(t.dataDaTransacao) = ?2 AND year(t.dataDaTransacao) = ?3 AND (t.bancoOrigem) = ?4")
    Double somaAgencia(String agenciaOrigem, int monthValue, int year, String bancoOrigem);
}

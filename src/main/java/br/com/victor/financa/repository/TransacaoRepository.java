package br.com.victor.financa.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.victor.financa.model.Transacao;

public interface TransacaoRepository  extends JpaRepository<Transacao, Long> {

    List<Transacao> findAll();

    @Query ("SELECT t.dataDaTransacao FROM Transacao t")
    List<LocalDate> findByDatadeTransacao();

    @Query ("SELECT t.dataDaTransacao FROM Transacao t WHERE t.dataDaTransacao = :data")
    Transacao findByData(LocalDate data);

    List<Transacao> findBydataDaTransacao(LocalDate data2);



}

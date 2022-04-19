package br.com.victor.financa.repository;

import br.com.victor.financa.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository  extends JpaRepository<Transacao, Long> {

    List<Transacao> findAll();

    @Query ("SELECT t.dataDaTransacao FROM Transacao t")
    List<LocalDate> findByDatadeTransacao();


}

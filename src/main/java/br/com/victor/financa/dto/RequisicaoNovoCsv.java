package br.com.victor.financa.dto;

import br.com.victor.financa.model.Transacao;
import com.opencsv.CSVReader;

import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequisicaoNovoCsv {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @NotBlank
    private String bancoOrigem;
    private String agenciaOrigem;
    private String contaOrigem;
    private String bancoDestino;
    private String agenciaDestino;
    private String contaDestino;
    private String valorDaTransacao;
    private String dataDaTransacao;

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

    public String getValorDaTransacao() {
        return valorDaTransacao;
    }

    public void setValorDaTransacao(String valorDaTransacao) {
        this.valorDaTransacao = valorDaTransacao;
    }

    public String getDataDaTransacao() {
        return dataDaTransacao;
    }

    public void setDataDaTransacao(String dataDaTransacao) {
        this.dataDaTransacao = dataDaTransacao;
    }



    public ArrayList<Transacao>  novoCsv(File nomeArquivo, String uploads, List<LocalDate> dataRepetida) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(uploads + nomeArquivo));
        ArrayList<Transacao> transacoes = new ArrayList<Transacao>();
        try {
            String[] arquivo = null;
            LocalDate dataCorreta = null;

            boolean informacaoCompleta = true;
            while ((arquivo = reader.readNext()) != null) {
                informacaoCompleta = true;
                Transacao transacao = new Transacao();
                transacao.setBancoOrigem(arquivo[0]);
                transacao.setAgenciaOrigem(arquivo[1]);
                transacao.setContaOrigem(arquivo[2]);
                transacao.setBancoDestino(arquivo[3]);
                transacao.setAgenciaDestino(arquivo[4]);
                transacao.setContaDestino(arquivo[5]);
                transacao.setValorDaTransacao(arquivo[6]);
                LocalDateTime original = LocalDateTime.parse(arquivo[7]);
                transacao.setDataDaTransacao(original.toLocalDate());
                transacao.setDataDeImportacao(LocalDateTime.now());
                LocalDate dataTrasacao = transacao.getDataDaTransacao();
                for (int i = 0; i < 7; i++) {
                    if (arquivo[i].isBlank()) {
                        informacaoCompleta = false;
                    }
                }
                if (informacaoCompleta == true) {
                    if (dataCorreta == null || dataCorreta.equals(dataTrasacao)) {
                        System.out.println("Data nula ou correta");
                        dataCorreta = dataTrasacao;
                        transacoes.add(transacao);
                    } else {
                        System.out.println("data Incorreta");
                    }
                }
            }
            try {
                Files.delete(Paths.get(uploads + nomeArquivo));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dataCorreta = null;
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("campo em branco");
        }

        reader.close();
        return transacoes;
    }

}

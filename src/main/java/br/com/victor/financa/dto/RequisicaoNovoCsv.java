package br.com.victor.financa.dto;

import br.com.victor.financa.model.Transacao;
import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import com.opencsv.CSVReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.PostConstruct;
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

@RequiredArgsConstructor
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



    @PostConstruct
    public ArrayList<Transacao>  novoCsv(File nomeArquivo, String uploads, List<LocalDate> dataRepetida, String email, Users user) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(uploads + nomeArquivo));
        ArrayList<Transacao> transacoes = new ArrayList<Transacao>();
        try {
            String[] arquivo = null;
            LocalDate dataCorreta = null;

            boolean informacaoCompleta = true;
            boolean dataJaInclusa = false;
            while ((arquivo = reader.readNext()) != null) {
                informacaoCompleta = true;
                dataJaInclusa = false;
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
                transacao.setUser(user);
                for (LocalDate dataNaoRepetida: dataRepetida) {
                    if (dataNaoRepetida.equals(dataTrasacao)){
                        System.out.println("data repetida");
                        dataJaInclusa = true;
                    }
                }
                for (int i = 0; i < 7; i++) {
                    if (arquivo[i].isBlank()) {
                        informacaoCompleta = false;
                    }
                }
                if (informacaoCompleta == true && dataJaInclusa == false) {
                    if (dataCorreta == null || dataCorreta.equals(dataTrasacao)) {
                        System.out.println("Data nula ou correta");
                        dataCorreta = dataTrasacao;
                        transacoes.add(transacao);
                    } else {
                        System.out.println("data Incorreta ou data repetida");
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
            System.out.println("Arquivo está vazio");
        }

        reader.close();
        return transacoes;
    }

}

package br.com.victor.financa.dto;

import br.com.victor.financa.model.Transacao;
import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.TransacaoRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RequisicaoNovoArquivo {

    @NotBlank
    private String bancoOrigem;
    private String agenciaOrigem;
    private String contaOrigem;
    private String bancoDestino;
    private String agenciaDestino;
    private String contaDestino;
    private Double valorDaTransacao;
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

    public Double getValorDaTransacao() {
        return valorDaTransacao;
    }

    public void setValorDaTransacao(Double valorDaTransacao) {
        this.valorDaTransacao = valorDaTransacao;
    }

    public String getDataDaTransacao() {
        return dataDaTransacao;
    }

    public void setDataDaTransacao(String dataDaTransacao) {
        this.dataDaTransacao = dataDaTransacao;
    }

    @PostConstruct
    public ArrayList<Transacao> novoCsv(File nomeArquivo, String uploads, List<LocalDate> dataRepetida, String email,
                                        Users user) throws IOException {
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
                transacao.setValorDaTransacao(Double.valueOf(arquivo[6]));
                LocalDateTime original = LocalDateTime.parse(arquivo[7]);
                transacao.setDataDaTransacao(original.toLocalDate());
                transacao.setDataDeImportacao(LocalDateTime.now());
                LocalDate dataTrasacao = transacao.getDataDaTransacao();
                transacao.setUser(user);
                for (LocalDate dataNaoRepetida : dataRepetida) {
                    if (dataNaoRepetida.equals(dataTrasacao)) {
                        System.out.println("data repetida");
                        dataJaInclusa = true;
                    }
                }
                for (int i = 0; i < 7; i++) {
                    if (arquivo[i].isBlank()) {
                        informacaoCompleta = false;
                    }
                }
                if (informacaoCompleta && !dataJaInclusa) {
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
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Arquivo está vazio");
        }

        reader.close();
        return transacoes;
    }

    @Autowired
    private Element element;

    public void novoXml(File nomeArquivo, String uploads, List<LocalDate> dataRepetida, String name, Users user, TransacaoRepository transacaoRepository) throws IOException {
        Document document;
        List<Transacao> transacoes = new ArrayList<Transacao>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(uploads + nomeArquivo);

            NodeList nodeListTransacoes = document.getElementsByTagName("transacoes");

            NodeList nodeList = document.getElementsByTagName("transacao");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == node.ELEMENT_NODE) {
                    Element element1 = (Element) node;

                    NodeList origemList = element1.getElementsByTagName("origem");

                    if (origemList.getLength() == 0)
                        System.out.println("O arquivo está vazio");

                    Node origemNode = origemList.item(0);
                    Element origemElement = (Element) origemNode;

                    if (origemElement.getElementsByTagName("banco").getLength() == 0)
                        System.out.println("Não tem banco de origem");

                    if (origemElement.getElementsByTagName("agencia").getLength() == 0)
                        System.out.println("Não tem agencia de origem");

                    if (origemElement.getElementsByTagName("conta").getLength() == 0)
                        System.out.println("Não tem conta de origem");

                    String bancoDeOrigem = origemElement.getElementsByTagName("banco").item(0).getTextContent();
                    String agenciaDeOrigem = origemElement.getElementsByTagName("agencia").item(0).getTextContent();
                    String contaDeOrigem = origemElement.getElementsByTagName("conta").item(0).getTextContent();

                    NodeList destinoList = element1.getElementsByTagName("destino");

                    if (destinoList.getLength() == 0)
                        System.out.println("O arquivo está vazio");

                    Node destinoNode = origemList.item(0);
                    Element destinoElement = (Element) destinoNode;

                    if (destinoElement.getElementsByTagName("banco").getLength() == 0)
                        System.out.println("Não tem banco de destino");

                    if (destinoElement.getElementsByTagName("agencia").getLength() == 0)
                        System.out.println("Não tem agencia de destino");

                    if (destinoElement.getElementsByTagName("conta").getLength() == 0)
                        System.out.println("Não tem conta de destino");

                    String bancoDeDestino = destinoElement.getElementsByTagName("banco").item(0).getTextContent();
                    String agenciaDeDestino = destinoElement.getElementsByTagName("agencia").item(0).getTextContent();
                    String contaDeDestino = destinoElement.getElementsByTagName("conta").item(0).getTextContent();

                    if (element1.getElementsByTagName("valor").getLength() == 0)
                        System.out.println("Não tem valor da transação");

                    if (element1.getElementsByTagName("data").getLength() == 0)
                        System.out.println("Não tem data da transação");

                    Double valor = Double.valueOf(element1.getElementsByTagName("valor").item(0).getTextContent());
                    LocalDateTime data = LocalDateTime.parse(element1.getElementsByTagName("data").item(0).getTextContent());
                    LocalDate dataParse = LocalDate.from(data);
                    LocalDateTime dataDeImportacao = LocalDateTime.now();

                    Transacao transacao = new Transacao(bancoDeOrigem, agenciaDeOrigem, contaDeOrigem, bancoDeDestino, agenciaDeDestino,
                            contaDeDestino, valor, dataParse, dataDeImportacao, user);
                    transacoes.add(transacao);
                }
            }
            LocalDate dataCorreta = transacoes.get(0).getDataDaTransacao();
            List<LocalDate> dataDaTransacao = transacaoRepository.findByDatadeTransacao();
            for (int i = 0; i < dataDaTransacao.size(); i++) {
                if (dataDaTransacao.get(i).isEqual(dataCorreta)){
                    System.out.println("Dia ja cadastrado");
                }
            }
            List<LocalDate> datasDiferentes = new ArrayList<LocalDate>();
            for (int i = 0; i < dataDaTransacao.size(); i++) {
                LocalDate data = dataDaTransacao.get(i);
                if (!datasDiferentes.contains(data)){
                    datasDiferentes.add(data);
                }
            }

            List<Integer> linhasComDatasDiferentes = new ArrayList<Integer>();
            int contadorDeLinhas = 0;
            for (int i = 0; i < transacoes.size(); i++) {
                contadorDeLinhas++;
                LocalDate dataComparada = transacoes.get(i).getDataDaTransacao();
                if (!dataCorreta.equals(dataComparada)) {
                    contadorDeLinhas++;
                    linhasComDatasDiferentes.add(contadorDeLinhas);
                    transacoes.remove(i);
                }
            }
            if (!linhasComDatasDiferentes.isEmpty()) {
                System.out.println("transação com datas diferentes");;
            }

            transacoes.forEach(transacaoRepository::save);

        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.delete(Paths.get(uploads + nomeArquivo));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

package br.com.victor.financa.controller;

import br.com.victor.financa.dto.RequisicaoNovoArquivo;
import br.com.victor.financa.model.Transacao;
import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.TransacaoRepository;
import br.com.victor.financa.repository.UsuarioRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class FormularioController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/formulario")
    public String formulario(Model model) {

        List<LocalDate> dataTransacao = transacaoRepository.findByDatadeTransacao();
        model.addAttribute("dataTransacao", dataTransacao);
        List<Transacao> dataConsulta = transacaoRepository.findAll();
        List<Transacao> dataCorreta = new ArrayList<>();
        List<LocalDate> datas = new ArrayList<>();
        for (int i = 0; i < dataConsulta.size(); i++) {
            LocalDate data = dataTransacao.get(i);
            if (!datas.contains(data)) {
                datas.add(data);
                dataCorreta.add(dataConsulta.get(i));
            }

        }
        Collections.sort(dataCorreta, Collections.reverseOrder());
        model.addAttribute("dataConsultas", dataCorreta);
        return "formulario";
    }

    @PostMapping("/formulario/upload")
    public String uploadFile(@RequestParam("arquivo") MultipartFile file, RedirectAttributes attributes,
                             RequisicaoNovoArquivo requisicao, Model model, Principal principal) throws IOException {

        String uploads = "/home/victor/Documentos/projetos/financa/src/main/resources/uploads/";
        File nomeArquivo = new File(file.getOriginalFilename()); // pega o nome do arquivo
        String extensao = FilenameUtils.getExtension(String.valueOf(nomeArquivo));
        System.out.println(extensao);
        if (extensao.equals("csv")) {
            if (file.isEmpty()) {
                attributes.addFlashAttribute("erroNoUpload", "Nenhum arquivo selecionado ou O arquivo está vazio.");
                System.out.println("Arquivo Vazio");
                return ("redirect:/formulario");
            }
            file.transferTo(new File(uploads + nomeArquivo)); // pega o tamanho do arquivo em Mb
            List<Transacao> dataValidacao = transacaoRepository.findAll();
            System.out.println(nomeArquivo); // escreve no console o nome do arquivo
            BigDecimal tamanhoDoArquivo = new BigDecimal(file.getSize()).divide(new BigDecimal("1000000"));
            System.out.println(tamanhoDoArquivo + " Mb"); // manda no console o tamanho do arquivo em Mb
            List<LocalDate> dataRepetida = transacaoRepository.findByDatadeTransacao().stream().distinct()
                    .collect(Collectors.toList());
            System.out.println(dataRepetida);
            System.out.println(principal.getName());
            Users user = usuarioRepository.findByEmail(principal.getName());
            ArrayList<Transacao> transcoes = requisicao.novoCsv(nomeArquivo, uploads, dataRepetida, principal.getName(),
                    user);
            transcoes.forEach(transacao -> transacaoRepository.save(transacao));
        } if (extensao.equals("xml")){
            if (file.isEmpty()) {
                attributes.addFlashAttribute("erroNoUpload", "Nenhum arquivo selecionado ou O arquivo está vazio.");
                System.out.println("Arquivo Vazio");
                return ("redirect:/formulario");
            }
            file.transferTo(new File(uploads + nomeArquivo));
            List<LocalDate> dataRepetida = transacaoRepository.findByDatadeTransacao().stream().distinct()
                    .collect(Collectors.toList());
            Users user = usuarioRepository.findByEmail((principal.getName()));
            requisicao.novoXml(nomeArquivo, uploads, dataRepetida, principal.getName(),user,transacaoRepository);
        }
        else {
            System.out.println(extensao);
        }
        return ("redirect:/formulario");
    }

    @PostMapping("/importacao/detalhar")
    public String detalhar(@RequestParam("data") String data, Model model) {
        LocalDate data2 = LocalDate.parse(data);
        List<Transacao> transacao = transacaoRepository.findBydataDaTransacao(data2);
        model.addAttribute("importacoes", transacao);
        ArrayList<Transacao> detalhes = new ArrayList<>();
        for (Transacao transacao2 : transacao) {
            if (transacao2.getId() != null) {
                detalhes.add(transacao2);
                model.addAttribute("detalhes", detalhes);
                break;
            }
        }

        return "/importacao/detalhar";
    }

    @GetMapping("/importacao/analise")
    public String analise() {
        return "/importacao/analise";

    }

    @PostMapping("/importacao/analisado")
    public String Analisado(@RequestParam("data") String data, Model model) {
        LocalDate data2 = LocalDate.parse(data);
        List<Transacao> transacao = transacaoRepository.findByMonthAndYear(data2.getMonthValue(), data2.getYear());
        ArrayList<Transacao> traSusp = new ArrayList<>();
        ArrayList<Transacao> contaSusp = new ArrayList<>();
        ArrayList<Transacao> ageSusp = new ArrayList<>();
        for (Transacao transacao2 : transacao) {
            if (transacao2.getValorDaTransacao() >= 100000) {
                traSusp.add(transacao2);
            }
        }
        for (Transacao transacao1 : transacao) {
            Double somaConta = transacaoRepository.somaConta(transacao1.getContaOrigem(),
                    transacao1.getAgenciaOrigem(), data2.getMonthValue(), data2.getYear(), transacao1.getBancoOrigem());

            if (somaConta > 1000000) {
                contaSusp.add(transacao1);
                model.addAttribute("contaSusp", contaSusp);
                model.addAttribute("somaConta", somaConta);
                model.addAttribute("saida", "Saída");
                model.addAttribute("entrada", "Entrada");

            }
        }
        for (Transacao transacao1 : transacao) {
            Double somaAgencia = Double.valueOf(transacaoRepository.somaAgencia(
                    transacao1.getAgenciaOrigem(), data2.getMonthValue(), data2.getYear(), transacao1.getBancoOrigem()));
            if (somaAgencia > 1000000000) {
                ageSusp.add(transacao1);
                model.addAttribute("ageSusp", ageSusp);
                model.addAttribute("somaAgencia", somaAgencia);
                model.addAttribute("saidaAge", "Saída");
                model.addAttribute("entradaAge", "Entrada");
            }
        }
        model.addAttribute("transacao", traSusp);
        System.out.println(transacao);
        return "/importacao/analise";
    }
}

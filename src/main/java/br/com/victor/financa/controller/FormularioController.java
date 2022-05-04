package br.com.victor.financa.controller;

import br.com.victor.financa.dto.RequisicaoNovoCsv;
import br.com.victor.financa.model.Transacao;
import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.TransacaoRepository;
import br.com.victor.financa.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("arquivo") MultipartFile file, RedirectAttributes attributes,
            RequisicaoNovoCsv requisicao, Model model, Principal principal) throws IOException {

        String uploads = "/home/victor/Documentos/projetos/financa/src/main/resources/uploads/";
        File nomeArquivo = new File(file.getOriginalFilename()); // pega o nome do arquivo
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

        return ("redirect:/formulario");
    }

    @PostMapping("/importacao/detalhar")
    public String detalhar(@RequestParam("data") String data, Model model) {
        LocalDate data2 = LocalDate.parse(data);
        List<Transacao> transacao = transacaoRepository.findBydataDaTransacao(data2);
        model.addAttribute("importacoes", transacao );
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
}

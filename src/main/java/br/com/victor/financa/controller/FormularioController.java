package br.com.victor.financa.controller;


import br.com.victor.financa.dto.RequisicaoNovoCsv;
import br.com.victor.financa.model.Transacao;
import br.com.victor.financa.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/formulario")
public class FormularioController {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @GetMapping
    public String formulario(Model model) {

        List<Transacao> dataContultas = transacaoRepository.findAll(Sort.by("dataDaTransacao"));
        model.addAttribute("dataContultas", dataContultas);


        return "formulario";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("arquivo") MultipartFile file, RedirectAttributes attributes, RequisicaoNovoCsv requisicao, Model model) throws IOException {

         String uploads = "/home/victor/Documentos/projetos/financa/src/main/resources/uploads/";
        File nomeArquivo = new File(file.getOriginalFilename()); //pega o nome do arquivo
        if (file.isEmpty()){
            attributes.addFlashAttribute("erroNoUpload", "Nenhum arquivo selecionado ou O arquivo está vazio.");
            System.out.println("Arquivo Vazio");
            return ("redirect:/formulario");
        }
        file.transferTo(new File(uploads + nomeArquivo)); //pega o tamanho do arquivo em Mb
        List<Transacao> dataValidacao = transacaoRepository.findAll();
        System.out.println(nomeArquivo); //escreve no console o nome do arquivo
        BigDecimal tamanhoDoArquivo = new BigDecimal(file.getSize()).divide(new BigDecimal("1000000"));
        System.out.println(tamanhoDoArquivo + " Mb"); //manda no console o tamanho do arquivo em Mb
        List<LocalDate> dataRepetida = transacaoRepository.findByDatadeTransacao().stream().distinct().collect(Collectors.toList());
        System.out.println(dataRepetida);
        ArrayList<Transacao> transcoes = requisicao.novoCsv(nomeArquivo, uploads, dataRepetida);
        transcoes.forEach(transacao -> transacaoRepository.save(transacao));


        return ("redirect:/formulario");
    }
}

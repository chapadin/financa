package br.com.victor.financa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.victor.financa.dto.CadastroService;
import br.com.victor.financa.dto.EditarCadastroService;
import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.UsuarioRepository;

@Controller
@RequestMapping ("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("listausuario")
    public String lista(Model model) {
        List<Users> usuarios = usuarioRepository.findByNomeNot("Admin");
        model.addAttribute("usuarios", usuarios);
        return "usuario/listausuario";
    }

    @GetMapping ("cadastro")
    public String cadastro() {
        return "usuario/cadastro";
    }

    @PostMapping ("novo")
    public String novo(CadastroService cadastro) {
        Users usuario = cadastro.novo();
        usuarioRepository.save(usuario);
        return "redirect:/usuario/listausuario";
        }

    @PostMapping ("/editar/{id}") 
    public String editar(EditarCadastroService cadastro, @RequestParam Long id) {
        
        Users usuario = cadastro.editar(id, usuarioRepository);
        usuarioRepository.save(usuario);
        return "redirect:/usuario/listausuario";
    }
     
    @PostMapping("delete/{id}")
    public String delete(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return "redirect:/usuario/listausuario";
    }
}

package br.com.victor.financa.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.victor.financa.dto.CadastroService;
import br.com.victor.financa.dto.EditarCadastroService;
import br.com.victor.financa.dto.EmailService;
import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.UsuarioRepository;

@Controller
@RequestMapping ("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JavaMailSender emailSender;

    @GetMapping("listausuario")
    public String lista(Model model) {
        List<Users> usuarios = usuarioRepository.findByNomeNotAndEnabled("Admin", true);
        model.addAttribute("usuarios", usuarios);
        return "usuario/listausuario";
    }

    @GetMapping ("cadastro")
    public String cadastro() {
        return "usuario/cadastro";
    }

    @PostMapping ("novo")
    public String novo(CadastroService cadastro) {
        EmailService emailService = new EmailService();
        Users usuario = cadastro.novo();
        emailService.sendSimpleMessage(emailSender, cadastro.getEmail(), cadastro.getSenha());
        usuarioRepository.save(usuario);        
        return "redirect:/usuario/listausuario";
        }

    @PostMapping ("editar") 
    public String editar(EditarCadastroService cadastro, @RequestParam("email") String email, Model model) {        
        Users usuario = usuarioRepository.findByEmail(email);
        model.addAttribute("usuario", usuario);
        return "usuario/editar";
    }    
    
    @PostMapping ("editado") 
    public String editado(EditarCadastroService cadastro, @RequestParam("email") String email) {        
        Users usuario = cadastro.editar(email, usuarioRepository);
        usuarioRepository.save(usuario);
        return "redirect:/usuario/listausuario";
    }
     
    @PostMapping("delete")
    public String delete(@RequestParam String email, Principal principal, Model model, RedirectAttributes attributes) {
        Users user = usuarioRepository.findByEmail(email);
        if (principal.getName().equals(email)) {
            System.out.println("Usuario logado, não pode excluir");
            attributes.addFlashAttribute("erroDeExclusao", "Usuario logado, não pode excluir");
        }else {            
            user.setEnabled(false);
            usuarioRepository.save(user);
            System.out.println(user.getEnabled());
        }

        return "redirect:/usuario/listausuario";
    }


}

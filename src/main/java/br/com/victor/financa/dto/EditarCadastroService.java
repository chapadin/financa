package br.com.victor.financa.dto;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.UsuarioRepository;

public class EditarCadastroService {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private String nome;
    private String senha;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Users editar(String email, UsuarioRepository usuarioRepository){
        Users usuario = usuarioRepository.findByEmail(email);
        usuario.setNome(nome);
        usuario.setPassword(encoder.encode(senha));
        return usuario;
    }

}

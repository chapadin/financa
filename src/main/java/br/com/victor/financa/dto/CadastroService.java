package br.com.victor.financa.dto;

import br.com.victor.financa.model.Authority;
import br.com.victor.financa.model.Users;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CadastroService {

    private String nome;
    private String email;
    private String senha;
    private Boolean enabled;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Users novo() {
        Users usuario = new Users();
        Authority authority = new Authority("USER");
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setEnabled(true);
        usuario.setSenha(encoder.encode(senha));
        usuario.addAuthority(authority);
        return usuario;
    }
}

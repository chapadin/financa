package br.com.victor.financa.dto;

import br.com.victor.financa.model.Users;
import br.com.victor.financa.repository.UsuarioRepository;

public class EditarCadastroService {

    private String nome;
    private String email;

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

    public Users editar(Long id, UsuarioRepository usuarioRepository){
        Users usuario = usuarioRepository.getById(id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        return usuario;
    }

}

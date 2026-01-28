package br.com.report_generator.infra.security;

import br.com.report_generator.model.Usuario;
import br.com.report_generator.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CadastraAdminStartupRunner implements CommandLineRunner {

    @Value("${sistema.admin.login}")
    private String login;

    @Value("${sistema.admin.senha}")
    private String senha;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome(login);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setSenha(passwordEncoder.encode(senha));

        usuario.setPermisao("ADMIN");

        this.usuarioRepository.save(usuario);
    }
}

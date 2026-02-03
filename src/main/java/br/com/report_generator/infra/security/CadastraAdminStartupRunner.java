package br.com.report_generator.infra.security;

import br.com.report_generator.model.Usuario;
import br.com.report_generator.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

        Optional<Usuario> usuarioOptional = this.usuarioRepository.findByNome(login);
        Usuario usuarioAdmin = null;

        if (usuarioOptional.isPresent()) {
            usuarioAdmin = usuarioOptional.get();
            usuarioAdmin.setSenha(new BCryptPasswordEncoder().encode(senha));

        } else {
            usuarioAdmin = new Usuario();
            usuarioAdmin.setNome(login);

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            usuarioAdmin.setSenha(passwordEncoder.encode(senha));

            usuarioAdmin.setPermisao("ADMIN");
        }
        this.usuarioRepository.save(usuarioAdmin);
    }
}

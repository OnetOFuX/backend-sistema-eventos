package pe.edu.upeu.eventos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.edu.upeu.eventos.entity.UsuarioEntity;
import pe.edu.upeu.eventos.repository.UsuarioRepository;

@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.nombre}")
    private String adminNombre;

    @Value("${app.admin.apellido}")
    private String adminApellido;

    @Value("${app.admin.codigo}")
    private String adminCodigo;

    @Override
    public void run(String... args) throws Exception {
        createAdminIfNotExists();
    }

    private void createAdminIfNotExists() {
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            UsuarioEntity admin = UsuarioEntity.builder()
                    .codigo(adminCodigo)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .nombre(adminNombre)
                    .apellido(adminApellido)
                    .telefono("999999999")
                    .carrera(null)  // Administrador no requiere carrera asignada
                    .ciclo("0")
                    .rol(UsuarioEntity.RolEnum.ADMINISTRADOR)
                    .activo(true)
                    .build();

            usuarioRepository.save(admin);
            logger.info("✅ Usuario administrador creado exitosamente");
            logger.info("📧 Email: {}", adminEmail);
            logger.info("🔑 Password: {}", adminPassword);
        } else {
            logger.info("ℹ️ Usuario administrador ya existe");
        }
    }
}

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.consultant")
@ComponentScan("com.consultant")
@EnableJpaRepositories
@SpringBootApplication
@EnableEncryptableProperties
public class ConsultancyManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultancyManagementApplication.class, args);
    }

}
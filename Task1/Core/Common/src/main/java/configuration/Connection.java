package configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Connection {
    @Value("${kafka.cluster.bootstrap-servers}")
    public String bootstrapServers;
}

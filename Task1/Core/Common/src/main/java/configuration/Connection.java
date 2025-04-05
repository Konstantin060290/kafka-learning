package configuration;

import org.springframework.beans.factory.annotation.Value;

public class Connection {
    @Value("${kafka.cluster.bootstrap-servers}")
    public String BootstrapServers;
}

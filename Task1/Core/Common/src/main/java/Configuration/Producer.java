package Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

public class Producer {
    public Integer maxBlocksMs;
    public Integer metaDataMaxAge;
    public String acks;
    public Integer retries;
    public Integer maxInFlightRequestsPerConnection;
    public Boolean enableIdempotence;
}

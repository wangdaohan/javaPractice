import com.example.disruptoromsgateway.GatewayConfig;
import com.client.checksum.CheckSum;
import com.client.codec.BodyCodec;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class GatewayStartup {
    public static void main(String[] args) throws Exception{
        String configFileName = "gateway.xml";
        GatewayConfig config = new GatewayConfig();
        config.initConfig(GatewayStartup.class.getResource("/").getPath() + configFileName);
        config.setCheckSum(new CheckSum());
        config.setBodyCodec(new BodyCodec());
        config.startup();
    }
}

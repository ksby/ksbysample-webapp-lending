package ksbysample.webapp.lending.config;

import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
import java.util.List;

/**
 * Redis Cluster 用 Configuration クラス
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterConfig {

    List<String> nodes;

    /**
     * Redis Cluster に接続するための {@link LettuceConnectionFactory} オブジェクトを生成する
     *
     * @return {@link LettuceConnectionFactory} オブジェクト
     */
    @Bean
    public RedisConnectionFactory connectionFactory() {
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(30))
                .enableAllAdaptiveRefreshTriggers()
                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .validateClusterNodeMembership(false)
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .build();
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(15))
                .shutdownTimeout(Duration.ZERO)
                .clientOptions(clusterClientOptions)
                .build();

        RedisClusterConfiguration serverConfig = new RedisClusterConfiguration(nodes);

        return new LettuceConnectionFactory(serverConfig, clientConfig);
    }

}

package ksbysample.webapp.lending.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

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
     * Redis Cluster に接続するための {@link JedisConnectionFactory} オブジェクトを生成する
     *
     * @return {@link JedisConnectionFactory} オブジェクト
     */
    @Bean
    public RedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory(new RedisClusterConfiguration(nodes));
    }

}

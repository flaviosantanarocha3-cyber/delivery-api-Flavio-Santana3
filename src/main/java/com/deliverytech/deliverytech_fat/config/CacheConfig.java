package com.deliverytech.deliverytech_fat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

/**
 * Configuração de cache da aplicação.
 * Armazena temporariamente dados em memória para melhorar o desempenho.
 */

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("produtos", "pedidos");
    }
}

/**
     * Cria o gerenciador de cache.
     * Os caches "produtos" e "pedidos" serão utilizados pela aplicação.
     */
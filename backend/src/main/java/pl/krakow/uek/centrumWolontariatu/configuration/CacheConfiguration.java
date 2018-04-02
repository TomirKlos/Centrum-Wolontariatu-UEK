package pl.krakow.uek.centrumWolontariatu.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@EnableCaching
@Configuration
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    private static EhCacheManagerFactoryBean cacheManagerfactoryBean;


    @Bean
    public CacheManager getEhCacheManager() {
        return  new EhCacheCacheManager(getEhCacheFactory().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean getEhCacheFactory() {
        cacheManagerfactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerfactoryBean.setShared(true);
        cacheManagerfactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return cacheManagerfactoryBean;
    }
}

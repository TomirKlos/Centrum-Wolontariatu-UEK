package pl.krakow.uek.centrumWolontariatu.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER_AD;
import static pl.krakow.uek.centrumWolontariatu.configuration.constant.UserConstant.UPLOADED_FOLDER_REQUESTS;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "file:" + UPLOADED_FOLDER_REQUESTS, "file:" + UPLOADED_FOLDER_AD};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
}

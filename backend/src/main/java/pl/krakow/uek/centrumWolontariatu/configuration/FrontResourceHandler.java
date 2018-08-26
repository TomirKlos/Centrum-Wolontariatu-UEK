package pl.krakow.uek.centrumWolontariatu.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class FrontResourceHandler implements WebMvcConfigurer {

    private static final String API = "/api";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/**/*.css", "/**/*.html", "/**/*.js", "/**/*.jsx", "/**/*.png", "/**/*.ttf", "/**/*.woff", "/**/*.woff2")
            .setCachePeriod(0)
            .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/", "/**")
            .setCachePeriod(0)
            .addResourceLocations("classpath:/static/index.html")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Resource getResource(String resourcePath, Resource location) {
                    if (resourcePath.startsWith(API) || resourcePath.startsWith(API.substring(1))) {
                        return null;
                    }

                    return location.exists() && location.isReadable() ? location : null;
                }
            });
    }

}

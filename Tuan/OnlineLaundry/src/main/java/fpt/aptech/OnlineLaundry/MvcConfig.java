package fpt.aptech.OnlineLaundry;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        Path servicesUploadDir = Paths.get("./images");
//        String servicesUploadPath = servicesUploadDir.toFile().getAbsolutePath();
//        registry.addResourceHandler("/images/**").addResourceLocations("file://"+servicesUploadPath+"/");
        registry.addResourceHandler("/images/**").addResourceLocations("file:/"+Paths.get("").toAbsolutePath().toString()+"/src/main/resources/static/images/");
    }
}

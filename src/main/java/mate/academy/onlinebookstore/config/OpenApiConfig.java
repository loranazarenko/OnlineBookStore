package mate.academy.onlinebookstore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String SECURITYSCHEMES = "BearerAuth";
    private static final String SCHEME = "bearer";
    private static final String BEARERFORMAT = "JWT";
    private static final String LISTAUTH = "BearerAuth";

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().components(new Components().addSecuritySchemes(SECURITYSCHEMES,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(SCHEME)
                                .bearerFormat(BEARERFORMAT)))
                .addSecurityItem(new SecurityRequirement()
                        .addList(LISTAUTH));
    }
}

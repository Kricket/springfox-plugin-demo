package com.ineat.springfoxplugindemo.config.swagger;

import com.ineat.springfoxplugindemo.dto.ErrorDTO;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER) // Required for Springfox plugins
public class ErrorOperationBuilder implements OperationBuilderPlugin {
    /**
     * Map of [method name => declared Exceptions]
     */
    private final Map<String, Class<?>[]> nameToExceptions = new HashMap<>();

    public ErrorOperationBuilder(List<RequestMappingInfoHandlerMapping> handlerMappings) {
        saveMethodExceptions(handlerMappings);
    }

    /**
     * Collect the declared Exceptions for each handler mapping
     */
    private void saveMethodExceptions(List<RequestMappingInfoHandlerMapping> handlerMappings) {
        handlerMappings.forEach(reqMapInfoHandMap ->
                reqMapInfoHandMap.getHandlerMethods().values().forEach(handlerMethod -> {
                    final Method method = handlerMethod.getMethod();
                    nameToExceptions.put(method.getName(), method.getExceptionTypes());
                })
        );
    }

    /**
     * Boilerplate method required by Springfox. Indicates if this plugin applies to the given
     * type of documentation (Swagger version).
     */
    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    /**
     * Run the plugin.
     */
    @Override
    public void apply(OperationContext context) {
        final Class<?>[] exceptions = nameToExceptions.get(context.getName());

        if(exceptions != null) {
            // Here, we add more responses (what would normally be done using the @ApiResponses annotation)
            final List<ExceptionErrorCode> errorCodes = Arrays
                    .stream(exceptions)
                    .map(ExceptionErrorCode::of)
                    .collect(Collectors.toList());
            context.operationBuilder().responseMessages(buildResponseMessage(errorCodes));
        }
    }

    /**
     * Build error responses for the given Exceptions.
     */
    private static Set<ResponseMessage> buildResponseMessage(List<ExceptionErrorCode> errorCodes) {
        Set<ResponseMessage> responses = new HashSet<>();

        // UNKNOWN errors are treated as Internal Server Errors. Since this is always possible,
        // we always add it as a possible result.
        responses.add(new ResponseMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected problem occurred. See the server logs for details.",
                new ModelRef(ErrorDTO.class.getSimpleName()),
                Collections.emptyMap(),
                Collections.emptyList()
        ));

        // This string is what will actually be displayed in the Swagger description.
        final String errors = errorCodes.stream()
                .filter(e -> e != ExceptionErrorCode.UNKNOWN)
                .map(Enum::name)
                .sorted()
                .collect(Collectors.joining("\n"));

        if(!StringUtils.isEmpty(errors)) {
            responses.add(new ResponseMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    "One of the following errors occurred:\n" + errors,
                    new ModelRef(ErrorDTO.class.getSimpleName()),
                    Collections.emptyMap(),
                    Collections.emptyList()));
        }

        return responses;
    }
}

package cz.muni.fi.obs.exceptions;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ValidationErrors(
        @Schema(description = "List of global errors", example = "[\"Invalid birth number for given nationality\"]")
        List<String> globalErrors,

        @Schema(description = "Map of field errors", example = "{\"firstName\":\"firstName is required\"}")
        Map<String, String> fieldErrors
) {
}

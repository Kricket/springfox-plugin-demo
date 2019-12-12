package com.ineat.springfoxplugindemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Information about an error returned from an API call")
public class ErrorDTO {
    public ErrorDTO() {}

    public ErrorDTO(final String code, final String description) {
        this.code = code;
        this.description = description;
    }

    @ApiModelProperty("A code that uniquely identifies the type of error")
    public String code;
    @ApiModelProperty("A technical description of the problem")
    public String description;
}

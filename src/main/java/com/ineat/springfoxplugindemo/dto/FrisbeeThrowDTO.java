package com.ineat.springfoxplugindemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Parameters of a single throw of a frisbee")
public class FrisbeeThrowDTO {
    @ApiModelProperty("The force (in Newtons) with which the frisbee was thrown")
    public double force;
    @ApiModelProperty("The angle-of-attack of the frisbee")
    public double angle;
    @ApiModelProperty("The distance the frisbee traveled before hitting the ground")
    public double distance;

    public FrisbeeThrowDTO(double force, double angle, double distance) {
        this.force = force;
        this.angle = angle;
        this.distance = distance;
    }
}

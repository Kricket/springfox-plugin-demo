package com.ineat.springfoxplugindemo.controller;

import com.ineat.springfoxplugindemo.dto.FrisbeeThrowDTO;
import com.ineat.springfoxplugindemo.exception.IllegalAngleException;
import com.ineat.springfoxplugindemo.exception.InsufficientForceException;
import com.ineat.springfoxplugindemo.service.FrisbeeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class FrisbeeController {
    private final FrisbeeService frisbeeService;

    public FrisbeeController(FrisbeeService frisbeeService) {
        this.frisbeeService = frisbeeService;
    }


    @ApiOperation("Calculate the distance (in meters) that a frisbee would travel")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Distance (in meters)")
            /*
            These are not needed: we generate them dynamically based on the declared Exceptions
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST,
                    message = "Not enough force was provided",
                    response = String.class),
            @ApiResponse(code = HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    message = "We don't know how to calculate with this angle",
                    response = String.class
             */
    })
    @GetMapping(value = "/frisbee/distance/{force}/{angle}")
    public FrisbeeThrowDTO getDistance(
            @ApiParam("The force (in Newtons) with which the disc was thrown")
            @PathVariable("force")
            final double force,
            @ApiParam("The angle-of-attack of the disc (in degrees)")
            @PathVariable("angle")
            final double angle
    ) throws InsufficientForceException, IllegalAngleException {
        final double distance;
        distance = frisbeeService.getDistance(force, angle);

        return new FrisbeeThrowDTO(force, angle, distance);
    }
}

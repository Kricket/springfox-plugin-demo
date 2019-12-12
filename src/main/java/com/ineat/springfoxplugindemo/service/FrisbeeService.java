package com.ineat.springfoxplugindemo.service;

import com.ineat.springfoxplugindemo.exception.IllegalAngleException;
import com.ineat.springfoxplugindemo.exception.InsufficientForceException;
import org.springframework.stereotype.Service;

@Service
public class FrisbeeService {
    private static final double MIN_FORCE = 1.0;
    private static final double MIN_ANGLE = -45.;
    private static final double MAX_ANGLE = 45.;

    /**
     * Get the distance that a frisbee would travel before hitting the ground
     * @param force Force (in Newtons) of the initial throw
     * @param angle Angle-of-attack (in degrees) at which the disc is thrown
     * @return The distance, in meters
     * @throws InsufficientForceException If the force is too low to calculate
     * @throws IllegalAngleException If the angle is outside of what we know how to calculate
     */
    public double getDistance(final double force, final double angle) throws InsufficientForceException, IllegalAngleException {
        if(force <= 0) {
            throw new InsufficientForceException("Force must be at least " + MIN_FORCE);
        }

        if(angle < MIN_ANGLE || angle > MAX_ANGLE) {
            throw new IllegalAngleException("Angle must be between " + MIN_ANGLE + " and " + MAX_ANGLE);
        }

        // The actual calculation is MUCH more complicated :)
        return force * 20 / Math.abs(angle);
    }
}

package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Simple exception with a message, that returns an Internal Server Error code.
 */
public class InternalServerErrorException extends AbstractThrowableProblem {

    public InternalServerErrorException(String message) {
        super(null, message, Status.INTERNAL_SERVER_ERROR);
    }
}

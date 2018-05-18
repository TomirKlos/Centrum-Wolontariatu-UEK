package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import org.zalando.problem.Status;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class RequestNotFoundException extends BadRequestAlertException {
    public RequestNotFoundException() {
        super(Status.NOT_FOUND, null, "Request not found", "requestManagement", "requestNotFound");
    }

}

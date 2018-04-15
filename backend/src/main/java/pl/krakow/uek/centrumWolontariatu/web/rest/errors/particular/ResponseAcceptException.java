package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class ResponseAcceptException extends BadRequestAlertException {
    public ResponseAcceptException() {
        super(null, "you are not allowed to accept if you are not owner of Volunteer Request", "ResponseVolunteerRequestManagement", "notallowedtoacceptresponsesforvolunteerrequest");
    }
}

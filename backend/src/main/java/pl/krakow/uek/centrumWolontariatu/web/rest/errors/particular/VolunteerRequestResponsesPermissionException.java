package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class VolunteerRequestResponsesPermissionException extends BadRequestAlertException {
    public VolunteerRequestResponsesPermissionException() {
        super(null, "you are not allowed to see responses if you are not owner of Volunteer Request", "ResponseVolunteerRequestManagement", "notallowedtoseeresponsesforvolunteerrequest");
    }
}

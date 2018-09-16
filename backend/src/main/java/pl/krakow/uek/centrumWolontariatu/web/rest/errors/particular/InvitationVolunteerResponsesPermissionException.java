package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class InvitationVolunteerResponsesPermissionException extends BadRequestAlertException {
    public InvitationVolunteerResponsesPermissionException() {
        super(null, "you are not allowed to see responses if you are not owner of Volunteer Ad", "InvitationVolunteerRequestManagement", "notallowedtoseeresponsesforvolunteerad");
    }
}

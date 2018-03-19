package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class EmailNotFoundException extends BadRequestAlertException {

    public EmailNotFoundException() {
        super(null, "Email address not registered", "userManagement", "emailnotfound");
    }
}

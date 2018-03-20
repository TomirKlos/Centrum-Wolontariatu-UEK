package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class EmailNotAllowedException extends BadRequestAlertException {

    public EmailNotAllowedException() {
        super(null, "Email address not allowed from this domain", "userManagement", "emailbaddomain");

    }
}



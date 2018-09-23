package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class EmailCouldNotBeSentException extends BadRequestAlertException {

    public EmailCouldNotBeSentException() {
        super(null, "Email could not be sent", "emailManagement", "emailcouldnotbesent");
    }
}


package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class UserNotFoundException extends BadRequestAlertException {

    public UserNotFoundException() {
        super(null, "User not found", "userManagement", "userNotFound");
    }
}

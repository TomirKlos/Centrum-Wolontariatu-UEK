package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;

public class EmailNotAllowedException extends BadRequestAlertException {

        public EmailNotAllowedException() {
            super(null, "Email address not allowed from this domain", "userManagement", "emailbaddomain");

        }
}



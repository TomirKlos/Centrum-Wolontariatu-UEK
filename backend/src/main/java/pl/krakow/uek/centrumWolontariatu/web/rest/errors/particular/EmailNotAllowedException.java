package pl.krakow.uek.centrumWolontariatu.web.rest.errors.particular;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EmailNotAllowedException extends AbstractThrowableProblem {

        public EmailNotAllowedException() {
            super(null, "Email address not allowed from this domain", Status.BAD_REQUEST);
        }
}



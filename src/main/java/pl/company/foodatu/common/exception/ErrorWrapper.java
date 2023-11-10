package pl.company.foodatu.common.exception;

import java.util.ArrayList;
import java.util.List;

public class ErrorWrapper {

    private List<Error> errors;

    public ErrorWrapper() {
    }

    public ErrorWrapper(List<Error> errors) {
        this.errors = errors;
    }

    public ErrorWrapper addError(Error error) {
        if(this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
        return this;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}

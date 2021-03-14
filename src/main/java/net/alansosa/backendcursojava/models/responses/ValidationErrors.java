package net.alansosa.backendcursojava.models.responses;

import java.util.Date;
import java.util.Map;

public class ValidationErrors {
    private Map<String, String> errors;
    private Date timeStamp;

    public ValidationErrors(Map<String, String> errors, Date timeStamp) {
        this.errors = errors;
        this.timeStamp = timeStamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}

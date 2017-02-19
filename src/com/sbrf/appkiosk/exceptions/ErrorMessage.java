package com.sbrf.appkiosk.exceptions;

import org.apache.commons.beanutils.BeanUtils;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The class <class>{@link ErrorMessage}</class> defines the body of th exceptions's properies.
 */
@XmlRootElement
public class ErrorMessage {

    /** contains the same HTTP Status code returned by the server */
    @XmlElement(name = "status")
    int status;

    /** application specific error code */
    @XmlElement(name = "code")
    int code;

    /** message describing the error*/
    @XmlElement(name = "message")
    String message;

    /** link point to page where the error message is documented */
    @XmlElement(name = "link")
    String link;

    /** extra information that might useful for developers */
    @XmlElement(name = "developerMessage")
    String developerMessage;

    public ErrorMessage(AppException ex){
        try {
            BeanUtils.copyProperties(this, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ErrorMessage() {

    }

    public ErrorMessage(NotFoundException ex) {
        this.status = Response.Status.NOT_FOUND.getStatusCode();
        this.message = ex.getMessage();
        this.link = "http://www.google.com";
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

package co.homecenter.etq.domain.model;

import co.homecenter.etq.domain.enums.DocumentStatus;

public class Document {

    private String documentType;
    private String documentNumber;
    private DocumentStatus status;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }
}

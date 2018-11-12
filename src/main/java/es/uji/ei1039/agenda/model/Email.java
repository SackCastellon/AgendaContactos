package es.uji.ei1039.agenda.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Email {

    private final StringProperty email = new SimpleStringProperty();

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(final String email) {
        this.email.set(email);
    }


    private final StringProperty label = new SimpleStringProperty();

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(final String label) {
        this.label.set(label);
    }
}

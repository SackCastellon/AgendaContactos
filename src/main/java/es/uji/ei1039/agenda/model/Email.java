package es.uji.ei1039.agenda.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;

public final class Email {

    private final @NotNull StringProperty email = new SimpleStringProperty();

    public String getEmail() {
        return email.get();
    }

    public @NotNull StringProperty emailProperty() {
        return email;
    }

    public void setEmail(final String email) {
        this.email.set(email);
    }


    private final @NotNull StringProperty label = new SimpleStringProperty();

    public String getLabel() {
        return label.get();
    }

    public @NotNull StringProperty labelProperty() {
        return label;
    }

    public void setLabel(final String label) {
        this.label.set(label);
    }
}

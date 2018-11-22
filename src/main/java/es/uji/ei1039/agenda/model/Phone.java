package es.uji.ei1039.agenda.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;

public final class Phone {

    private final @NotNull StringProperty phone = new SimpleStringProperty();

    public String getPhone() {
        return phone.get();
    }

    public @NotNull StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone.set(phone);
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

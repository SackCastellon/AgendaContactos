package es.uji.ei1039.agenda.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class Phone {

    private final StringProperty phone = new SimpleStringProperty();

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone.set(phone);
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

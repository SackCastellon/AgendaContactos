package es.uji.ei1039.agenda.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;

public final class Group {

    private final @NotNull StringProperty name = new SimpleStringProperty();

    public String getName() {
        return name.get();
    }

    public @NotNull StringProperty nameProperty() {
        return name;
    }

    public void setName(final String name) {
        this.name.set(name);
    }
}

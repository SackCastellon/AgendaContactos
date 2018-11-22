package es.uji.ei1039.agenda.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

public final class Contact {

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


    private final @NotNull StringProperty surname = new SimpleStringProperty();

    public String getSurname() {
        return surname.get();
    }

    public @NotNull StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname.set(surname);
    }


    private final @NotNull ListProperty<Phone> phones = new SimpleListProperty<>();

    public ObservableList<Phone> getPhones() {
        return phones.get();
    }

    public @NotNull ListProperty<Phone> phonesProperty() {
        return phones;
    }

    public void setPhones(final @NotNull ObservableList<Phone> phones) {
        this.phones.set(phones);
    }


    private final @NotNull ListProperty<Email> emails = new SimpleListProperty<>();

    public ObservableList<Email> getEmails() {
        return emails.get();
    }

    public @NotNull ListProperty<Email> emailsProperty() {
        return emails;
    }

    public void setEmails(final @NotNull ObservableList<Email> emails) {
        this.emails.set(emails);
    }


    private final @NotNull ListProperty<Group> groups = new SimpleListProperty<>();

    public ObservableList<Group> getGroups() {
        return groups.get();
    }

    public @NotNull ListProperty<Group> groupsProperty() {
        return groups;
    }

    public void setGroups(final @NotNull ObservableList<Group> groups) {
        this.groups.set(groups);
    }
}

package es.uji.ei1039.agenda.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public final class Contact {

    private final StringProperty name = new SimpleStringProperty();

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(final String name) {
        this.name.set(name);
    }


    private final StringProperty surname = new SimpleStringProperty();

    public String getSurname() {
        return surname.get();
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(final String surname) {
        this.surname.set(surname);
    }


    private final ListProperty<Phone> phones = new SimpleListProperty<>();

    public ObservableList<Phone> getPhones() {
        return phones.get();
    }

    public ListProperty<Phone> phonesProperty() {
        return phones;
    }

    public void setPhones(final ObservableList<Phone> phones) {
        this.phones.set(phones);
    }


    private final ListProperty<Email> emails = new SimpleListProperty<>();

    public ObservableList<Email> getEmails() {
        return emails.get();
    }

    public ListProperty<Email> emailsProperty() {
        return emails;
    }

    public void setEmails(final ObservableList<Email> emails) {
        this.emails.set(emails);
    }


    private final ListProperty<Group> groups = new SimpleListProperty<>();

    public ObservableList<Group> getGroups() {
        return groups.get();
    }

    public ListProperty<Group> groupsProperty() {
        return groups;
    }

    public void setGroups(final ObservableList<Group> groups) {
        this.groups.set(groups);
    }
}

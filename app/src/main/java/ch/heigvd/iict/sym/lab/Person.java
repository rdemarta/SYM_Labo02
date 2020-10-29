package ch.heigvd.iict.sym.lab;

import java.util.List;

public class Person {

    private String name;
    private String firstName;
    private String middleName;
    private String gender;
    private List<Phone> phones;

    public Person(String name, String firstName, String middleName, String gender, List<Phone> phones) {
        this.name = name;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.phones = phones;
    }

    public Person(String name, String firstName, String gender, List<Phone> phones) {
        this.name = name;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.phones = phones;
    }


    public void addPhone(Phone phone){
        phones.add(phone);
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getGender() {
        return gender;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
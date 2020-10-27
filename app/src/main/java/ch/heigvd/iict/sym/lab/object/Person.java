package ch.heigvd.iict.sym.lab.object;

public class Person {

    private String name;
    private String firstName;
    private String middleName;
    private String gender;
    private Phone phone;

    public Person(String name, String firstName, String middleName, String gender, Phone phone) {
        this.name = name;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.phone = phone;
    }
}

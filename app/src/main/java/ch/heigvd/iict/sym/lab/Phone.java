package ch.heigvd.iict.sym.lab;

import android.util.Xml;

public class Phone {

    private String phoneNumber;
    private PhoneType type;

    public Phone(String phoneNumber, PhoneType type) {
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public PhoneType getType() {
        return type;
    }


}

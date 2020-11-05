package ch.heigvd.iict.sym.lab;

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import ch.heigvd.iict.sym.lab.Person;
import ch.heigvd.iict.sym.lab.Phone;

public class Directory {

    private List<Person> people;



    public Directory() {
        this.people = new ArrayList<>();
    }




    public void addPerson(Person person) {
        people.add(person);
    }

    public boolean isEmpty(){
        return people.size() == 0;
    }

    public String xmlSerialize() {
        XmlSerializer serializer = Xml.newSerializer();

        StringWriter writer = new StringWriter();

        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", false);
            serializer.docdecl("directory SYSTEM \"http://sym.iict.ch/directory.dtd\"");

            // Root tag
            serializer.startTag("", "directory");

            for (Person p : people) {
                serializer.startTag("", "person");

                serializer.startTag("", "name");
                serializer.text(p.getName());
                serializer.endTag("", "name");

                serializer.startTag("", "firstname");
                serializer.text(p.getFirstName());
                serializer.endTag("", "firstname");

                // If the person doesn't have a middle name, don't add it
                if (p.getMiddleName() != null ) {
                    serializer.startTag("", "middlename");
                    serializer.text(p.getMiddleName());
                    serializer.endTag("", "middlename");
                }

                serializer.startTag("", "gender");
                serializer.text(p.getGender());
                serializer.endTag("", "gender");

                for (Phone phone : p.getPhones()) {
                    serializer.startTag("", "phone");
                    serializer.attribute("", "type", phone.getType().name());
                    serializer.text(phone.getPhoneNumber());
                    serializer.endTag("", "phone");
                }

                serializer.endTag("", "person");
            }

            serializer.endTag("", "directory");

            // Close the document
            serializer.endDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
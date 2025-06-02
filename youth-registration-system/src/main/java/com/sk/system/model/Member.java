package com.sk.system.model;

public class Member {
    private int id;
    private String name;
    private int age;
    private String address;
    private String contact;
    private String gender;
    private String birthdate;
    private String school;

    // Add group field to hold member's group info
    private Group group;

    public Member() {}

    public Member(int id, String name, int age, String address, String contact, String gender, String birthdate, String school) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
        this.contact = contact;
        this.gender = gender;
        this.birthdate = birthdate;
        this.school = school;
    }

    public Member(String name, int age, String address, String contact, String gender, String birthdate, String school) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.contact = contact;
        this.gender = gender;
        this.birthdate = birthdate;
        this.school = school;
    }

    // Getters and setters for existing fields

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    // New getter and setter for Group
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    // Existing stub methods you can remove or implement if needed
    public void setLastName(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFirstName(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

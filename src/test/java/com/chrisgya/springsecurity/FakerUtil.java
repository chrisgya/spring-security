package com.chrisgya.springsecurity;


import net.datafaker.Faker;

public class FakerUtil {
    private static final Faker faker = new Faker();

    public static String getFunnyName() {
        return faker.funnyName().name();
    }

    public static String getUsername() {
        return faker.name().username();
    }

    public static String getFirstName() {
        return faker.name().firstName();
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static String getPhoneNumber() {
        return "0808" + faker.number().digits(7);
    }

    public static String getEmailAddress() {
        return faker.internet().emailAddress();
    }

    public static String getShortSentence() {
        return faker.lorem().sentence(10);
    }

    public static int getShortRandomNumber() {
        return faker.number().numberBetween(1, 10);
    }

    public static String getUrl(){
        return "http://" + faker.internet().url();
    }

    public static String getAvatarUrl(){
        return faker.internet().avatar();
    }

}

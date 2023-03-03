package animal.shelter.animalsshelter.services;

public class StartMenu {
    public String hello;

    public String sayHello(){
        String hello = new String("Общаясь со мной, можно узнать:\n" +
                "Миссию и историю приюта\n" +
                "Наши ценности и принципы работы\n" +
                "Общую информацию для желающих взять питомца из приюта\n" +
                "Выбрать себе питомца\n" +
                "Получать поддержку и консультации волонтёров\n" +
                "Узнать больше о способах помощи приюту.\n\n" +
                "Давайте знакомиться!");
        return hello;
    }
}

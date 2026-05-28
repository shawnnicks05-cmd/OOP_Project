package GameEngine;

import Characters.*;

public class CharacterFactory {

    public static GameCharacter createCharacter(String characterName) {
        return switch(characterName.toLowerCase()) {
            case "shawn" -> new CharacterShawn();
            case "ethan" -> new CharacterEthan();
            case "dwight" -> new CharacterDwight();
            case "omar" -> new CharacterOmar();
            case "princess" -> new CharacterPrincess();
            case "Ian"     -> new CharacterIan();
            default -> null;
        };
    }
}

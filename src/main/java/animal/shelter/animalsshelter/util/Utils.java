package animal.shelter.animalsshelter.util;

import org.apache.commons.codec.language.bm.Rule;
import org.apache.commons.validator.routines.EmailValidator;

public class Utils {
    public static boolean isValidEmailAddress(String email) {
        return EmailValidator.getInstance().isValid(email);

    }
}

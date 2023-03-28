package animal.shelter.animalsshelter.service;

import java.nio.file.Path;

public interface FileService {

    Path getPhoto(byte[] array);

    Path getText(String s);
}

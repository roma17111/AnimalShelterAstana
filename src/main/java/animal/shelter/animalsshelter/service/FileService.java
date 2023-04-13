package animal.shelter.animalsshelter.service;

import animal.shelter.animalsshelter.model.Report;

import java.nio.file.Path;

public interface FileService {

    Path getPhoto(byte[] array);

    Path getText(Report report);

    void getPdfDocument();
}

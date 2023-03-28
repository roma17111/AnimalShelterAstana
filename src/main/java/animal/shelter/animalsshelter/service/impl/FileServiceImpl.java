package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.service.FileService;
import animal.shelter.animalsshelter.service.ReportService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@Log4j2
public class FileServiceImpl implements FileService {

    private final ReportService reportService;

    public FileServiceImpl(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public Path getPhoto(byte[] array) {
        List<Report> reportList = reportService.getAllReports();
        Path target = Paths.get("" + (Math.random() * 1000000) + "document.jpeg");
        try {
            Files.createFile(target);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        BufferedImage image = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(array);
            image = ImageIO.read(inputStream);
            ImageIO.write(image, "jpeg", target.toFile());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return target;
    }

    @Override
    public Path getText(String s) {
        List<Report> reportList = reportService.getAllReports();
        Path text = Paths.get("" + (Math.random() * 1000000) + "document.txt");
        try {
            Files.createFile(text);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try (Writer writer = Files.newBufferedWriter(text, StandardOpenOption.APPEND)) {
            writer.append(s);
            Files.readAllBytes(text.toAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return text;
    }
}

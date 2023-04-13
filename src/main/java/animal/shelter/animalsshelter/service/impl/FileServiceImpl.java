package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.service.FileService;
import animal.shelter.animalsshelter.service.ReportService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Path getText(Report report) {
        Path text = Paths.get("" + (Math.random() * 1000000) + "document.txt");
        try {
            Files.createFile(text);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try (Writer writer = Files.newBufferedWriter(text, StandardOpenOption.APPEND)) {
            writer.append(report.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return text;
    }

    @Override
    public void getPdfDocument() {
        Document document = new Document(PageSize.A4);
        String output = "document.pdf";
        FileOutputStream fos = null;
        Font myfont = FontFactory.getFont("DejaVuSans.ttf", "cp1251", BaseFont.EMBEDDED, 20);
        myfont.setStyle(Element.ALIGN_CENTER);
        myfont.setSize(20);
        try {
            fos = new FileOutputStream(output);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, fos);
        } catch (DocumentException e) {
            log.error(e.getMessage());
        }
        document.open();
        writer.open();

        List<Report> reports = reportService.getAllReports()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        for (Report report : reports) {
            try {
                Paragraph para = new Paragraph();
                para.setFont(myfont);
                Image image = Image.getInstance(report.getPhoto());
                image.scaleAbsolute(400, 300);
                image.setAlignment(Element.ALIGN_CENTER);
                para.setAlignment(Element.ALIGN_CENTER);
                para.add("\n\nОтчёт \n");
                para.add("Пользователь \n" +report.getUserInfo()+"\n");
                para.add("Дата \n" +report.getMsgDate()+"\n");
                para.add("Поведение питомца \n " +report.getBehaviorChange()+"\n");
                para.add("Диета \n" +report.getDiet()+"\n");
                para.add("Здоровье \n" +report.getGeneralHealth());
                document.add(para);
                document.add(image);
            } catch (DocumentException | IOException e) {
                log.error(e.getMessage());
            }
        }
        document.close();
        writer.close();
    }
}

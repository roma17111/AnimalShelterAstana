package animal.shelter.animalsshelter.service.impl;

import animal.shelter.animalsshelter.model.Report;
import animal.shelter.animalsshelter.service.FileService;
import animal.shelter.animalsshelter.service.ReportService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

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
        getPdfDocument();
        return text;
    }

    @Override
    public void getPdfDocument() {
        Document document = new Document();
        String output = "document.pdf";
        FileOutputStream fos = null;
        Font myfont = new Font();
        myfont.getBaseFont();
        myfont.setStyle(Font.ITALIC);
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
        Objects.requireNonNull(writer).open();
        document.open();
        writer.open();
        List<Report> reportList = reportService.getAllReports();
        for (Report report : reportList) {
            Paragraph para = new Paragraph();
            para.setFont(myfont);
            try {
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(
                                        new FileInputStream(getText(report).toFile()), "UTF8"));
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.replace("\u0020", "\u00A0");
                    para.add(line + "\n");
                    //document.add(new Paragraph(line).setFont(normal));
                }
                para.setAlignment(Element.ALIGN_CENTER);
                document.add(para);
                Image image = Image.getInstance(report.getPhoto());
                image.scaleAbsolute(400,400);
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
            } catch (DocumentException | IOException e) {
                log.error(e.getMessage());
            }
        }
        document.close();
        writer.close();
    }
}

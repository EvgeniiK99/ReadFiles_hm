package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.GlossaryModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFilesTests {

    @Test
    void testPdf() throws Exception {
        try (ZipFile zipFile = new ZipFile(new File("src/test/resources/zipFiles.zip"))) {
            ZipEntry pdfFile = zipFile.getEntry("junit-user-guide-5.9.3.pdf");
            if (pdfFile != null) {
                try (InputStream inputStreamPdf = zipFile.getInputStream(pdfFile)) {
                    PDF pdf = new PDF(inputStreamPdf);
                    Assertions.assertEquals("JUnit 5 User Guide", pdf.title);
                }
            } else {
                throw new FileNotFoundException("PDF file not found");
            }
        }
    }

    @Test
    void testCsv() throws Exception {
        try (ZipFile zipFile = new ZipFile(new File("src/test/resources/zipFiles.zip"))) {
            ZipEntry csvFile = zipFile.getEntry("pets.csv");
            if (csvFile != null) {
                try (InputStream inputStreamCsv = zipFile.getInputStream(csvFile);
                     Reader reader = new InputStreamReader(inputStreamCsv)) {
                    CSVReader csvReader = new CSVReader(reader);
                    List<String[]> content = csvReader.readAll();

                    final String[] getFirstRow = content.get(0);
                    final String[] getSecondRow = content.get(1);
                    final String[] getThirdRow = content.get(2);
                    final String[] getFourthRow = content.get(3);

                    Assertions.assertArrayEquals(new String[]{"Pet", "Pet's name", "Pet Age"}, getFirstRow);
                    Assertions.assertArrayEquals(new String[]{"Cat", "Vasilii", "3"}, getSecondRow);
                    Assertions.assertArrayEquals(new String[]{"Dog", "Evgen", "10"}, getThirdRow);
                    Assertions.assertArrayEquals(new String[]{"Turtle", "Zhora", "99"}, getFourthRow);
                }
            } else {
                throw new FileNotFoundException("CSV file not found");
            }
        }
    }

    @Test
    void testXls() throws Exception {
        try (ZipFile zipFile = new ZipFile(new File("src/test/resources/zipFiles.zip"))) {
            ZipEntry xlsFile = zipFile.getEntry("budget.xlsx");
            if(xlsFile != null) {
                try (InputStream inputStreamXls = zipFile.getInputStream(xlsFile)) {
                    XLS xls = new XLS(inputStreamXls);

                    Assertions.assertEquals("Остаток:", xls.excel.getSheetAt(0).getRow(1).getCell(4).getStringCellValue());
                    Assertions.assertEquals(50000, xls.excel.getSheetAt(0).getRow(2).getCell(4).getNumericCellValue());
                }
            } else {
                throw new FileNotFoundException("XLS file not found");
            }
        }
    }

    @Test
    void testJSON() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/test/resources/json.json");
        GlossaryModel glossaryModel = objectMapper.readValue(jsonFile, GlossaryModel.class);

        Assertions.assertEquals("9999f9b9-c9b1-4f72-855b-a8a4bf15b1a9", glossaryModel.getProjectId());
        Assertions.assertEquals("Test for QA.GURU", glossaryModel.getName());
        Assertions.assertEquals(5499, glossaryModel.getCode());

        Assertions.assertEquals("https://qa.guru/", glossaryModel.getLinks().get(0).getLink());
        Assertions.assertEquals("https://github.com/EvgeniiK99", glossaryModel.getLinks().get(1).getLink());

        Assertions.assertEquals("c45a9999", glossaryModel.getCreator().getId());
        Assertions.assertEquals("Evgenii", glossaryModel.getCreator().getName());
        Assertions.assertEquals("Klimashin", glossaryModel.getCreator().getSurname());
        Assertions.assertTrue(glossaryModel.getCreator().getHuman());

    }

}



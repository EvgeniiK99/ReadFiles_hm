package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.GlossaryModel;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipFilesTests {

    @Test
    void testPdf() throws Exception {
        try (ZipFile zipFile = new ZipFile(new File("src/test/resources/zipFiles.zip"))) {
            ZipEntry pdfFile = zipFile.getEntry("junit-user-guide-5.9.3.pdf");
            if (pdfFile != null) {
                try (InputStream inputStreamPdf = zipFile.getInputStream(pdfFile)) {
                    PDF pdf = new PDF(inputStreamPdf);
                    assertThat(pdf.title).isEqualTo("JUnit 5 User Guide");
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

                    assertThat(getFirstRow).containsExactly("Pet", "Pet's name", "Pet Age");
                    assertThat(getSecondRow).containsExactly("Cat", "Vasilii", "3");
                    assertThat(getThirdRow).containsExactly("Dog", "Evgen", "10");
                    assertThat(getFourthRow).containsExactly("Turtle", "Zhora", "99");
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
            if (xlsFile != null) {
                try (InputStream inputStreamXls = zipFile.getInputStream(xlsFile)) {
                    XLS xls = new XLS(inputStreamXls);

                    assertThat(xls.excel.getSheetAt(0).getRow(1).getCell(4).getStringCellValue()).isEqualTo("Остаток:");
                    assertThat(xls.excel.getSheetAt(0).getRow(2).getCell(4).getNumericCellValue()).isEqualTo(50000);
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

        assertThat(glossaryModel.getProjectId()).isEqualTo("9999f9b9-c9b1-4f72-855b-a8a4bf15b1a9");
        assertThat(glossaryModel.getName()).isEqualTo("Test for QA.GURU");
        assertThat(glossaryModel.getCode()).isEqualTo(5499);

        assertThat(glossaryModel.getLinks().get(0).getLink()).isEqualTo("https://qa.guru/");
        assertThat(glossaryModel.getLinks().get(1).getLink()).isEqualTo("https://github.com/EvgeniiK99");

        assertThat(glossaryModel.getCreator().getId()).isEqualTo("c45a9999");
        assertThat(glossaryModel.getCreator().getName()).isEqualTo("Evgenii");
        assertThat(glossaryModel.getCreator().getSurname()).isEqualTo("Klimashin");
        assertThat(glossaryModel.getCreator().getHuman()).isTrue();
    }

}



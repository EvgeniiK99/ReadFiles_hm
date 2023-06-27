package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFilesTests {
//    ClassLoader cl = ZipFilesTests.class.getClassLoader();


    @Test
    void testZip() throws Exception {
        try (ZipFile zipFile = new ZipFile(new File("src/test/resources/zipFiles.zip"))) {

            ZipEntry pdfFile = zipFile.getEntry("junit-user-guide-5.9.3.pdf");
            ZipEntry csvFile = zipFile.getEntry("pets.csv");
            ZipEntry xlsFile = zipFile.getEntry("budget.xlsx");

            try (InputStream inputStreamPdf = zipFile.getInputStream(pdfFile)) {
                PDF pdf = new PDF(inputStreamPdf);
                Assertions.assertEquals("JUnit 5 User Guide", pdf.title);
            }

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

            try (InputStream inputStreamXls = zipFile.getInputStream(xlsFile)) {
                XLS xls = new XLS(inputStreamXls);

                Assertions.assertEquals("Остаток:", xls.excel.getSheetAt(0).getRow(1).getCell(4).getStringCellValue());
                Assertions.assertEquals(50000, xls.excel.getSheetAt(0).getRow(2).getCell(4).getNumericCellValue());
            }
        }
    }
}


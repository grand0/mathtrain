package ru.zeroapps.mathtrain;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.StageStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.prefs.Preferences;

public class StatsWriter {
    final static private String SHEET_NAME = "Статистика";

    public static void writeStatisticsSafely(Statistics statistics, TasksStatistics tasksStatistics) {
        TasksStatistics tasksStatisticsCopy = tasksStatistics.createCopy();
        try {
            StatsWriter.addStatistics(statistics, tasksStatisticsCopy);
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE,
                    "error while writing statistics to file, maybe file is opened by other application?", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Не удалось записать статистику");
            alert.setContentText("Произошла ошибка во время записи статистики в файл. " +
                    "Возможно, файл открыт другой программой. Закройте её и попробуйте ещё раз.");
            alert.initStyle(StageStyle.UTILITY);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();
            Label label = new Label("Информация об ошибке:");
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            alert.getDialogPane().setExpandableContent(expContent);
            ButtonType repeatButtonType = new ButtonType("Повторить", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(repeatButtonType, cancelButtonType);
            Platform.runLater(() -> {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == repeatButtonType)
                    writeStatisticsSafely(statistics, tasksStatistics);
            });
        }
    }

    public static void addStatistics(Statistics stats, TasksStatistics taskStats) throws IOException {
        Preferences prefs = Preferences.userNodeForPackage(ru.zeroapps.mathtrain.Main.class);

        File file = new File(prefs.get(Defaults.PREFS_STATS_DIR_ENTRY.getValue(), Defaults.STATS_DIR_PATH.getValue()));
        file.mkdir();
        file = new File(prefs.get(Defaults.PREFS_STATS_DIR_ENTRY.getValue(), Defaults.STATS_DIR_PATH.getValue())
                + "\\" + prefs.get(Defaults.PREFS_STATS_FILE_ENTRY.getValue(), Defaults.STATS_FILE_NAME.getValue()) + ".xls");
        file.createNewFile();

        Workbook book;
        try {
            FileInputStream fis = new FileInputStream(file);
            book = new HSSFWorkbook(fis);
            fis.close();
        } catch (NotOLE2FileException e) {
            book = new HSSFWorkbook();
        }
        Sheet sheet = book.getSheet(SHEET_NAME);
        if (sheet == null) {
            sheet = book.createSheet(SHEET_NAME);
        }
        if (sheet.getLastRowNum() == -1) createHeader(sheet.createRow(0));
        Row row = sheet.createRow(sheet.getLastRowNum()+1);

        CellStyle mainCellStyle = book.createCellStyle();
        mainCellStyle.setBorderTop(BorderStyle.THIN);
        mainCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        mainCellStyle.setBorderRight(BorderStyle.THIN);
        mainCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        mainCellStyle.setBorderBottom(BorderStyle.THIN);
        mainCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        mainCellStyle.setBorderLeft(BorderStyle.THIN);
        mainCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle dashedCellStyle = book.createCellStyle();
        dashedCellStyle.setBorderTop(BorderStyle.DASHED);
        dashedCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        dashedCellStyle.setBorderRight(BorderStyle.DASHED);
        dashedCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        dashedCellStyle.setBorderBottom(BorderStyle.DASHED);
        dashedCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        dashedCellStyle.setBorderLeft(BorderStyle.DASHED);
        dashedCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        CellStyle correctCellStyle = book.createCellStyle();
        correctCellStyle.setBorderTop(BorderStyle.DASHED);
        correctCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        correctCellStyle.setBorderRight(BorderStyle.DASHED);
        correctCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        correctCellStyle.setBorderBottom(BorderStyle.DASHED);
        correctCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        correctCellStyle.setBorderLeft(BorderStyle.DASHED);
        correctCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        correctCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        correctCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        Font correctFont = book.createFont();
        correctFont.setColor(IndexedColors.GREEN.getIndex());
        correctCellStyle.setFont(correctFont);

        CellStyle skippedCellStyle = book.createCellStyle();
        skippedCellStyle.setBorderTop(BorderStyle.DASHED);
        skippedCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        skippedCellStyle.setBorderRight(BorderStyle.DASHED);
        skippedCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        skippedCellStyle.setBorderBottom(BorderStyle.DASHED);
        skippedCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        skippedCellStyle.setBorderLeft(BorderStyle.DASHED);
        skippedCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        skippedCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        skippedCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        Font skippedFont = book.createFont();
        skippedFont.setColor(IndexedColors.DARK_YELLOW.getIndex());
        skippedCellStyle.setFont(skippedFont);

        CellStyle wrongCellStyle = book.createCellStyle();
        wrongCellStyle.setBorderTop(BorderStyle.DASHED);
        wrongCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        wrongCellStyle.setBorderRight(BorderStyle.DASHED);
        wrongCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        wrongCellStyle.setBorderBottom(BorderStyle.DASHED);
        wrongCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        wrongCellStyle.setBorderLeft(BorderStyle.DASHED);
        wrongCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        wrongCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        wrongCellStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        Font wrongFont = book.createFont();
        wrongFont.setColor(IndexedColors.DARK_RED.getIndex());
        wrongCellStyle.setFont(wrongFont);

        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(stats.getName());
        cell.setCellStyle(mainCellStyle);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue(stats.getGrade().getLabel());
        cell.setCellStyle(mainCellStyle);
        cell = row.createCell(2, CellType.NUMERIC);
        cell.setCellValue(stats.getTarget() == 0 ? "---" : String.valueOf(stats.getTarget()));
        cell.setCellStyle(mainCellStyle);
        cell = row.createCell(3, CellType.NUMERIC);
        cell.setCellValue(stats.getCorrectAnswers());
        cell.setCellStyle(mainCellStyle);
        cell = row.createCell(4, CellType.NUMERIC);
        cell.setCellValue(stats.getWrongAnswers());
        cell.setCellStyle(mainCellStyle);
        cell = row.createCell(5, CellType.NUMERIC);
        cell.setCellValue(stats.getSkippedTasks());
        cell.setCellStyle(mainCellStyle);
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue(LocalTime.ofSecondOfDay(stats.getTimeSpentMillis()/1000).format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        cell.setCellStyle(mainCellStyle);
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM uuuu HH:mm:ss")));
        cell.setCellStyle(mainCellStyle);

        int startIndex = sheet.getLastRowNum() + 1;
        Row headerRow = sheet.createRow(sheet.getLastRowNum()+1);
        cell = headerRow.createCell(0, CellType.STRING);
        cell.setCellValue("Задание");
        cell.setCellStyle(dashedCellStyle);
        cell = headerRow.createCell(1, CellType.STRING);
        cell.setCellValue("Ответ ученика");
        cell.setCellStyle(dashedCellStyle);
        cell = headerRow.createCell(2, CellType.STRING);
        cell.setCellValue("Результат");
        cell.setCellStyle(dashedCellStyle);
        int endIndex = startIndex;
        while (taskStats.hasNextEntry()) {
            endIndex++;
            TaskResultEntry tre = taskStats.getNextEntry();
            CellStyle currentCellStyle;
            if (tre.getResult() == TaskResult.CORRECT) currentCellStyle = correctCellStyle;
            else if (tre.getResult() == TaskResult.SKIPPED) currentCellStyle = skippedCellStyle;
            else currentCellStyle = wrongCellStyle;
            row = sheet.createRow(sheet.getLastRowNum()+1);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(tre.getTask().getTask(true));
            cell.setCellStyle(currentCellStyle);
            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellValue(tre.getUserAnswer() == -1 ? "---" : String.valueOf(tre.getUserAnswer()));
            cell.setCellStyle(currentCellStyle);
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(tre.getResult().getLabel());
            cell.setCellStyle(currentCellStyle);
        }
        sheet.groupRow(startIndex, endIndex);
        sheet.setRowGroupCollapsed(startIndex, true);

        for (int i = 1; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        CellAddress firstCell = sheet.createRow(endIndex+1).createCell(0).getAddress();
        CellAddress lastCell = sheet.getRow(endIndex+1).createCell(7).getAddress();
        sheet.addMergedRegion(CellRangeAddress.valueOf(firstCell.formatAsString() + ":" + lastCell.formatAsString()));
        sheet.getRow(endIndex+1).getCell(0).setCellValue("←- Нажмите на \"+\" для подробной статистики.");

        FileOutputStream fos = new FileOutputStream(file);
        book.write(fos);
        fos.close();
        book.close();
    }

    private static void createHeader(Row row) {
        Font font = row.getSheet().getWorkbook().createFont();
        font.setBold(true);
        CellStyle cs = row.getSheet().getWorkbook().createCellStyle();
        cs.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setFont(font);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderRight(BorderStyle.THIN);
        cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());

        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Имя ученика");
        cell.setCellStyle(cs);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Класс");
        cell.setCellStyle(cs);
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Цель");
        cell.setCellStyle(cs);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Правильных ответов");
        cell.setCellStyle(cs);
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Неправильных ответов");
        cell.setCellStyle(cs);
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Пропущенных заданий");
        cell.setCellStyle(cs);
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Времени затрачено");
        cell.setCellStyle(cs);
        cell = row.createCell(7, CellType.STRING);
        cell.setCellValue("Дата");
        cell.setCellStyle(cs);
    }
}

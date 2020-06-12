package ru.delivery.system.common

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ExecutorService

class ExcelUtils {

//    fun writeToFile() {
//
//        val workbook = XSSFWorkbook()
//        val createHelper = workbook.getCreationHelper()
//
//        val sheet = workbook.createSheet("Customers")
//
//        val headerFont = workbook.createFont()
//        headerFont.bold = true
//        headerFont.color = IndexedColors.BLUE.getIndex()
//
//        val headerCellStyle = workbook.createCellStyle()
//        headerCellStyle.setFont(headerFont)
//
//        // Row for Header
//        val headerRow = sheet.createRow(0)
//
//        // Header
//        for (col in COLUMNs.indices) {
//            val cell = headerRow.createCell(col)
//            cell.setCellValue(COLUMNs[col])
//            cell.setCellStyle(headerCellStyle)
//        }
//
//        // CellStyle for Age
//        val ageCellStyle = workbook.createCellStyle()
//        ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"))
//
//        var rowIdx = 1
//        for (customer in customers) {
//            val row = sheet.createRow(rowIdx++)
//            row.createCell(0).setCellValue(customer.id)
//            row.createCell(1).setCellValue(customer.name)
//            row.createCell(2).setCellValue(customer.address)
//            val ageCell = row.createCell(3)
//            ageCell.setCellValue(customer.age.toDouble())
//            ageCell.setCellStyle(ageCellStyle)
//        }
//
//        val fileOut = FileOutputStream("customers.xlsx")
//        workbook.write(fileOut)
//        fileOut.close()
//        workbook.close()
//    }

    fun createPathListDoc() {

    }

    fun createTtnDoc() {
        val excelFile = FileInputStream(File("/home/akbar/code/java_kotlin/delivery-system/delivery-system-desktop/src/main/resources/documents/ttn.xls"))
        val workbook = HSSFWorkbook(excelFile)

        val sheet = workbook.getSheet("стр1")
        val rows = sheet.iterator()
        while (rows.hasNext()) {
            val currentRow = rows.next()
            val cellsInRow = currentRow.iterator()
            while (cellsInRow.hasNext()) {
                val currentCell = cellsInRow.next()

                // Грузоотправитель
                if (currentRow.rowNum == 9) {
                    if (currentCell.columnIndex == 2) {
                        currentCell.setCellValue("Адрес грузоотправителя")
                    }
                }

                if (currentCell.cellTypeEnum === CellType.STRING) {
                    print(currentCell.stringCellValue + " | ")
                } else if (currentCell.cellTypeEnum === CellType.NUMERIC) {
                    print(currentCell.numericCellValue.toString() + "(numeric)")
                }
            }
            println()
        }

//        val fileOut = FileOutputStream("customers.xls")
//        workbook.write(fileOut)
//        fileOut.close()
        workbook.close()
        excelFile.close()
    }
}

fun main() {
    ExcelUtils().createTtnDoc();
}

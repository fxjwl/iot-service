package com.data.util;

import com.data.exception.BadRequestException;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CoreExcelUtil {
    private static final Logger LOGGER = Logger.getLogger(CoreExcelUtil.class);
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DecimalFormat df = new DecimalFormat("0");

    public Workbook getWorkBook(InputStream fileInputStream, String suffixName) throws IOException {
        Workbook wb = null;
        if (".xls".equalsIgnoreCase(suffixName)) {
            wb = new HSSFWorkbook(fileInputStream);
        } else if (".xlsx".equalsIgnoreCase(suffixName)) {
            wb = new XSSFWorkbook(fileInputStream);
        }
        return wb;
    }

    public String getCellString(Row row, int i) {
        String value = "";
        try {
            Cell cell = row.getCell(i);
            LOGGER.info("rownum=[" + row.getRowNum() + "] i=[" + i + "]");
            if (cell == null) {
                return null;
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: // 数字
                    // 日期
                    value = df.format(cell.getNumericCellValue()) + "";
                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    value = cell.getStringCellValue();
                    break;
                case XSSFCell.CELL_TYPE_ERROR: //错误
                    value = String.valueOf(cell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA: // 公式
//                    value = String.valueOf(cell.getStringCellValue());
                    try {
                        value = String.valueOf(cell.getStringCellValue());
                    } catch (IllegalStateException e) {
                        value = String.valueOf(df.format(cell.getNumericCellValue()));
                    }
                    break;
                case XSSFCell.CELL_TYPE_BOOLEAN: //布尔型
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case XSSFCell.CELL_TYPE_BLANK: //空白
                    value = cell.getStringCellValue();
                    break;

                default:
                    value = null;
            }
        } catch (Exception e) {
            throw new BadRequestException("Excel行号[" +(row.getRowNum()+1)+ "],列名[" + CellReference.convertNumToColString(i) + "]出现解析异常:" + e.getMessage());
        }
        return value;
    }

    /**
     * 将有可能为纯数字的字符串单元格还原为字符串
     *
     * @param row
     * @param i
     * @return
     */
    public String getCellFormula(Row row, int i) {
        String value = "";
        try {
            Cell cell = row.getCell(i);
            LOGGER.info("rownum=[" + row.getRowNum() + "] i=[" + i + "]");
            if (cell == null) {
                return null;
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: // 数字
                case Cell.CELL_TYPE_FORMULA: // 公式
                    try {
                        value = new BigDecimal(cell.getNumericCellValue()).toPlainString();
                    } catch (IllegalStateException e) {
                        value = cell.getStringCellValue();
                    }
                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    value = cell.getStringCellValue();
                    break;
                default:
                    value = null;
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException(i + "");
        }
        return value;
    }

    public Timestamp getCellTimestamp(Row row, int i) {
        Timestamp value = null;
        try {
            Cell cell = row.getCell(i);
            LOGGER.info("rownum=[" + row.getRowNum() + "] i=[" + i + "]");
            if (cell == null) {
                return null;
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA: // 公式
                case Cell.CELL_TYPE_NUMERIC: // 数字
                    // 日期
                    double doubleVal = cell.getNumericCellValue();
                    Date date = DateUtil.getJavaDate(doubleVal);
                    value = new Timestamp(date.getTime());
                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    value = new Timestamp(dateFormat.parse(cell.getStringCellValue()).getTime());
                    break;
                default:
                    value = null;
            }
        } catch (NumberFormatException | ParseException e) {
            throw new BadRequestException(i + "");
        }
        return value;
    }

    public BigDecimal getCellBigDecimal(Row row, int i) {
        BigDecimal value = null;
        try {
            Cell cell = row.getCell(i);
            if (cell == null) {
                return null;
            }
            LOGGER.info("rownum=[" + row.getRowNum() + "] i=[" + i + "] cellType=[" + cell.getCellType() + "]");
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA: // 公式
                case Cell.CELL_TYPE_NUMERIC: // 数字
                    LOGGER.info("cell.getNumericCellValue() = " + cell.getNumericCellValue());
                    value = new BigDecimal(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    String cellValue = cell.getStringCellValue();
                    LOGGER.info("cell.getStringCellValue() = " + cellValue);
                    if (!StringUtils.isEmpty(cellValue)) {
                        value = new BigDecimal(cell.getStringCellValue());
                    }
                    break;
                default:
                    value = null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new BadRequestException(i + "");
        }
        return value;
    }

    public Integer getCellInteger(Row row, int i) {
        Integer value = null;
        try {
            Cell cell = row.getCell(i);
            LOGGER.info("rownum=[" + row.getRowNum() + "] i=[" + i + "]");
            if (cell == null) {
                return null;
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA: // 公式
                case Cell.CELL_TYPE_NUMERIC: // 数字
                    value = new Double(cell.getNumericCellValue()).intValue();
                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    value = Integer.parseInt(cell.getStringCellValue());
                    break;
                default:
                    value = null;
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException(i + "");
        }
        return value;
    }

    public Object getCellValue(Cell cell, SimpleDateFormat dateFormat) {
        Object value = null;
        try {
            if (cell == null) {
                return value;
            }
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA: // 公式
                case Cell.CELL_TYPE_NUMERIC: // 数字

                    try {
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            // 日期
                            double doubleVal = cell.getNumericCellValue();
                            Date date = DateUtil.getJavaDate(doubleVal);
                            value = new Timestamp(date.getTime());
                        } else {
                            // Shawn:
                            // 使用如下的formtter是因为poi默认数字的内容为double,会带上小数点。在某些情况这是不允许的，
                            // 譬如 2016 会转换为 2016.0
                            HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                            String cellFormatted = dataFormatter.formatCellValue(cell);
                            // 尝试将format过后的值转为number试试，如果不行，则表示读入的是 公式或其他的字符
                            // 需要重新将getNumericCellValue值返回出去。
                            try {
                                new Double(cellFormatted);
                                value = cellFormatted;
                            } catch (NumberFormatException e) {
                                value = cell.getNumericCellValue();
                            }
                        }
                    } catch (IllegalStateException e) {
                        LOGGER.error("当前的cell应该是numeric 但是取不出来值 [" + e.getMessage() + "]");
                        // 2017-03-20: 有可能数据的格式是有问题的，数字出错了，则尝试string
                        try {
                            value = cell.getStringCellValue().trim();
                        } catch (IllegalStateException e2) {
                            LOGGER.error("当前的cell应该是numeric 但是尝试String后也取不出来值 [" + e2.getMessage() + "]");
                            value = "";
                        }
                    }
                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    value = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK: // 空白
                    value = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN: // Boolean
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR: // Error，返回错误码
                    value = String.valueOf(cell.getErrorCellValue());
                    break;
                default:
                    value = "";
                    break;
            }
        } catch (Exception e) {
            throw new BadRequestException("rowindex=[" + cell.getRowIndex() + "] columnindex=[" + cell.getColumnIndex()
                    + "], 错误是" + e.getMessage());
        }
        return value;
    }

    /**
     * 创建 title.
     *
     * @param workbook
     * @param sheet
     * @param titles
     */
    public static Workbook createTitle(Workbook workbook, Sheet sheet, Map<Integer, String> titles) {
        Row row = sheet.createRow(0);
        // 创建格式
        Font font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(cellStyle);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        }
        return workbook;
    }

    /**
     * 创建 title.
     *
     * @param workbook
     * @param sheet
     * @param titles
     */
    public static void createTitle(Workbook workbook, Sheet sheet, List<String> titles) {
        Row row = sheet.createRow(0);
        // 创建格式
        Font font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        for (int i = 0; i < titles.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles.get(i));
            cell.setCellStyle(cellStyle);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        }
    }

    public static void output(HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response, String exportFileName) {
        OutputStream out = null;
        try {
            String filePath = request.getSession().getServletContext().getRealPath("/") + URLEncoder.encode(exportFileName, "utf-8");

            FileOutputStream fout = new FileOutputStream(filePath + ".xls");
            wb.write(fout);
            fout.close();

            File file = new File(filePath + ".xls");

            FileInputStream is = new FileInputStream(file);
            response.setCharacterEncoding("UTF-8");
            out = response.getOutputStream();
            response.setHeader("Content-disposition",
                    "attachment;filename=" + exportFileName + ".xls");

//            response.setContentType("application/ms-excel");
            response.setContentType("application/vnd.ms-excel");
//            response.setContentType("application/octet-stream");

//            response.setHeader("Content-disposition", "attachment;filename=" + exportFileName + ".xlsx");
//            response.setContentType("application/msexcel");
//            response.setHeader("content_Length", String.valueOf(file.length()));
//            response.setHeader("Content-type", "text/html;charset=UTF-8");
            int b = 0;
            byte[] buff = new byte[1024];
            while ((b = is.read(buff)) != -1) {
                out.write(buff, 0, b);
            }

            is.close();
            out.flush();

            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package br.com.report_generator.service.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class JasperUtil {

    public static byte[] compilaJRXML(byte[] bytesJRXML)  {

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    new ByteArrayInputStream(bytesJRXML)
            );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JRSaver.saveObject(jasperReport, baos);

            return baos.toByteArray();

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}

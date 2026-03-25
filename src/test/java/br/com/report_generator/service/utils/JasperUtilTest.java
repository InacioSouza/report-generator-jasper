package br.com.report_generator.service.utils;

import br.com.report_generator.infra.exception.FalhaCompilacaoJasperException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JasperUtilTest {

    @DisplayName("Teste Compila JRXML, Template Correto, Devolve Array De byte")
    @Test
    void testCompilaJRXML_TemplateCorreto_DevolveArrayDeByte() throws IOException {

        byte[] templateCorreto = new ClassPathResource(
                "templatesjasper/VEICULOS_SUBREPORT.jrxml")
                .getInputStream().readAllBytes();

        assertNotNull(JasperUtil.compilaJRXML(templateCorreto));
    }

    @DisplayName("Teste Compila JRXML, Template Com Erro De Sintaxe, Lanca: FalhaCompilacaoJasperException")
    @Test
    void testCompilaJrxml_TemplateComErroDeSintaxe_LancaFalhaCompilacaoJasperException() throws IOException {

        byte[] templateCorreto = new ClassPathResource(
                "templatesjasper/Template-Com-Erro-De-Sintaxe.jrxml")
                .getInputStream().readAllBytes();

        assertThrows(FalhaCompilacaoJasperException.class,() -> {
            JasperUtil.compilaJRXML(templateCorreto);
        });
    }
}
package br.com.report_generator.service.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilTest {

    @DisplayName("Teste Extrair Arquivos Do Zip, Zip Com 2 Arquivos, Devolve Map Com 2 Registros")
    @Test
    void testExtrairArquivosDoZip_ZipCom2Arquivos_DevolveMapCom2Registros() throws Exception {

        byte[] zipCom2Arquivos = new ClassPathResource(
                "arquivos/zips/zip-com-2-arquivos-jrxml.zip")
                .getInputStream().readAllBytes();

        Map<String, byte[]> mapArquivos =  ZipUtil.extrairArquivosDoZip(
                new MockMultipartFile (
                        "teste",
                        "teste",
                        "",
                        zipCom2Arquivos
                )
        );

        assertEquals(mapArquivos.size(), 2);
    }

    @DisplayName("Teste Gerar Zip, 2 Arquivos, Devolve Arquivo Zip")
    @Test
    void testGerarZip_2Arquivos_DevolveArquivoZip() throws Exception {
        Map<String, byte[]> mapArquivos = new HashMap<>();

        mapArquivos.put(
                "Arquivo1",
                new ClassPathResource(
                        "templatesjasper/relatorio-veiculos-pessoa-main.jrxml"
                ).getInputStream()
                        .readAllBytes()
        );

        mapArquivos.put(
                "Arquivo2",
                new ClassPathResource(
                        "templatesjasper/VEICULOS_SUBREPORT.jrxml"
                ).getInputStream()
                        .readAllBytes()
        );

        assertNotNull(ZipUtil.gerarZip(mapArquivos));
    }

    @DisplayName("Teste Gerar Zip, Map de Arquivos Vazio, Lança: IllegalArgumentException")
    @Test
    void testGerarZip_MapDeArquivosVazio_LançaIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> {
            ZipUtil.gerarZip(new HashMap<>());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            ZipUtil.gerarZip(null);
        });
    }

    @DisplayName("Teste Assinatura Do Arquivo Corresponde A Zip, Recebe Arquivo Zip, Devolve True")
    @Test
    void testAssinaturaDoArquivoCorrespondeZip_RecebeZip_DevolveTrue() throws Exception {

        byte[] arquivoZip = new ClassPathResource(
                "arquivos/zips/zip-com-2-arquivos-jrxml.zip")
                .getInputStream().readAllBytes();

        assertTrue(ZipUtil.assinaturaDoArquivoCorrespondeZIP(
                new MockMultipartFile(
                        "teste",
                        "teste",
                        "",
                        arquivoZip)
                )
        );
    }

    @DisplayName("Teste Assinatura Do Arquivo Não Corresponde A Zip, Recebe Arquivo Jrxml, Devolve False")
    @Test
    void testAssinaturaDoArquivoNaoCorrespondeAZipRecebeArquivoJrxmlDevolveFalse() throws Exception {

        byte[] arquivoZip = new ClassPathResource(
                "templatesjasper/VEICULOS_SUBREPORT.jrxml")
                .getInputStream().readAllBytes();

        assertFalse(ZipUtil.assinaturaDoArquivoCorrespondeZIP(
                new MockMultipartFile(
                        "teste",
                        "teste",
                        "",
                        arquivoZip)
                )
        );
    }
}
package br.com.report_generator.service.utils;

import br.com.report_generator.infra.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Testes de validação dos arquivos de template dos Relatórios")
class TrataArquivoServiceTest {

    private TrataArquivoService trataArquivoService;

    @BeforeEach
    void setup() {
        this.trataArquivoService = new TrataArquivoService();
    }

    @DisplayName("Teste Recebe MultipartFile, MultipartFile Vazio, Lanca Excecao: FormatoInvalidoException")
    @Test
    void testRecebeMultipartFile_MultipartFileVazio_LancaExcecaoFormatoInvalidoException() {
        assertThrows(ArquivoVazioException.class, () -> {
           this.trataArquivoService.validaEDevolveArquivosDoZip(new MockMultipartFile("teste", new byte[0]));
        });
    }

    @DisplayName("Teste Recebe MultipartFile, MultipartFile Sem nome, Lanca Excecao: NomenclaturaArquivoInvalidaException")
    @Test
    void testRecebeMultipartFile_SemNomeLanca_ExcecaoNomenclaturaArquivoInvalidaException() {
        assertThrows(NomenclaturaArquivoInvalidaException.class, () -> {
           this.trataArquivoService.validaEDevolveArquivosDoZip(new MockMultipartFile("teste", new byte[3]));
        });
    }

    @DisplayName("Teste Recebe MultipartFile, MultipartFile Com Arquivo Diferente De Zip, Lanca Excecao: FormatoInvalidoException")
    @Test
    void testRecebeMultipartFile_ComArquivoDiferenteDeZip_LancaExcecaoFormatoInvalidoException() throws Exception {

        assertThrows(FormatoInvalidoException.class, () -> {

            byte[] arquivoDiferenteDeZip = new ClassPathResource(
                    "templatesjasper/relatorio-veiculos-pessoa-main.jrxml")
                    .getInputStream().readAllBytes();

           this.trataArquivoService.validaEDevolveArquivosDoZip(
                   new MockMultipartFile(
                           "teste",
                           "teste",
                           "",
                           arquivoDiferenteDeZip)
           );
        });
    }


    @DisplayName("Teste Recebe MultipartFile Com Zip Vazio Lanca Excecao: ZipVazioException")
    @Test
    void testRecebeMultipartFile_ComZipVazio_LancaExcecaoZipVazioException()  {

        assertThrows(ZipVazioException.class, () -> {

            byte[] zipVazio = new ClassPathResource(
                    "arquivos/zips/zip-vazio.zip")
                    .getInputStream().readAllBytes();

            this.trataArquivoService.validaEDevolveArquivosDoZip(
                    new MockMultipartFile(
                            "teste",
                            "teste",
                            "",
                            zipVazio)
            );
        });
    }

    @DisplayName("Teste Recebe MultipartFile Com Zip Contendo 1 arquivo jrxml e 1 arquivo txt Lanca Excecao: FormatoInvalidoException")
    @Test
    void testRecebeMultipartFile_ComZipContendo1JrxmlE1Txt_LancaExcecaoFormatoInvalidoException()  {

        assertThrows(FormatoInvalidoException.class, () -> {

            byte[] zipCom1JrxmE1Txt = new ClassPathResource(
                    "arquivos/zips/zip-com-1-jrxm-e-1-txt.zip")
                    .getInputStream().readAllBytes();

            this.trataArquivoService.validaEDevolveArquivosDoZip(
                    new MockMultipartFile(
                            "teste",
                            "teste",
                            "",
                            zipCom1JrxmE1Txt)
            );
        });
    }

    @DisplayName("Teste Recebe MultipartFile Com Zip Contendo 1 arquivo jrxml Com mais de 1 caractere '.' Lança Exceção: NomenclaturaArquivoInvalidaException")
    @Test
    void testRecebeMultipartFile_ComZipContendo1ArquivoJrxmlComMaisDe1CaracterePonto_LançaNomenclaturaArquivoInvalidaException()  {

        assertThrows(NomenclaturaArquivoInvalidaException.class, () -> {

            byte[] zip = new ClassPathResource(
                    "arquivos/zips/zip-com-1-arquivo-com-mais-de-1-caractere-ponto.zip")
                    .getInputStream().readAllBytes();

            this.trataArquivoService.validaEDevolveArquivosDoZip(
                    new MockMultipartFile(
                            "teste",
                            "teste",
                            "",
                            zip)
            );
        });
    }

    @DisplayName("Teste Recebe MultipartFile Com mais de 1 arquivo do Zip Contendo 'MAIN' no nome, Lança Exceção: IdentificadorArquivoPrincipalInvalidoException")
    @Test
    void testRecebeMultipartFile_ComMaisDe1ArquivoDoZipContendoMainNoNome_LancaIdentificadorArquivoPrincipalInvalidoException()  {

        assertThrows(IdentificadorArquivoPrincipalInvalidoException.class, () -> {

            byte[] zip = new ClassPathResource(
                    "arquivos/zips/zip-com-com-mais-de-1-arquivo-contendo-palavra-main.zip")
                    .getInputStream().readAllBytes();

            this.trataArquivoService.validaEDevolveArquivosDoZip(
                    new MockMultipartFile(
                            "teste",
                            "teste",
                            "",
                            zip)
            );
        });
    }

}
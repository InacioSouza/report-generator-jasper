package br.com.report_generator.usecase;

import br.com.report_generator.contexto.IntegrationTestContext;
import br.com.report_generator.dto.relatorio.CadastraRelatorioRequestDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.junit.Assert.assertThrows;

@SpringBootTest
class CadastrarRelatorioUseCaseTest extends IntegrationTestContext {

    @Autowired
    private CadastrarRelatorioUseCase cadastrarRelatorioUseCase;

    @DisplayName("Teste Cadastrar Relatório Sistema Inexistente Lança RegistroNaoEncontradoException")
    @Test
    void testCadastrarRelatorio_SistemaInexistente_LancaRegistroNaoEncontradoException() {

        CadastraRelatorioRequestDto cadastraRelatorioDto = new CadastraRelatorioRequestDto(
                "",
                "",
                "",
                "",
                "",
                "",
                UUID.randomUUID()
        );

        assertThrows(RegistroNaoEncontradoException.class, () -> {
            this.cadastrarRelatorioUseCase.executar(
                    new MockMultipartFile(
                            "test",
                            "test",
                            "",
                            new byte[3]
                    ), cadastraRelatorioDto);
        });
    }
}
package br.com.report_generator.usecase;

import br.com.report_generator.dto.relatorio.BaixarRelatorioRequestDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.model.Relatorio;
import br.com.report_generator.service.api.RelatorioService;
import br.com.report_generator.service.api.VersaoRelatorioService;
import br.com.report_generator.usecase.relatorio.BaixarTemplateRelatorioUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaixarTemplateRelatorioUseCaseTest  {

    @Mock
    private RelatorioService relatorioService;

    @Mock
    private VersaoRelatorioService versaoRelatorioService;

    @InjectMocks
    private BaixarTemplateRelatorioUseCase baixarTemplateRelatorioUseCase;

    @DisplayName("Teste Baixa Template Relatório, Versão De Relatório Não Encontrada, Lança RegistroNaoEncontradoException")
    @Test
    void testBaixaTemplateRelatorio_VersaoDeRelatorioNaoEncontrada_LancaRegistroNaoEncontradoException() {

        when(relatorioService.findById(any(UUID.class)))
                .thenReturn(new Relatorio());

        doNothing().when(relatorioService)
                .verificaAutorizacaoClienteParaAlterarRelatorio(any());

        when(versaoRelatorioService
                .buscaVersaoRelatorioMaisRecentePara(any(UUID.class)))
                .thenReturn(null);

        BaixarRelatorioRequestDto dto = new BaixarRelatorioRequestDto(
                UUID.randomUUID(),
                null,
                null);


        assertThrows(RegistroNaoEncontradoException.class, () -> {
            this.baixarTemplateRelatorioUseCase.executar(
                    dto, new MockHttpServletResponse());
        });

        verify(versaoRelatorioService)
                .buscaVersaoRelatorioMaisRecentePara(any(UUID.class));
    }

    @DisplayName("Teste Baixa Template Relatório, Relatório Inexistente, Lança RegistroNaoEncontradoException")
    @Test
    void testBaixaTemplateRelatorio_RelatorioInexistente_LancaRegistroNaoEncontradoException() {

        BaixarRelatorioRequestDto dto = new BaixarRelatorioRequestDto(
                UUID.randomUUID(),
                null, null);

        assertThrows(RegistroNaoEncontradoException.class, () -> {
            this.baixarTemplateRelatorioUseCase.executar(
                    dto, new MockHttpServletResponse());
        });
    }

}
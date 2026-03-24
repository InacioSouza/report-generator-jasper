package br.com.report_generator.service;

import br.com.report_generator.dto.SistemaRequestDto;
import br.com.report_generator.infra.exception.RegistroNaoEncontradoException;
import br.com.report_generator.repository.SistemaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SistemaServiceImplTest {

    @Mock
    SistemaRepository sistemaRepository;

    @InjectMocks
    SistemaServiceImpl sistemaService;

    @DisplayName("Teste Atualiza Sistema, Sistema não encontrado, Lança exceção RegistroNaoEncontradoException")
    @Test
    void testAtualizaSistema_SistemaNaoEncontrado_LancaRegistroNaoEncontradoException() {

        when(this.sistemaRepository.existsById(any()))
                .thenReturn(false);

        assertThrows(RegistroNaoEncontradoException.class, () -> {
            this.sistemaService.atualiza(
                    UUID.randomUUID(),
                    new SistemaRequestDto("Vendas", "Vendas")
            );
        });

    }
}
package com.example.careplus.service;

import com.example.careplus.dto.dtoTratamento.TratamentoRequestDto;
import com.example.careplus.model.FichaClinica;
import com.example.careplus.model.Tratamento;
import com.example.careplus.repository.FichaClinicaRepository;
import com.example.careplus.repository.TratamentoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TratamentoServiceTest {

    @InjectMocks
    private TratamentoService tratamentoService;

    @Mock
    private TratamentoRepository tratamentoRepository;

    @Mock
    private FichaClinicaRepository fichaClinicaRepository;

    @Nested
    class CadastroTest {

        private final Long ID_FICHA_CLINICA = 1L;
        private final String TIPO_TRATAMENTO = "Linguagem Oral e Escrita";

        @Test
        void cadastrarTratamentoCasoFichaClinicaExista (){
            TratamentoRequestDto dto = new TratamentoRequestDto(TIPO_TRATAMENTO,  false, ID_FICHA_CLINICA);

            FichaClinica fichaClinicaEncontrada = new FichaClinica();
            fichaClinicaEncontrada.setId(ID_FICHA_CLINICA);

            Mockito.when(fichaClinicaRepository.findById(ID_FICHA_CLINICA))
                    .thenReturn(Optional.of(fichaClinicaEncontrada));

            Mockito.when(tratamentoRepository.save(Mockito.any(Tratamento.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Tratamento resultado = tratamentoService.cadastrar(dto);

            Assertions.assertNotNull(resultado, "O tratamento não deve ser nulo.");

            Assertions.assertEquals(TIPO_TRATAMENTO, resultado.getTipoDeTratamento(),
                    "O tipo de tratamento deve ser o mesmo do DTO.");
            Assertions.assertEquals(false, resultado.getFinalizado(),
                    "O status de finalizado deve ser o mesmo do DTO.");
            Assertions.assertEquals(fichaClinicaEncontrada, resultado.getFichaClinica(),
                    "A Ficha Clínica correta deve ser setada no Tratamento.");

            Mockito.verify(tratamentoRepository, Mockito.times(1)).save(Mockito.any(Tratamento.class));
            Mockito.verify(fichaClinicaRepository, Mockito.times(1)).findById(ID_FICHA_CLINICA);
        }

        @Test
        void cadastrarTratamentoCasoFichaClinicaNaoExista(){
            Long fichaClinicaInexistenteId = 99L;
            TratamentoRequestDto dto = new TratamentoRequestDto("Teste Falha", true, fichaClinicaInexistenteId);

            Mockito.when(fichaClinicaRepository.findById(fichaClinicaInexistenteId))
                    .thenReturn(Optional.empty());

            RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
                tratamentoService.cadastrar(dto);
            });

            Assertions.assertEquals("Ficha Clínica não encontrada", exception.getMessage(),
                    "A mensagem da exceção deve estar correta.");

            Mockito.verify(tratamentoRepository, Mockito.never()).save(Mockito.any());
            Mockito.verify(fichaClinicaRepository, Mockito.times(1)).findById(fichaClinicaInexistenteId);
        }
    }

    @Nested
    class BuscarPorNomeTest{

        private final String NOME_BUSCADO = "Linguagem Oral e Escrita";

        @Test
        void buscarPorNomeCasoExista(){
            Tratamento t1 = new Tratamento();
            t1.setTipoDeTratamento(NOME_BUSCADO);
            List<Tratamento> tratamentosEncontrados = List.of(t1);

            Mockito.when(tratamentoRepository.findByTipoDeTratamento(NOME_BUSCADO))
                    .thenReturn(tratamentosEncontrados);

            List<Tratamento> resultado = tratamentoService.buscarByNome(NOME_BUSCADO);

            Assertions.assertNotNull(resultado, "A lista não deve ser nula quando há resultados.");
            Assertions.assertEquals(1, resultado.size(), "Deve retornar o número correto de itens.");

            Mockito.verify(tratamentoRepository, Mockito.times(1)).findByTipoDeTratamento(NOME_BUSCADO);
        }

        @Test
        void buscarPorNomeCasoNãoExista(){
            List<Tratamento> listaVazia = List.of();

            Mockito.when(tratamentoRepository.findByTipoDeTratamento(NOME_BUSCADO))
                    .thenReturn(listaVazia);

            List<Tratamento> resultado = tratamentoService.buscarByNome(NOME_BUSCADO);

            Assertions.assertNull(resultado, "Deve retornar NULL quando a lista encontrada está vazia.");

            Mockito.verify(tratamentoRepository, Mockito.times(1)).findByTipoDeTratamento(NOME_BUSCADO);
        }
    }

    /*
    @Nested
    class buscarPeloIdProntuarioTest{

        private final Long ID_PRONTUARIO = 1L;

        @Test
        void buscarPeloIdProntuarioCasoExista(){

            final Long CONTAGEM_ESPERADA = 5L;

            Mockito.when(tratamentoRepository.buscarQuantidadeDeTratamentosPorId(ID_PRONTUARIO))
                    .thenReturn(CONTAGEM_ESPERADA);

            Long resultado = tratamentoService.buscarPeloIdProntuario(ID_PRONTUARIO);

            Assertions.assertEquals(CONTAGEM_ESPERADA, resultado,
                    "Deve retornar a contagem exata de tratamentos.");

            Mockito.verify(tratamentoRepository, Mockito.times(1)).buscarQuantidadeDeTratamentosPorId(ID_PRONTUARIO);
        }

        @Test
        void buscarPeloIdProntuarioCasoNaoExista(){
            final Long CONTAGEM_ZERO = 0L;
            Mockito.when(tratamentoRepository.buscarQuantidadeDeTratamentosPorId(ID_PRONTUARIO))
                    .thenReturn(CONTAGEM_ZERO);
            Caso eu mude para retornar null la da service. -> validar qual seria a melhor pratica.
           Mockito.when(tratamentoRepository.buscarQuantidadeDeTratamentosPorId(ID_PRONTUARIO)).thenReturn(null);


           Long resultado = tratamentoService.buscarPeloIdProntuario(ID_PRONTUARIO);

           Assertions.assertEquals(0L, resultado,
                    "Deve retornar 0L quando não houver tratamentos");

           Mockito.verify(tratamentoRepository, Mockito.times(1)).buscarQuantidadeDeTratamentosPorId(ID_PRONTUARIO);
        }
    }
    */

}
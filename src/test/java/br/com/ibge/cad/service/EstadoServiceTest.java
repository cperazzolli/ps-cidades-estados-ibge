package br.com.ibge.cad.service;

import br.com.ibge.cad.DataFaker;
import br.com.ibge.cad.MockSupportTest;
import br.com.ibge.cad.client.ClientError;
import br.com.ibge.cad.client.ClientResult;
import br.com.ibge.cad.client.estados.EstadosClient;
import br.com.ibge.cad.domain.EstadoDTO;
import br.com.ibge.cad.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EstadoServiceTest extends MockSupportTest {

    @Mock
    private EstadosClient estadosClient;

    @InjectMocks
    EstadosService subject;

    @Override
    protected List<Object> mocks() {
        return List.of(estadosClient);
    }

    @Test
    void givenValidCall_whenCallingIbgeClient_thenReturnsListOfStates() {
        var estados = DataFaker.getEstados(2);

        ClientResult<List<EstadoDTO>, ClientError> clientResult = ClientResult.ok(estados);

        when(estadosClient.find()).thenReturn(clientResult);

        var actual = subject.findEstados();


        assertEquals(estados.size(), actual.size());

        assertEquals(estados.get(0), actual.get(0));

        verify(estadosClient).find();
    }

    @Test
    void givenClientReturnsError_whenCallingIbgeClient_thenThrowsBusinessException() {
        var error = new ClientError("Erro no client", HttpStatus.INTERNAL_SERVER_ERROR);
        ClientResult<List<EstadoDTO>, ClientError> clientResult = ClientResult.fail(error);

        when(estadosClient.find()).thenReturn(clientResult);

        var ex = assertThrows(BusinessException.class, () -> subject.findEstados());
        assertTrue(ex.getMessage().contains("Erro ao buscar estados"));

        verify(estadosClient).find();
    }
}

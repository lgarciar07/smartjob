package com.bci.smartjob.service.impl;

import com.bci.smartjob.bean.out.LoginOut;
import com.bci.smartjob.model.Login;
import com.bci.smartjob.repository.LoginRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LoginServiceImplTest {

    @Mock
    private LoginRepository loginRepositoryMock;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsuario() {
        String username = "testUser";

        Login expectedLoginOut = new Login();
        expectedLoginOut.setUsuario(username);

        when(loginRepositoryMock.findByUsuarioAndEstadoTrue(username)).thenReturn(expectedLoginOut);

        LoginOut result = loginService.findByUsuario(username);

        LoginOut resultOut = new LoginOut();
        resultOut.setId(result.getId());
        resultOut.setUsuario(result.getUsuario());
        resultOut.setEstado(result.getEstado());
        resultOut.setPassword(result.getPassword());

        assertEquals(resultOut, result);
    }
}

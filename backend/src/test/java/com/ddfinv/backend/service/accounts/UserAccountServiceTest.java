package com.ddfinv.backend.service.accounts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ddfinv.backend.dto.DTOMapper;
import com.ddfinv.backend.dto.accounts.UserAccountDTO;
import com.ddfinv.backend.repository.ClientRepository;
import com.ddfinv.backend.repository.EmployeeRepository;
import com.ddfinv.backend.service.auth.AuthenticationService;
import com.ddfinv.core.domain.UserAccount;
import com.ddfinv.core.domain.enums.Role;
import com.ddfinv.core.repository.PermissionRepository;
import com.ddfinv.core.repository.UserAccountRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private DTOMapper dtoMapper;

    @Mock
    private PasswordEncoder pwEncoder;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ClientRepository clientRepository;
    
    @Mock
    private UserAccountRepository userAccountRepository;
    
    @BeforeEach
    void setUp() {
        userAccountService = new UserAccountService(userAccountRepository, permissionRepository, authenticationService, dtoMapper, pwEncoder, employeeRepository, clientRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testTest(){
        assertTrue(true, "Test always passes, is system configured correctly?");
    }

    // @Test
    // void testCreateUserAccount() {
    //     // test data
    //     String email = "test@example.com";
    //     String password = "Password123!";
    //     String firstName = "Test";
    //     String lastName = "User";
        
    //     // mock behavs
    //     UserAccount mockedUser = new UserAccount(email, password, firstName, lastName);
    //     when(pwEncoder.encode(any())).thenReturn("encoded_password");
    //     when(userAccountRepository.save(any(UserAccount.class))).thenReturn(mockedUser);
        
    //     // convert to dto
    //     UserAccountDTO dto = new UserAccountDTO();
    //     dto.setEmail(email);
    //     dto.setFirstName(firstName);
    //     dto.setPassword(password);
    //     dto.setLastName(lastName);
    //     dto.setRole(Role.guest);

    //     when(dtoMapper.toDTO(any(UserAccount.class))).thenReturn(dto);
    //     when(dtoMapper.toEntity(any(UserAccountDTO.class))).thenReturn(mockedUser);
    //     when(userAccountRepository.save(any(UserAccount.class))).thenReturn(mockedUser);


    //     // service method called
    //     UserAccount user = userAccountService.createNewUserAccount(email, password, firstName, lastName);
        
    //     // user created with correct props
    //     assertNotNull(user);
    //     assertEquals(email, user.getEmail());
    //     assertEquals(firstName, user.getFirstName());
    //     assertEquals(lastName, user.getLastName());
    //     assertEquals(Role.guest, user.getRole());
        
    //     // verify repository methods called
    //     verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    // }

    @Test
    void createUserAcount() {
    }

    @Test
    void createNewUserAccount() {
    }

    @Test
    void assignUserRole() {
    }

    @Test
    void modifyUserAccountRole() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void saveUserAccount() {
    }

    @Test
    void updateUserAccount() {
    }

    @Test
    void getAllUserAccounts() {
    }

    @Test
    void updateUserAccountPassword() {
    }

    @Test
    void resetUserAccountPassword() {
    }

    @Test
    void deleteUserAccount() {
    }

    @Test
    void changeUserRole() {
    }
}
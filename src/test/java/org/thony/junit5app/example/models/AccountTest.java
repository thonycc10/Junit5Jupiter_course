package org.thony.junit5app.example.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.thony.junit5app.example.exceptions.InsufficientFunds;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


class AccountTest {
    Account account;

    @BeforeEach
    void beforeEach() {
        System.out.println("Iniciando el metodo de prueba.");
        this.account = new Account("Thony", new BigDecimal("1000.12345"));
    }

    @AfterEach
    void afterEach() {
        System.out.println("Finalizo el metodo de prueba.");
    }

    @Nested
    @DisplayName("probando atributos de la cuenta corriente")
    class accountBank {
        @Test
        @DisplayName("el nombre de la cuenta corriente")
        void NameAccountTest() {
            // izd: esperado - der: real
            assertEquals("Thony", account.getPerson());
            assertTrue(account.getPerson().equals("Thony"));
        }

        @Test
        @DisplayName("credito de la cuenta")
        void CreditAccountTest() {
            assertEquals(1000.12345, account.getCredit().doubleValue());
            assertFalse(account.getCredit().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(account.getCredit().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("igualdad de dos cuentas")
        void ReferenceAccountTest() {
            Account account1 = new Account("Thony", new BigDecimal("8900.9997"));
            Account account2 = new Account("Thony", new BigDecimal("8900.9997"));

            assertEquals(account1, account2);
        }

        @Test
        @DisplayName("igualdad de dos cuentas")
        void DebitAccountTest() {
            account.debit(new BigDecimal(100));
            assertNotNull(account.getCredit());
            assertEquals(900, account.getCredit().intValue());
            assertEquals("900.12345", account.getCredit().toPlainString());
        }

        @Test
        @DisplayName("Cuenta de credito")
        void CredtAccountTest() {
            account.credit(new BigDecimal(100));
            assertNotNull(account.getCredit());
            assertEquals(1100, account.getCredit().intValue());
            assertEquals("1100.12345", account.getCredit().toPlainString());
        }

        @Test
        @DisplayName("Validar error insuficiente monto de la cuenta")
        void InsufficientFundsAccountExceptionTest() {
            Exception insufficientFunds = assertThrows(InsufficientFunds.class, () -> {
                account.debit(new BigDecimal(1500));
            });

            String actual = insufficientFunds.getMessage();
            String expected = "Insufficient Funds";
            assertEquals(expected, actual);
        }

        @Test
        @Disabled
        @DisplayName("Transferencia de dinero a otra cuenta")
        void TransferFoundsAccountTest() {
            fail();
            Account account1 = new Account("Thony 1", new BigDecimal("2500"));
            Account account2 = new Account("Thony 2", new BigDecimal("1500.8989"));

            Bank bank = new Bank();
            bank.setName("Banco del estado");
            bank.transfer(account2, account1, new BigDecimal(500));
            assertEquals("1000.8989", account2.getCredit().toPlainString());
            assertEquals("3000", account1.getCredit().toPlainString());
        }

        @Test
        @DisplayName("Añadir cuentas al banco")
        void BankAndAccountTest() {
            Account account1 = new Account("Thony 1", new BigDecimal("2500"));
            Account account2 = new Account("Thony 2", new BigDecimal("1500.8989"));

            Bank bank = new Bank();
            bank.setName("Banco del estado");

            bank.addAccount(account1);
            bank.addAccount(account2);

            bank.transfer(account2, account1, new BigDecimal(500));

            assertAll(() -> assertEquals("1000.8989", account2.getCredit().toPlainString(),
                            () -> "El valor de credito de la cuenta2 no es el esperado."),
                    () -> assertEquals("3000", account1.getCredit().toPlainString(),
                            () -> "El valor de credito de la cuenta 1 no es el esperado."),
                    () -> assertEquals(2, bank.getAccounts().size(),
                            () -> "El total de cuentas asociados al banco no es el esperado."),
                    () -> assertEquals("Banco del estado", account1.getBank().getName(),
                            () -> "La cuenta1 no esta asociado al banco esperado."),
                    () -> assertEquals("Thony 2", bank.getAccounts()
                                    .stream()
                                    .filter(account -> account.getPerson().equals("Thony 2"))
                                    .findFirst().get().getPerson(),
                            () -> "La cuenta no tiene el nombre de la persona esperado"),
                    () -> assertTrue(bank.getAccounts()
                                    .stream()
                                    .anyMatch(account -> account.getPerson().equals("Thony 1")),
                            () -> "No se encontro la cuenta con el nombre Thony 1.")
            );
        }
    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void enviromentDevTest() {

    }

    @Test
    void imprSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "dev")
    void envDevTest() {
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "prod")
    void envProdTest() {
    }

    /* CLASES anidadas usar anotacion @Nested*/
    @Nested
    class variablesEnviromentTest {
        /* USE ASSUMING */
        @Test
        @DisplayName("CreditAccountAssuming1Test")
        void CreditAccountAssuming1Test() {
            boolean isDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(isDev);
            assertFalse(account.getCredit().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(account.getCredit().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("CreditAccountAssuming2Test")
        void CreditAccountAssuming2Test() {
            boolean isDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(isDev, () -> { // validacion por ambiente de pruebas
                assertNotNull(account.getCredit());
                assertEquals(1000.12345, account.getCredit().doubleValue());
            });
            assertFalse(account.getCredit().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(account.getCredit().compareTo(BigDecimal.ZERO) > 0);
        }
    }


}
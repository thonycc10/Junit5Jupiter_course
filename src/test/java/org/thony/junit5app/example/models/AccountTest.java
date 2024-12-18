package org.thony.junit5app.example.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.thony.junit5app.example.exceptions.InsufficientFunds;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


class AccountTest {
    Account account;
    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void beforeEach(TestInfo testInfo, TestReporter testReporter) {
        this.account = new Account("Thony", new BigDecimal("1000.12345"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("Iniciando el metodo de prueba.");
        testReporter.publishEntry(" Ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName()
        + " con las etiquetas " + testInfo.getTags());
    }

    @AfterEach
    void afterEach() {
        System.out.println("Finalizo el metodo de prueba.");
        testReporter.publishEntry(" Ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName()
                + " con las etiquetas " + testInfo.getTags());
    }

    @Nested
    @Tag("Bank")
    @DisplayName("probando atributos de la cuenta corriente")
    class accountBank {
        @Test
        @DisplayName("el nombre de la cuenta corriente")
        void NameAccountTest() {
            // izd: esperado - der: real
            System.out.println(testInfo.getTags());
            if (testInfo.getTags().contains("Bank")) {
                System.out.println("hacer algo con la etiqueta cuenta");
            }
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
        @DisplayName("Validando credito a cuenta")
        void DebitAccountTest() {
            account.debit(new BigDecimal(100));
            assertNotNull(account.getCredit());
            assertEquals(900, account.getCredit().intValue());
            assertEquals("900.12345", account.getCredit().toPlainString());
        }

        @DisplayName("Validando credito a cuenta")
        @RepeatedTest(value = 5, name = "{displayName} - Repiticiones numero {currentRepetition} de {totalRepetitions}")
        void DebitAccountRepeatTest(RepetitionInfo info) {
            if (info.getCurrentRepetition() == 3 ) {
                System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
            }
            account.debit(new BigDecimal(100));
            assertNotNull(account.getCredit());
            assertEquals(900, account.getCredit().intValue());
            assertEquals("900.12345", account.getCredit().toPlainString());
        }

        @ParameterizedTest(name = "Numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "1000"})
        @DisplayName("Parametrizado 1 - Validando credito a cuenta")
        void DebitAccountParameterizedTest(String amount) {
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getCredit());
            assertTrue(account.getCredit().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "Numero {index} ejecutando con valor {0} - {argumentsWithNames}")
//        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000"})
        @CsvFileSource(resources = "/data.csv")
        @DisplayName("Parametrizado 2 - Validando credito a cuenta")
        void DebitAccountParameterizedCsvTest(String index, String amount) {
            System.out.println(index + " -> " + amount);
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getCredit());
            assertTrue(account.getCredit().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "Numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @MethodSource("amountList")
        @DisplayName("Parametrizado 3 - Validando credito a cuenta")
        void DebitAccountParameterizedMethodTest(String amount) {
            System.out.println("Amount " + amount);
            account.debit(new BigDecimal(amount));
            assertNotNull(account.getCredit());
            assertTrue(account.getCredit().compareTo(BigDecimal.ZERO) > 0);
        }

        static List<String> amountList() {
            return Arrays.asList("100", "200", "300", "500", "700", "1000");
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
    @Tag("Enviroment")
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void enviromentDevTest() {

    }

    @Test
    void imprSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    @Test
    @Tag("Enviroment")
    @EnabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "dev")
    void envDevTest() {
    }

    @Test
    @Tag("Enviroment")
    @EnabledIfEnvironmentVariable(named = "ENVIROMENT", matches = "prod")
    void envProdTest() {
    }

    /* CLASES anidadas usar anotacion @Nested*/
    @Nested
    @Tag("Enviroment")
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

    @Nested
    @Tag("Timeout")
    class TimeOutTest {
        @Test
        @Timeout(1)
        void timeoutTest() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void timeOutTest2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(1000);
        }

        @Test
        void timeOutTest3() {
            assertTimeout(Duration.ofSeconds(5), () -> TimeUnit.MILLISECONDS.sleep(4000));
        }
    }

}
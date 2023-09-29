package guru.qa.niffler.test.grpc;

import guru.qa.grpc.niffler.grpc.*;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

public class NifflerCurrencyGrpcTest extends BaseGrpcTest {

    private static final Map<CurrencyValues, Double> EXPECTED_CURRENCY_RATES = Map.of(
            CurrencyValues.RUB, 0.015,
            CurrencyValues.USD, 1.0,
            CurrencyValues.EUR, 1.08,
            CurrencyValues.KZT, 0.0021
    );

    private CurrencyResponse getAllCurrenciesResponse() {
        return currencyStub.getAllCurrencies(EMPTY);
    }

    private void assertCurrencyRate(Currency currency, Double expectedRate) {
        Assertions.assertEquals(expectedRate, currency.getCurrencyRate());
    }

    @Test
    void getAllCurrenciesTest() {
        CurrencyResponse allCurrencies = getAllCurrenciesResponse();
        List<Currency> currencyList = allCurrencies.getAllCurrenciesList();

        Assertions.assertAll("Currency List",
                () -> Assertions.assertEquals(CurrencyValues.RUB, currencyList.get(0).getCurrency()),
                () -> Assertions.assertEquals(4, allCurrencies.getAllCurrenciesList().size())
        );
    }

    @Test
    void testGetAllCurrenciesValues() {
        CurrencyResponse allCurrencies = getAllCurrenciesResponse();
        List<Currency> currencyList = allCurrencies.getAllCurrenciesList();

        for (Currency currency : currencyList) {
            Double expectedRate = EXPECTED_CURRENCY_RATES.get(currency.getCurrency());
            assertCurrencyRate(currency, expectedRate);
        }
    }

    @Test
    void calculateRateTest() {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(100.00)
                .setSpendCurrency(CurrencyValues.RUB)
                .setDesiredCurrency(CurrencyValues.KZT)
                .build();

        CalculateResponse calculateResponse = currencyStub.calculateRate(cr);
        Assertions.assertEquals(714.29, calculateResponse.getCalculatedAmount());
    }

    @ParameterizedTest
    @CsvSource({
            "USD, EUR, 100.00, 92.59",
            "EUR, USD, 100.00, 108.0",
            "USD, EUR, -100.00, -92.59",
            "EUR, USD, -100.00, -108.0"
    })
    void calculateRateTest(CurrencyValues spendCurrency, CurrencyValues desiredCurrency, double amount, double expectedAmount) {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        CalculateResponse calculateResponse = currencyStub.calculateRate(cr);
        Assertions.assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
    }

    @ParameterizedTest
    @CsvSource({
            "RUB, EUR, 1000.00, 13.89",
            "USD, KZT, 500.00, 238095.24",
            "KZT, RUB, 2000.00, 280.0",
            "EUR, USD, 1000.00, 1080.0"
    })
    void calculateRateTestWithDifferentCurrencies(CurrencyValues spendCurrency, CurrencyValues desiredCurrency, double amount, double expectedAmount) {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(amount)
                .setSpendCurrency(spendCurrency)
                .setDesiredCurrency(desiredCurrency)
                .build();

        CalculateResponse calculateResponse = currencyStub.calculateRate(cr);
        Assertions.assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
    }

    @ParameterizedTest
    @CsvSource({
            "USD, 100.00",
            "EUR, 92.59",
            "KZT, 47619.05"
    })
    void calculateRateTestWithUSDAsSpendCurrency(CurrencyValues desiredCurrency, double expectedAmount) {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(100.00)
                .setSpendCurrency(CurrencyValues.USD)
                .setDesiredCurrency(desiredCurrency)
                .build();

        CalculateResponse calculateResponse = currencyStub.calculateRate(cr);
        Assertions.assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
    }

    @Test
    void calculateRateTestWithUnspecifiedCurrency() {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(100.00)
                .setSpendCurrency(CurrencyValues.UNSPECIFIED)
                .setDesiredCurrency(CurrencyValues.KZT)
                .build();

        Assertions.assertThrows(StatusRuntimeException.class, () -> currencyStub.calculateRate(cr));
    }

    @Test
    void calculateRateTestWithZeroAmount() {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(0)
                .setSpendCurrency(CurrencyValues.EUR)
                .setDesiredCurrency(CurrencyValues.USD)
                .build();

        CalculateResponse calculateResponse = currencyStub.calculateRate(cr);
        Assertions.assertEquals(0, calculateResponse.getCalculatedAmount());
    }

    @Test
    void calculateRateTestWithSameCurrencies() {
        final CalculateRequest cr = CalculateRequest.newBuilder()
                .setAmount(100)
                .setSpendCurrency(CurrencyValues.USD)
                .setDesiredCurrency(CurrencyValues.USD)
                .build();

        CalculateResponse calculateResponse = currencyStub.calculateRate(cr);
        Assertions.assertEquals(100, calculateResponse.getCalculatedAmount());
    }
}

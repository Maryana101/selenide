package ru.netology.selenide;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }


    public String getDate(int cnt) {
        java.time.LocalDate targetDate = java.time.LocalDate.now().plusDays(cnt);
        String date = targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return date;

    }

    public String getDate() {
        java.time.LocalDate targetDate = java.time.LocalDate.now();
        String date = targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        return date;

    }

    @Test
    void shouldSubmitRequest() {
        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id=date] input").setValue(getDate(5));
        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=name] input").setValue("Тест Тестович");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement].checkbox").click();
        $("button.button ").click();
        $("[data-test-id=notification] .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldBe(exactText("Успешно!"));

    }

    @Test
    void shouldNotifyAboutInvalidCity() {
        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id=date] input").setValue(getDate(3));
        $("[data-test-id=city] input").setValue("Город");
        $("[data-test-id=name] input").setValue("Тест Тестович");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement].checkbox").click();
        $("button.button ").click();
        $("[data-test-id=city].input_invalid .input__sub").shouldBe(visible, exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotifyAboutInvalidDate() {

        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id=date] input").setValue(getDate());
        $("[data-test-id=city] input").setValue("Томск");
        $("[data-test-id=name] input").setValue("Тест Тестович");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement].checkbox").click();
        $("button.button ").click();
        $("[data-test-id=date] .input_invalid .input__sub").shouldBe(visible, exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotifyAboutInvalidName() {

        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id=date] input").setValue(getDate(10));
        $("[data-test-id=city] input").setValue("Томск");
        $("[data-test-id=name] input").setValue("User Test");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement].checkbox").click();
        $("button.button ").click();
        $("[data-test-id=name].input_invalid .input__sub").shouldBe(visible, exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotifyAboutInvalidPhone() {

        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id=date] input").setValue(getDate(10));

        $("[data-test-id=city] input").setValue("Томск");
        $("[data-test-id=name] input").setValue("Тест Тестович");
        $("[data-test-id=phone] input").setValue("+7927000000");
        $("[data-test-id=agreement].checkbox").click();
        $("button.button ").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldBe(visible, exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldNotifyAboutUncheckedAgreement() {

        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id=date] input").setValue(getDate(10));

        $("[data-test-id=city] input").setValue("Томск");
        $("[data-test-id=name] input").setValue("Тест Тестович");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("button.button ").click();
        $("[data-test-id=agreement].input_invalid").shouldBe(visible);
    }

    @Test
    void shouldVisibleCityList() {

        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        $("[data-test-id=date] input").setValue(getDate(7));
        $("[data-test-id=name] input").setValue("Тест Тестович");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement].checkbox").click();
        $("[data-test-id=city] input").setValue("То");
        $$(".menu-item").last().shouldBe(visible, exactText("Томс"));
    }

}

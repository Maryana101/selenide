package ru.netology.selenide;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AppCardDeliveryTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }


    public String getDate(int cnt) {
        LocalDate targetDate = LocalDate.now().plusDays(cnt);
        String date = targetDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;

    }

    @Test
    void shouldSubmitRequest() {
        $("[data-test-id=date] input ").sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        String date = getDate(5);
        $("[data-test-id=date] input").setValue(date);
        $("[data-test-id=city] input").setValue("Омск");
        $("[data-test-id=name] input").setValue("Тест Тестович");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement].checkbox").click();
        $("button.button ").click();
        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(matchText(date));

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
        $("[data-test-id=date] input").setValue(getDate(0));
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
    void shouldClickDateInCalendarAndCityInList() {

        // Выбрать из списка город
        $("[data-test-id=city] input ").setValue("То");
        $$(".menu-item").find(exactText("Саратов")).click();
        // String city = $$(".menu-item").first().getText();
        //$("[data-test-id=city] input ").sendKeys(Keys.ARROW_DOWN, Keys.ENTER);

        // Выбрать в календаре дату на 1 год и 1 месяц вперед, 21 число
        $("button.icon-button").click();
        $("[data-step=\"12\"].calendar__arrow_direction_right").click();
        $("[data-step=\"1\"].calendar__arrow_direction_right").click();
        $$(".calendar__day").find(exactText("21")).click();

        // Вычислить получившуюся дату
        LocalDate targetDate = LocalDate.now().plusMonths(13);
        String date = "21." + targetDate.format(DateTimeFormatter.ofPattern("MM.yyyy"));


        // Заполнить тесктовые поля и чекбокс
        $("[data-test-id=name] input").setValue("Ян Тестович");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement].checkbox").click();

        // Забронировать встречу
        $("button.button ").click();


        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(matchText(date));
    }
}

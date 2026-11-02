package ru.netology;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CardDeliveryTest {

    public String generateDate(int days, String pattern) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern(pattern));
    }


    @Test
    void shouldRegisterWithDatePlus4() {
        String planningDate = generateDate(4, "dd.MM.yyyy");

        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                .setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79031111111");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").should(Condition.text("Встреча успешно забронирована на " + planningDate));

    }

    @Test
    void negativeTestInvalidCity() {
        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("123");
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79031111111");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='city'].input_invalid")
                .should(Condition.visible)
                .should(Condition.text("Доставка в выбранный город недоступна"));

    }

    @Test
    void negativeTestInvalidDate() {
        String planningDate = generateDate(0, "dd.MM.yyyy");

        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input")
                .press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE)
                .setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79031111111");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid")
                .should(Condition.visible)
                .should(Condition.text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void negativeTestInvalidName() {
        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='name'] input").setValue("123");
        $("[data-test-id='phone'] input").setValue("+79031111111");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='name'].input_invalid")
                .should(Condition.visible)
                .should(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы"));
    }

    @Test
    void negativeTestNoAgreement() {
        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79031111111");
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='agreement'].input_invalid")
                .should(Condition.visible);
    }

    @Test
    void negativeTestInvalidPhone() {
        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("fff");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid")
                .should(Condition.visible)
                .should(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void negativeTestNoName() {
        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='phone'] input").setValue("+79031111111");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='name'].input_invalid")
                .should(Condition.visible)
                .should(Condition.text("Поле обязательно для заполнения"));
    }


    @Test
    void level2() {
        String planningDate = generateDate(7, "dd.MM.yyyy");
        String findDate = generateDate(7, "d");

        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Се");
        $(byText("Севастополь")).click();
        $("[data-test-id='date'] button").click();
        $$(".calendar__day").find(Condition.text(findDate)).click();
        $("[data-test-id='name'] input").setValue("Иван Петров");
        $("[data-test-id='phone'] input").setValue("+79031111111");
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Забронировать")).click();
        $("[data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(15));
        $(".notification__content").should(Condition.text("Встреча успешно забронирована на " + planningDate));

    }


}

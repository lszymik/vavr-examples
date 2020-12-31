package com.lszymik;


import io.vavr.Tuple;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
public class VavrExamples {

    private final Random random = new Random();

    public static final String DELIMITER_SPACE = " ";

    @Test
    void helloWorld() {
        log.info("Hello World!");
    }

    @Test
    void simpleUseOfTuple() {
        final Tuple3<String, String, String> hello = Tuple.of("Hello", "World!", "You are great!");

        log.info("Hello? " + hello._1);
        log.info(hello.toSeq().mkString(DELIMITER_SPACE));
    }

    @Test
    void letTryIt() {
        final Try<String> result = Try.of(this::computeString);

        final String message = result.recover(x -> Match(x).of(
                Case($(instanceOf(RuntimeException.class)), this::recoverFromException)
        )).getOrElse("Even your recovery failed.");

        log.info(message);
    }

    @Test
    void either() {
        final Either<String, Integer> eitherString = Either.left("error");
        final Either<String, Integer> eitherInteger = Either.right(15);

        log.info(eitherString.fold(l -> l, r -> r).toString());
        log.info(eitherInteger.fold(l -> l, r -> r).toString());
    }

    @Test
    void sumMyList() {
        log.info(List.of(1, 2, 3, 4, 5, 6).sum().toString());
    }

    @Test
    void allForNothing() {
        final Option<String> christmasGift = For(
                box(),
                gift(),
                paper()
        ).yield(
                (box, gift, paper) -> String.format("I bought %s and %s and %s.", box, gift, paper)
        );

        log.info(christmasGift.getOrElse("Sorry, no gift that year."));


        final Option<String> christmasGift2 = For(
                box(),
                noGift(),
                paper()
        ).yield(
                (box, gift, paper) -> String.format("I bought %s and %s and %s.", box, gift, paper)
        );

        log.info(christmasGift2.getOrElse("Sorry, no gift that year."));
    }

    private String recoverFromException(final RuntimeException t) {
        log.error(t.getMessage());
        return "You failed, but you are getting the second chance.";
    }

    private String computeString() {
        if (random.nextBoolean()) {
            return "success";
        } else {
            throw new RuntimeException("You don't have luck today");
        }
    }

    private Option<Box> box() {
        return Some(new Box("box"));
    }

    private Option<Gift> gift() {
        return Some(new Gift("gift"));
    }

    private Option<Gift> noGift() {
        return None();
    }

    private Option<Paper> paper() {
        return Some(new Paper("paper"));
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = false)
    @EqualsAndHashCode
    static class Box {
        private final String box;
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = false)
    @EqualsAndHashCode
    static class Gift {
        private final String gift;
    }

    @AllArgsConstructor
    @ToString(includeFieldNames = false)
    @EqualsAndHashCode
    static class Paper {
        private final String paper;
    }
}

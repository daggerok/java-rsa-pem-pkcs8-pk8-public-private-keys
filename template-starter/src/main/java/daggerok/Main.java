package daggerok;

import io.vavr.control.Try;

import java.util.Optional;

import static java.util.function.Predicate.not;

public class Main {
  public static void main(String[] args) {
    var input = String.join(" ", args);
    var nonEmpty = Optional.of(input)
                           .map(String::strip)
                           .filter(not(String::isBlank))
                           .orElse("Hello, Baby!");
    Try.run(() -> System.out.println(nonEmpty))
       .andFinallyTry(() -> System.out.println("Bye-bye!"));
  }
}

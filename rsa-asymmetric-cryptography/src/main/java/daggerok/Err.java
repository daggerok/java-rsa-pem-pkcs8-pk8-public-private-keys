package daggerok;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Error DRY.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Err {

  public static final Function<String, Supplier<RuntimeException>>
      reThrow = message -> () -> new RuntimeException(message);

  public static final Consumer<Throwable> onFailureSystemOutError =
      throwable -> System.err.println(throwable.getLocalizedMessage());
}

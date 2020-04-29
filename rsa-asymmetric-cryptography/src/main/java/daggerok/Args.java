package daggerok;

import lombok.Value;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Parsing program arguments.
 */
@Value(staticConstructor = "of")
public class Args {

  String[] args;

  public boolean is(String key) {
    Optional.ofNullable(key)
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(() -> new IllegalArgumentException("key may not be null or empty."));
    return Arrays.asList(Objects.requireNonNull(args, "args may not be null."))
                 .contains(key);
  }

  public Optional<String> find(String flag) {
    return Optional.ofNullable(get(flag, null));
  }

  public String get(String flag, String defaultValue) {
    final Function<String, Supplier<RuntimeException>> err = name -> () ->
        new RuntimeException(String.format("%s may not be null or empty.", name));
    var aFlag = Optional.ofNullable(flag)
                        .filter(Predicate.not(String::isBlank))
                        .orElseThrow(err.apply("key"));
    return Arrays.stream(args)
                 .filter(s -> s.startsWith("--") && s.contains("="))
                 .map(s -> s.split("="))
                 .filter(pair -> pair[0].endsWith(aFlag))
                 .findFirst()
                 .map(pair -> pair[1])
                 .orElse(defaultValue);
  }
}

package daggerok;

import lombok.Value;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Parsing program arguments.
 */
@Value(staticConstructor = "of")
public class Args {

  String[] args;

  public boolean has(String command) {
    Optional.ofNullable(command)
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(Err.reThrow.apply("command may not be null or empty"));
    return Arrays.asList(Objects.requireNonNull(args, "args may not be null"))
                 .contains(command);
  }

  public Optional<String> find(String query) {
    return Optional.ofNullable(find(query, null));
  }

  public String find(String query, String defaultValue) {
    Optional.ofNullable(query)
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(Err.reThrow.apply("query may not be null or empty"));
    return Arrays.stream(args)
                 .filter(s -> s.startsWith("--") && s.contains("="))
                 .map(s -> s.split("="))
                 .filter(pair -> pair[0].endsWith(query))
                 .findFirst()
                 .map(pair -> pair[1])
                 .orElse(defaultValue);
  }
}

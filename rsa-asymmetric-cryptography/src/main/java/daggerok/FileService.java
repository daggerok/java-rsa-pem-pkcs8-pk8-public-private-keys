package daggerok;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Working with files.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService {

  public static byte[] readBytes(String filepath) {
    Optional.ofNullable(filepath)
            .filter(Predicate.not(String::isBlank))
            .orElseThrow(Err.reThrow.apply("filepath may not be null or empty"));
    return Try.of(() -> Files.readAllBytes(Path.of(filepath)))
              .onFailure(Err.onFailureSystemOutError)
              .getOrElseThrow(Err.reThrow.apply("cannot read file: " + filepath));
  }

  public static void write(byte[] bytes, String filepath) {
    Objects.requireNonNull(bytes, "bytes array may not be null");
    var file = Optional.ofNullable(filepath)
                       .filter(Predicate.not(String::isBlank))
                       .map(File::new)
                       .orElseThrow(Err.reThrow.apply("filepath may not be null or empty"));
    if (file.getParentFile().mkdirs())
      System.out.printf("%s parent dir created%n", filepath);
    Try
        .run(() -> {
          try (var fos = new FileOutputStream(file)) {
            fos.write(bytes);
          }
        })
        .onFailure(Err.onFailureSystemOutError)
        .getOrElseThrow(Err.reThrow.apply("cannot write bytes into " + filepath));
  }
}

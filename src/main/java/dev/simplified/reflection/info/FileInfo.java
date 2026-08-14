package dev.simplified.reflection.info;

import dev.simplified.annotations.AccessLevel;
import dev.simplified.annotations.EqualsAndHashCode;
import dev.simplified.annotations.Getter;
import dev.simplified.annotations.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Represents a file resource that can be either a class file or any other resource file
 * loadable from the class path.
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class FileInfo {

    @Getter private final @NotNull File file;
    @Getter(AccessLevel.PROTECTED)
    private final @NotNull ClassLoader classLoader;

    @Override
    public @NotNull String toString() {
        return this.getFile().toString();
    }

}
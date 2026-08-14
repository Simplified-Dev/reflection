package dev.simplified.reflection.info;

import dev.simplified.annotations.CallSuper;
import dev.simplified.annotations.EqualsAndHashCode;
import dev.simplified.annotations.Getter;
import dev.simplified.util.StringUtil;
import dev.simplified.util.SystemUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;

/**
 * Represents a class path resource that can be either a class file or any other resource file
 * loadable from the class path.
 */
@EqualsAndHashCode(callSuper = CallSuper.YES)
@Getter
public class ResourceInfo extends FileInfo {

    /**
     * Returns the fully qualified name of the resource. Such as "com/mycomp/foo/bar.txt".
     */
    private final @NotNull String resourceName;

    public ResourceInfo(@NotNull File file, @NotNull String resourceName, @NotNull ClassLoader classLoader) {
        super(file, classLoader);
        this.resourceName = resourceName;
    }

    static @NotNull ResourceInfo of(@NotNull File file, @NotNull String resourceName, @NotNull ClassLoader loader) {
        if (resourceName.endsWith(".class"))
            return new ClassInfo(file, resourceName, loader);
        else
            return new ResourceInfo(file, resourceName, loader);
    }

    /**
     * The file extension without a leading dot (e.g., "png").
     */
    public final @NotNull String getExtension() {
        return StringUtil.getFileExtension(this.getResourceName());
    }

    /**
     * The filename without extension (e.g., "panel" from "emojis/system/panel.png").
     */
    public final @NotNull String getName() {
        String fileName = this.getFileName();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    /**
     * The filename with extension (e.g., "accept.png").
     */
    public final @NotNull String getFileName() {
        String resourceName = this.getResourceName();
        int lastSlash = resourceName.lastIndexOf('/');
        return lastSlash >= 0 ? resourceName.substring(lastSlash + 1) : resourceName;
    }

    /**
     * The directory path without a trailing slash (e.g., "emojis/system").
     */
    public final @NotNull String getPath() {
        String resourceName = this.getResourceName();
        int lastSlash = resourceName.lastIndexOf('/');
        return lastSlash >= 0 ? resourceName.substring(0, lastSlash) : "";
    }

    /**
     * Returns the url identifying the resource.
     *
     * <p>See {@link ClassLoader#getResource}
     *
     * @throws NoSuchElementException if the resource cannot be loaded through the class loader,
     *                                despite physically existing in the class path.
     */
    public final @NotNull URL getUrl() {
        URL url = this.getClassLoader().getResource(this.getResourceName());

        if (url == null)
            throw new NoSuchElementException(this.getResourceName());

        return url;
    }

    public byte[] toBytes() {
        return SystemUtil.readResource(this.getResourceName());
    }

    public @Nullable InputStream toStream() {
        return SystemUtil.getResource(this.getResourceName());
    }

    @Override
    public @NotNull String toString() {
        return this.getResourceName();
    }

}

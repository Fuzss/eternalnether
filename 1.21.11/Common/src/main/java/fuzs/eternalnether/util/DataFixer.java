package fuzs.eternalnether.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fuzs.puzzleslib.api.config.v3.json.GsonCodecHelper;
import fuzs.puzzleslib.impl.PuzzlesLib;
import net.minecraft.nbt.*;
import net.minecraft.util.GsonHelper;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A simple utility for replacing string values in {@code json} and {@code nbt} files.
 * <p>
 * Useful for switching out mod ids, configure directories ideally as:
 * <ul>
 *     <li>{@code inputDirectory}: {@code "../Common/src/main/resources/data"}</li>
 *     <li>{@code outputDirectory}: {@code "../Common/src/generated/resources/data"}</li>
 * </ul>
 */
public abstract class DataFixer<T> {
    public static final DataFixer<?> JSON = new JsonDataFixer();
    public static final DataFixer<?> NBT = new NbtDataFixer();
    static final Collection<DataFixer<?>> ALL = List.of(JSON, NBT);

    private final String fileExtension;

    DataFixer(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    static void getAllFilesRecursive(File file, Consumer<File> fileOutput, Predicate<String> fileFilter) {
        File[] listedFiles = file.listFiles();
        if (listedFiles != null) {
            for (File listedFile : listedFiles) {
                if (listedFile.isDirectory()) {
                    getAllFilesRecursive(listedFile, fileOutput, fileFilter);
                } else if (fileFilter.test(listedFile.getName())) {
                    fileOutput.accept(listedFile);
                }
            }
        }
    }

    public static void updateAll(File inputDirectory, File outputDirectory, String targetValue, String replacementValue) {
        for (DataFixer<?> dataFixer : ALL) {
            dataFixer.update(inputDirectory, outputDirectory, targetValue, replacementValue);
        }
    }

    public final void update(File inputDirectory, File outputDirectory, String targetValue, String replacementValue) {
        List<File> files = new ArrayList<>();
        getAllFilesRecursive(inputDirectory, files::add, (String s) -> s.endsWith("." + this.fileExtension));
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File inputFile = iterator.next();
            File outputFile = outputDirectory.toPath()
                    .resolve(inputDirectory.toPath().relativize(inputFile.toPath()))
                    .toFile();
            outputFile.getParentFile().mkdirs();
            try {
                T t = this.read(inputFile);
                this.write(outputFile, this.process(t, targetValue, replacementValue));
            } catch (IOException exception) {
                PuzzlesLib.LOGGER.warn("Failed handling {}", outputFile, exception);
                iterator.remove();
            }
        }
    }

    public abstract T process(T t, String targetValue, String replacementValue);

    abstract T read(File file) throws IOException;

    abstract void write(File file, T t) throws IOException;

    private static class JsonDataFixer extends DataFixer<JsonElement> {

        JsonDataFixer() {
            super("json");
        }

        @Override
        public JsonElement process(JsonElement jsonElement, String targetValue, String replacementValue) {
            if (jsonElement != null && !jsonElement.isJsonNull()) {
                if (jsonElement.isJsonPrimitive()) {
                    JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                    if (jsonPrimitive.isString()) {
                        if (jsonPrimitive.getAsString().contains(targetValue)) {
                            return new JsonPrimitive(jsonPrimitive.getAsString()
                                    .replace(targetValue, replacementValue));
                        }
                    }
                    return jsonElement;
                } else if (jsonElement.isJsonArray()) {
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        jsonArray.set(i, this.process(jsonArray.get(i), targetValue, replacementValue));
                    }
                } else if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        entry.setValue(this.process(entry.getValue(), targetValue, replacementValue));
                    }
                }
            }
            return jsonElement;
        }

        @Override
        JsonElement read(File file) throws IOException {
            try (FileReader fileReader = new FileReader(file)) {
                return GsonHelper.fromJson(GsonCodecHelper.GSON, fileReader, JsonElement.class);
            }
        }

        @Override
        void write(File file, JsonElement jsonElement) throws IOException {
            try (FileWriter fileWriter = new FileWriter(file)) {
                GsonCodecHelper.GSON.toJson(jsonElement, fileWriter);
            }
        }
    }

    private static class NbtDataFixer extends DataFixer<CompoundTag> {

        NbtDataFixer() {
            super("nbt");
        }

        @Override
        public CompoundTag process(CompoundTag compoundTag, String targetValue, String replacementValue) {
            return this.processTag(compoundTag, targetValue, replacementValue);
        }

        private <T extends Tag> T processTag(T tag, String targetValue, String replacementValue) {
            if (tag != null) {
                if (tag instanceof StringTag(String value)) {
                    if (value.contains(targetValue)) {
                        return (T) StringTag.valueOf(value.replace(targetValue, replacementValue));
                    }
                } else if (tag instanceof ListTag listTag) {
                    for (int i = 0; i < listTag.size(); i++) {
                        listTag.set(i, this.processTag(listTag.get(i), targetValue, replacementValue));
                    }
                } else if (tag instanceof CompoundTag compoundTag) {
                    for (Map.Entry<String, Tag> entry : compoundTag.entrySet()) {
                        entry.setValue(this.processTag(entry.getValue(), targetValue, replacementValue));
                    }
                }
            }
            return tag;
        }

        @Override
        CompoundTag read(File file) throws IOException {
            return NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap());
        }

        @Override
        void write(File file, CompoundTag tag) throws IOException {
            NbtIo.writeCompressed(tag, file.toPath());
        }
    }
}

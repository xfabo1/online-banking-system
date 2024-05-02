package cz.muni.fi.obs.util;

import static lombok.AccessLevel.PRIVATE;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class Resources {

	public static <T> T readResource(String path, Class<T> type) {
		try (var in = Resources.class.getResourceAsStream(path)) {
			return JsonConvertor.reader(type).readValue(in);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static <T> T readResource(String path, TypeReference<T> type) {
		try (var in = Resources.class.getResourceAsStream(path)) {
			return JsonConvertor.reader(type).readValue(in);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

}

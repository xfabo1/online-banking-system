package cz.muni.fi.obs.controller.pagination;

import java.util.List;

import org.springframework.data.domain.Page;

public record PagedResponse<T>(List<T> records, Pagination pagination) {

	public static <T> PagedResponse<T> fromPage(Page<T> page) {
		return new PagedResponse<>(page.getContent(), Pagination.fromPage(page));
	}
}

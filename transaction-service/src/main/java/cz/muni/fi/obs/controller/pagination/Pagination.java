package cz.muni.fi.obs.controller.pagination;

import org.springframework.data.domain.Page;

import lombok.Builder;

@Builder
public record Pagination(
		long totalRecords,
		int totalPages,
		int currentPage,
		int pageSize) {

	public static Pagination fromPage(Page<?> page) {
		return Pagination.builder()
				.totalRecords(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.currentPage(page.getNumber())
				.pageSize(page.getSize())
				.build();
	}

}

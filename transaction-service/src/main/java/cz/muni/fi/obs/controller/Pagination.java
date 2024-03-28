package cz.muni.fi.obs.controller;

import org.springframework.data.domain.Page;

import lombok.Builder;

@Builder
public record Pagination(
		long totalRecords,
		int totalPages,
		int currentPage,
		int pageSize) {

	public static <T> Pagination fromPage(Page<T> page) {
		return Pagination.builder()
				.totalRecords(page.getTotalElements())
				.totalPages(page.getTotalPages())
				.currentPage(page.getNumber())
				.pageSize(page.getSize())
				.build();
	}

}

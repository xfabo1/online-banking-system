package cz.muni.fi.obs.dto;

import java.util.List;

public record PagedResult<T>(List<T> result, Long count, PageRequest request) {

}

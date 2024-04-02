package cz.muni.fi.obs.api;

import lombok.Builder;

@Builder
public record UserUpdateDto(String firstName, String lastName, String phoneNumber, String email) {
}

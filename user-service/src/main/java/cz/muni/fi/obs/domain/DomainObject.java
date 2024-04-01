package cz.muni.fi.obs.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(of = "id")
@Getter
public abstract class DomainObject {
    private final String id = UUID.randomUUID().toString();
}

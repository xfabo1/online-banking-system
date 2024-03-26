package cz.muni.fi.obs.domain;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(of = "id")
public abstract class DomainObject {

    private final String id = UUID.randomUUID().toString();
}

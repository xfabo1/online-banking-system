package cz.muni.fi.obs.data.dbo;

import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(of = "id")
public abstract class Dbo {

    private final String id = UUID.randomUUID().toString();
}

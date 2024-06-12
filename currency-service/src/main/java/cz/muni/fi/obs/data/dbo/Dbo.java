package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@MappedSuperclass
@EqualsAndHashCode(of = "id")
public abstract class Dbo {

    @Id
    private final String id = UUID.randomUUID().toString();
}

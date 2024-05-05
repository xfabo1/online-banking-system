package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "temp_account")
public class TempAccount extends Dbo{
    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String currencyCode;
    @Column(nullable = false, unique = true)
    private String accountId;
}

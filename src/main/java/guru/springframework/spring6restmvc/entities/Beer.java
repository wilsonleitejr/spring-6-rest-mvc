package guru.springframework.spring6restmvc.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beer {

	@Id
	@GeneratedValue(generator = "UUID")
	@UuidGenerator
	@Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
	private UUID id;
	@Column(nullable = false)
	private String beerName;

	private BeerStyle beerStyle;
	private String upc;
	private Integer quantityOnHand;
	private BigDecimal price;
	private LocalDateTime createdDate;
	private LocalDateTime updateDate;
	@Version
	private Integer version;

}

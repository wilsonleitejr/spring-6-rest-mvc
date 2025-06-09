package guru.springframework.spring6restmvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerDTO {

	private UUID id;
	private Integer version;

	@NotNull
	@NotBlank
	private String beerName;

	@NotNull
	private BeerStyle beerStyle;

	@NotNull
	@NotBlank
	private String upc;

	private Integer quantityOnHand;

	@NotNull
	@PositiveOrZero
	private BigDecimal price;

	private LocalDateTime createdDate;
	private LocalDateTime updateDate;
}

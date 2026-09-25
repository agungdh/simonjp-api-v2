package id.my.agungdh.mapper;

import id.my.agungdh.dto.ProductRequest;
import id.my.agungdh.dto.ProductResponse;
import id.my.agungdh.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequest request);
}

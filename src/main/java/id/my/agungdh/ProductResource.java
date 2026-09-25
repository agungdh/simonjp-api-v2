package id.my.agungdh;

import id.my.agungdh.dto.ProductRequest;
import id.my.agungdh.dto.ProductResponse;
import id.my.agungdh.entity.Product;
import id.my.agungdh.mapper.ProductMapper;
import id.my.agungdh.repository.ProductRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductRepository repository;

    @Inject
    ProductMapper mapper;

    @GET
    public List<ProductResponse> list() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GET
    @Path("/{id}")
    public ProductResponse get(@PathParam("id") Long id) {
        Product product = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        return mapper.toResponse(product);
    }

    @POST
    public Response create(ProductRequest request, @Context UriInfo uriInfo) {
        Product product = repository.save(mapper.toEntity(request));
        ProductResponse response = mapper.toResponse(product);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(product.getId().toString())
                .build();

        return Response.created(location).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    public ProductResponse update(@PathParam("id") Long id, ProductRequest request) {
        repository.findById(id)
                .orElseThrow(NotFoundException::new);

        Product updated = mapper.toEntity(request);
        updated.setId(id);
        repository.update(updated);

        return mapper.toResponse(updated);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (!repository.delete(id)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }
}

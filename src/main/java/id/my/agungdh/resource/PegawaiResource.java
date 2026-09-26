package id.my.agungdh.resource;

import id.my.agungdh.dto.PegawaiRequest;
import id.my.agungdh.dto.PegawaiResponse;
import id.my.agungdh.entity.Pegawai;
import id.my.agungdh.mapper.PegawaiMapper;
import id.my.agungdh.repository.PegawaiRepository;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Path("/pegawai")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "pegawai", description = "Pegawai CRUD operations")
@RunOnVirtualThread
public class PegawaiResource {

    @Inject
    PegawaiRepository repository;

    @Inject
    PegawaiMapper mapper;

    @GET
    @Operation(summary = "List all pegawai")
    public List<PegawaiResponse> list() {
        return repository.listAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GET @Path("/{id}")
    @Operation(summary = "Get a pegawai by id")
    @APIResponse(responseCode = "404", description = "Pegawai not found")
    public PegawaiResponse get(@PathParam("id") Long id) {
        Pegawai pegawai = repository.findByIdOptional(id)
                .orElseThrow(NotFoundException::new);
        return mapper.toResponse(pegawai);
    }

    @POST
    @Transactional
    @Operation(summary = "Create a pegawai")
    @APIResponse(responseCode = "201", description = "Pegawai created")
    public Response create(PegawaiRequest request, @Context UriInfo uriInfo) {
        Pegawai pegawai = mapper.toEntity(request);
        repository.persist(pegawai);
        PegawaiResponse response = mapper.toResponse(pegawai);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(pegawai.id.toString()).build();
        return Response.created(location).entity(response).build();
    }

    @PUT @Path("/{id}")
    @Transactional
    @Operation(summary = "Update a pegawai")
    @APIResponse(responseCode = "404", description = "Pegawai not found")
    public PegawaiResponse update(@PathParam("id") Long id, PegawaiRequest request) {
        Pegawai pegawai = repository.findByIdOptional(id)
                .orElseThrow(NotFoundException::new);
        pegawai.nip = request.nip();
        pegawai.nama = request.nama();
        pegawai.jabatan = request.jabatan();
        return mapper.toResponse(pegawai);
    }

    @DELETE @Path("/{id}")
    @Transactional
    @Operation(summary = "Delete a pegawai")
    @APIResponse(responseCode = "204", description = "Pegawai deleted")
    @APIResponse(responseCode = "404", description = "Pegawai not found")
    public Response delete(@PathParam("id") Long id) {
        if (!repository.deleteById(id)) throw new NotFoundException();
        return Response.noContent().build();
    }
}
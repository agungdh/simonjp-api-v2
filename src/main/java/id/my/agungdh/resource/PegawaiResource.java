package id.my.agungdh.resource;

import id.my.agungdh.dto.PegawaiRequest;
import id.my.agungdh.dto.PegawaiResponse;
import id.my.agungdh.entity.Pegawai;
import id.my.agungdh.mapper.PegawaiMapper;
import id.my.agungdh.service.PegawaiService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/pegawai")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "pegawai", description = "Pegawai CRUD operations")
@RunOnVirtualThread
public class PegawaiResource {

    @Inject
    PegawaiService service;

    @Inject
    PegawaiMapper mapper;

    @GET
    @Operation(summary = "List all pegawai")
    public List<PegawaiResponse> list() {
        return service.listAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GET @Path("/{uuid}")
    @Operation(summary = "Get a pegawai by uuid")
    @APIResponse(responseCode = "404", description = "Pegawai not found")
    public PegawaiResponse get(@PathParam("uuid") UUID uuid) {
        Pegawai pegawai = service.findByUuid(uuid)
                .orElseThrow(NotFoundException::new);
        return mapper.toResponse(pegawai);
    }

    @POST
    @Operation(summary = "Create a pegawai")
    @APIResponse(responseCode = "201", description = "Pegawai created")
    public Response create(PegawaiRequest request, @Context UriInfo uriInfo) {
        Pegawai pegawai = service.create(mapper.toEntity(request));
        PegawaiResponse response = mapper.toResponse(pegawai);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(pegawai.getUuid().toString()).build();
        return Response.created(location).entity(response).build();
    }

    @PUT @Path("/{uuid}")
    @Operation(summary = "Update a pegawai")
    @APIResponse(responseCode = "404", description = "Pegawai not found")
    public PegawaiResponse update(@PathParam("uuid") UUID uuid, PegawaiRequest request) {
        Pegawai pegawai = service.update(uuid, mapper.toEntity(request));
        return mapper.toResponse(pegawai);
    }

    @DELETE @Path("/{uuid}")
    @Operation(summary = "Delete a pegawai")
    @APIResponse(responseCode = "204", description = "Pegawai deleted")
    @APIResponse(responseCode = "404", description = "Pegawai not found")
    public Response delete(@PathParam("uuid") UUID uuid) {
        service.delete(uuid);
        return Response.noContent().build();
    }
}

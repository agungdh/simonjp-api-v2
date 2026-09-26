package id.my.agungdh.mapper;

import id.my.agungdh.dto.PegawaiRequest;
import id.my.agungdh.dto.PegawaiResponse;
import id.my.agungdh.entity.Pegawai;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PegawaiMapper {

    PegawaiResponse toResponse(Pegawai pegawai);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Pegawai toEntity(PegawaiRequest request);
}
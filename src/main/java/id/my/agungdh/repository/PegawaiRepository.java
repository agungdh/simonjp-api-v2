package id.my.agungdh.repository;

import id.my.agungdh.entity.Pegawai;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PegawaiRepository implements SoftDeletableRepository<Pegawai, Long> {

    public Optional<Pegawai> findByUuid(UUID uuid) {
        return find("uuid", uuid).firstResultOptional();
    }

    public Optional<Pegawai> findByNip(String nip) {
        return find("nip = ?1", nip).firstResultOptional();
    }

    public List<Pegawai> findAllIncludingDeleted() {
        disableDeletedFilter();
        try {
            return findAll().list();
        } finally {
            enableDeletedFilter();
        }
    }
}

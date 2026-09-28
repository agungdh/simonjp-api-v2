package id.my.agungdh.repository;

import id.my.agungdh.entity.Pegawai;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PegawaiRepository implements PanacheRepositoryBase<Pegawai, Long> {

    public Optional<Pegawai> findByUuid(UUID uuid) {
        return find("uuid", uuid).firstResultOptional();
    }

    public Optional<Pegawai> findByNip(String nip) {
        return find("nip = ?1", nip).firstResultOptional();
    }

    @SuppressWarnings("unchecked")
    public List<Pegawai> findAllIncludingDeleted() {
        return getEntityManager()
                .createNativeQuery("SELECT * FROM pegawai", Pegawai.class)
                .getResultList();
    }
}

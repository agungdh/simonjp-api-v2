package id.my.agungdh.repository;

import id.my.agungdh.entity.Pegawai;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class PegawaiRepository implements PanacheRepositoryBase<Pegawai, Long> {

    public Optional<Pegawai> findByNip(String nip) {
        return find("nip", nip).firstResultOptional();
    }
}
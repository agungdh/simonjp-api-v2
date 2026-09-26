package id.my.agungdh.service;

import id.my.agungdh.entity.Pegawai;
import id.my.agungdh.repository.PegawaiRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PegawaiService {

    @Inject
    PegawaiRepository repository;

    public List<Pegawai> listAll() {
        return repository.find("deletedAt IS NULL").list();
    }

    public Optional<Pegawai> findByUuid(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    @Transactional
    public Pegawai create(Pegawai pegawai) {
        repository.persist(pegawai);
        return pegawai;
    }

    @Transactional
    public Pegawai update(UUID uuid, Pegawai updated) {
        Pegawai existing = repository.findByUuid(uuid)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException());
        existing.nip = updated.nip;
        existing.nama = updated.nama;
        existing.jabatan = updated.jabatan;
        return existing;
    }

    @Transactional
    public void delete(UUID uuid) {
        Pegawai pegawai = repository.findByUuid(uuid)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException());
        pegawai.deletedAt = OffsetDateTime.now();
    }
}

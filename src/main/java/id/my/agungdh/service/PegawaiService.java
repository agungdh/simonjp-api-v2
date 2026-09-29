package id.my.agungdh.service;

import id.my.agungdh.dto.PegawaiRequest;
import id.my.agungdh.entity.Pegawai;
import id.my.agungdh.entity.User;
import id.my.agungdh.repository.PegawaiRepository;
import id.my.agungdh.repository.UserRepository;
import id.my.agungdh.util.PasswordHasher;
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

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordHasher passwordHasher;

    public List<Pegawai> listAll() {
        return repository.findAll().list();
    }

    public Optional<Pegawai> findByUuid(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    @Transactional
    public Pegawai create(Pegawai pegawai, PegawaiRequest request) {
        repository.persist(pegawai);
        User user = new User();
        user.pegawaiId = pegawai.id;
        user.username = pegawai.nip;
        user.password = passwordHasher.hash(request.password());
        userRepository.persist(user);
        return pegawai;
    }

    @Transactional
    public Pegawai update(UUID uuid, Pegawai pegawai, PegawaiRequest request) {
        Pegawai existing = repository.findByUuid(uuid)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException());
        existing.nip = pegawai.nip;
        existing.nama = pegawai.nama;
        existing.jabatan = pegawai.jabatan;
        User user = userRepository.findByPegawaiId(existing.id)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException());
        user.username = pegawai.nip;
        user.password = passwordHasher.hash(request.password());
        return existing;
    }

    @Transactional
    public void delete(UUID uuid) {
        Pegawai pegawai = repository.findByUuid(uuid)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException());
        pegawai.deletedAt = OffsetDateTime.now();
        User user = userRepository.findByPegawaiId(pegawai.id)
                .orElseThrow(() -> new jakarta.ws.rs.NotFoundException());
        user.deletedAt = OffsetDateTime.now();
    }
}

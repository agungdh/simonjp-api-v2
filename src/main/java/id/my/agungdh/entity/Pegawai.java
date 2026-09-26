package id.my.agungdh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pegawai")
public class Pegawai extends BaseEntity {

    @Column(nullable = false, length = 20)
    public String nip;

    @Column(nullable = false, length = 100)
    public String nama;

    @Column(nullable = false, length = 100)
    public String jabatan;
}

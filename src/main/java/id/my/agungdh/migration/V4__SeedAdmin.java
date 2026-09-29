package id.my.agungdh.migration;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.flywaydb.core.api.migration.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class V4__SeedAdmin {

    public void migrate(Context context) throws Exception {
        Connection connection = context.getConnection();
        Argon2 argon2 = Argon2Factory.create();
        String passwordHash = argon2.hash(10, 65536, 1, "admin".toCharArray());

        long pegawaiId;
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO pegawais (uuid, nip, nama, jabatan) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, UUID.randomUUID());
            ps.setString(2, "000000000000000000");
            ps.setString(3, "Administrator");
            ps.setString(4, "System Administrator");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                pegawaiId = rs.getLong(1);
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users (uuid, pegawai_id, username, password) VALUES (?, ?, ?, ?)")) {
            ps.setObject(1, UUID.randomUUID());
            ps.setLong(2, pegawaiId);
            ps.setString(3, "admin");
            ps.setString(4, passwordHash);
            ps.executeUpdate();
        }
    }
}

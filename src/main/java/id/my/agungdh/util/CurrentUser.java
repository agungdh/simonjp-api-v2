package id.my.agungdh.util;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequestScoped
public class CurrentUser {

    private Long id;
    private UUID uuid;
    private String username;
}

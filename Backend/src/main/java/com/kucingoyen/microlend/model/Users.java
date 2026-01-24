package com.kucingoyen.microlend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users extends BaseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 19248192839123L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(unique = true, name = "email")
    private String email;

    @NotNull
    @Column(name = "user_level")
    private String userLevel;

    @NotNull
    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true, name = "daml_party_id")
    private String damlPartyId;

    @Column(unique = true)
    private String googleSub;
}

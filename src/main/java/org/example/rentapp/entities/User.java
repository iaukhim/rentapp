package org.example.rentapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@SecondaryTable(name = "users_info")
@Data
@NoArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Role> roles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(table = "users_info", name = "address_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Address address;

    private String email;

    private String password;

    @Column(name = "phone_number", table = "users_info")
    private String phoneNumber;

    @Column(name = "firstname", table = "users_info")
    private String firstName;

    @Column(name = "lastname", table = "users_info")
    private String lastName;

    @Column(name = "status", table = "users_info")
    private Boolean status;

    @Column(name = "joindate", table = "users_info")
    private LocalDate joindate;

    private void addRole(Role role) {
        roles.add(role);
    }

    private void deleteRole(Role role) {
        roles.remove(role);
    }

}

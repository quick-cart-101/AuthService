package models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private String address;
    private List<Role> roles;
}
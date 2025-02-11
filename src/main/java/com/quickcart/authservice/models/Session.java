package models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Session extends BaseModel {
    private String token;

    @ManyToOne
    private User user;

    private Status status;
}

package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(mappedBy = "tags")
    private List<Project> projects = new ArrayList<>();

    private String name;

}

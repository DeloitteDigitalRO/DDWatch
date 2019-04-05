package com.deloitte.ddwatch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Tag")
@Builder
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(mappedBy = "tags")
    private Set<Project> projects = new HashSet<>();

    @NaturalId
    @Column(name = "name", length = 32)
    private String name;



}

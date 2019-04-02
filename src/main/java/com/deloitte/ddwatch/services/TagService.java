package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;


    public Tag safelyGetByName(String tagName) {
        Optional<Tag> tag = tagRepository.findByName(tagName);
        if(!tag.isPresent()) {
            throw new RuntimeException("No such tag found");
        }
        return tag.get();
    }
}

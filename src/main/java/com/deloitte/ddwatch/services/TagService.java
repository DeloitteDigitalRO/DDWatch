package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.Set;

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

    public void setTags(Project project, Set<Tag> tags) {
        if (ObjectUtils.isEmpty(tags)) {
            return;
        }
        for (Tag tag : tags) {
            Optional<Tag> persistedTag = tagRepository.findByName(tag.getName());
            project.addTag(persistedTag.orElse(tag));
        }
    }
}

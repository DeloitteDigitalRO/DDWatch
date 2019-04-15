package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.repositories.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.Set;

@Service
public class TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagService.class.getCanonicalName());

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

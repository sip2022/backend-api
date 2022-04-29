package com.sip.api.services.impl;

import com.sip.api.domains.resource.Resource;
import com.sip.api.dtos.resource.ResourceCreationDto;
import com.sip.api.dtos.resource.ResourceDto;
import com.sip.api.exceptions.BadRequestException;
import com.sip.api.exceptions.NotFoundException;
import com.sip.api.repositories.ResourceRepository;
import com.sip.api.services.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    @Override
    public Resource findById(String resourceId) {
        return resourceRepository.findById(resourceId).orElseThrow(() -> new NotFoundException("Resource not found"));
    }

    @Override
    public Resource findByName(String resourceName) {
        return resourceRepository.findByName(resourceName).orElseThrow(() -> new NotFoundException("Resource not found"));
    }

    @Override
    public Resource findByUrl(String resourceUrl) {
        return resourceRepository.findByUrl(resourceUrl).orElseThrow(() -> new NotFoundException("Resource not found"));
    }

    @Override
    public Resource createResource(ResourceCreationDto resourceCreationDto) {
        checkExistence(resourceCreationDto.name());
        return resourceRepository.save(Resource.builder()
                .name(resourceCreationDto.name())
                .url(resourceCreationDto.url())
                .build());
    }

    @Override
    public Resource updateResource(ResourceDto resourceDto) {
        Resource resource = findById(resourceDto.id());
        checkExistence(resourceDto.name());
        resource.setName(resourceDto.name());
        resource.setUrl(resourceDto.url());
        return resourceRepository.save(resource);
    }

    @Override
    public void deleteById(String resourceId) {
        if (!resourceRepository.existsById(resourceId)) throw new NotFoundException("Resource not found");
        resourceRepository.deleteById(resourceId);
    }

    @Override
    public void deleteByName(String resourceName) {
        Resource resource = findByName(resourceName);
        resourceRepository.deleteById(resource.getId());
    }

    @Override
    public void deleteByUrl(String resourceUrl) {
        Resource resource = findByUrl(resourceUrl);
        resourceRepository.deleteById(resource.getId());
    }

    @Override
    public boolean existsById(String resourceId) {
        return resourceRepository.existsById(resourceId);
    }

    @Override
    public boolean existsByName(String resourceName) {
        return resourceRepository.existsByName(resourceName);
    }

    @Override
    public boolean existsByUrl(String resourceUrl) {
        return resourceRepository.existsByUrl(resourceUrl);
    }

    private void checkExistence(String name) {
        if (resourceRepository.existsByName(name))
            throw new BadRequestException(String.format("Resource '%s' already exists!", name));
    }
}

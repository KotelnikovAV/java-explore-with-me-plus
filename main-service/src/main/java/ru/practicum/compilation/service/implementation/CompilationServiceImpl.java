package ru.practicum.compilation.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.dto.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = compilationMapper.fromNewCompilationDto(compilationDto);
        List<Long> ids = compilationDto.getEvents();
        if (!CollectionUtils.isEmpty(ids)) {
            compilation.setEvents(eventRepository.findAllByIdIn(ids));
        } else {
            compilation.setEvents(Collections.emptyList());
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " not found"));
        if (!CollectionUtils.isEmpty(request.getEvents())) {
            compilation.setEvents(eventRepository.findAllByIdIn(request.getEvents()));
        }
        if (request.getPinned() != null) compilation.setPinned(request.getPinned());
        if (request.getTitle() != null) compilation.setTitle(request.getTitle());
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(long compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " not found"));
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAllCompilations(Boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from, size);

        if (pinned == null) {
            return compilationMapper.toCompilationDtoList(compilationRepository.findAll(pageRequest).getContent());
        }

        if (pinned) {
            return compilationMapper.toCompilationDtoList(
                    compilationRepository.findAllByPinnedTrue(pageRequest).getContent());
        } else {
            return compilationMapper.toCompilationDtoList(
                    compilationRepository.findAllByPinnedFalse(pageRequest).getContent());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationById(long compId) {
        return compilationMapper.toCompilationDto(compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " not found")));
    }
}

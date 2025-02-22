package com.example.demo.todo.service;

import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.todo.dto.*;
import com.example.demo.todo.entity.Todo;
import com.example.demo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor

public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TodoSaveResponseDto save(Long memberId, TodoSaveRequestDto dto) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalStateException("그런 멤버 없슴")
        );

        Todo todo = new Todo(
                dto.getContent(),
                member
        );
        Todo savedTodo = todoRepository.save(todo);
        return new TodoSaveResponseDto(
                savedTodo.getId(),
                savedTodo.getContent(),
                member.getId(),
                member.getEmail()
        );
    }


    @Transactional(readOnly = true)
    public List<TodoResponseDto> findAll() {
        List<Todo> todos = todoRepository.findAll();
        List<TodoResponseDto> dtos = new ArrayList<>();
        for (Todo todo : todos) {
            TodoResponseDto todoResponseDto = new TodoResponseDto(
                    todo.getId(), todo.getContent()
            );
            dtos.add(todoResponseDto);
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public TodoResponseDto findById(Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalStateException("그런 사람 없음")
        );
        return new TodoResponseDto(todo.getId(), todo.getContent());
    }

    @Transactional
    public TodoUpdateResponseDto update(Long memberId, Long todoId, TodoUpdateRequestDto dto) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalStateException("그런 멤버 없음")
        );

        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalStateException("그런 사람 없음")
        );

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("누구임? 투두작성자 해킹임?");
        }

        todo.update(dto.getContent());
        return new TodoUpdateResponseDto(todo.getId(),
                todo.getContent());
    }

    @Transactional
    public void delete(Long memberId, Long todoId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalStateException("그런 멤버 없음")
        );

        Todo todo = todoRepository.findById(todoId).orElseThrow(
                () -> new IllegalStateException("그런 사람 없음")
        );

        if (!todo.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("누구임? 투두작성자 해킹임?");
        }

         todoRepository.deleteById(todoId);
    }
}

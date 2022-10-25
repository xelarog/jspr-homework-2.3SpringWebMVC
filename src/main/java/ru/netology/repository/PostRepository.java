package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {
    private AtomicLong countId = new AtomicLong();

    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return posts.values().stream().filter(p -> !p.isRemoved()).collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        try {
            if (checkRemovedFlag(id))
                throwNotFoundException();
            return Optional.of(posts.get(id));
        } catch (NullPointerException e) {
            throwNotFoundException();
        }
        return Optional.empty();
    }

    public Optional<Post> save(Post post) {

        long id = post.getId();
        if (id == 0) {
            long idNext = countId.incrementAndGet();
            post.setId(idNext);
            posts.put(idNext, post);
        } else if (haveId(id)) {
            if (checkRemovedFlag(id))
                throwNotFoundException();
            posts.replace(id, post);
            return Optional.of(posts.get(id));
        } else {
            throwNotFoundException();
        }
        return Optional.of(post);
    }

    public void removeById(long id) {
        if (haveId(id)) {
            if (checkRemovedFlag(id))
                throw new NotFoundException();
            posts.get(id).setRemoved(true);
        }
    }

    public boolean haveId(long id) {
        return posts.containsKey(id);
    }

    private void throwNotFoundException() {
        throw new NotFoundException("пост не найден");
    }

    private boolean checkRemovedFlag(long id) {
        return posts.get(id).isRemoved();
    }
}

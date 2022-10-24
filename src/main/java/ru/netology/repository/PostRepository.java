package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private AtomicLong countId = new AtomicLong();

    private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        try {
            return Optional.of(posts.get(id));
        } catch (NullPointerException e) {
            throw new NotFoundException("пост не найден");
        }

    }

    public Optional<Post> save(Post post) {

        long id = post.getId();
        if (id == 0) {
            long idNext = countId.incrementAndGet();
            post.setId(idNext);
            posts.put(idNext, post);
        } else if (hasId(id)) {
            posts.replace(id, post);
            return Optional.of(posts.get(id));
        } else {
            throw new NotFoundException("пост не найден");
        }
        return Optional.of(post);
    }

    public void removeById(long id) {
        posts.remove(id);
    }

    public boolean hasId(long id) {
        return posts.containsKey(id);
    }
}

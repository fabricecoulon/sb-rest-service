package com.example.restservice.singleton;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.example.restservice.model.Entry;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class EntriesSingleton {
    public List<Entry> entries;
    private final AtomicLong entry_counter = new AtomicLong();

    public EntriesSingleton() {
        entries = new ArrayList<Entry>(); //Collections.emptyList();
        Stream.of("user1", "user2").forEach(name -> {
            Entry entry = new Entry();
            entry.setId(entry_counter.incrementAndGet()); 
            entry.setUsername(name);
            entry.setUrl(String.format("http://%s", name));
            //entry.setPassword(String.format("pass_for_%s", name));
            if (name.equals("user1")) {
                entry.setPassword("U2FsdGVkX19etA7fv6vhGvcdygLyibluA4ynkkw6enw=");
            } else if (name.equals("user2")) {
                entry.setPassword("U2FsdGVkX1+BidxTT7UX3fLpaNwB4UlKzTQowZVpH8I=");
            }            
            entries.add(entry);
        });
    }
}

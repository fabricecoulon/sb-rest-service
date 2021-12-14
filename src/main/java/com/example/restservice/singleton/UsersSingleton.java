package com.example.restservice.singleton;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.example.restservice.model.UserFromDB;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class UsersSingleton {
    public List<UserFromDB> users;
    private final AtomicLong userCounter = new AtomicLong();

    public UsersSingleton() {
        users = new ArrayList<UserFromDB>(); //Collections.emptyList();
        Stream.of("user1", "user2").forEach(name -> {
            UserFromDB user = new UserFromDB();
            user.setId(userCounter.incrementAndGet()); 
            user.setUsername(name);
            user.setHashpass(String.format("hashpass_for_%s", name));
            users.add(user);
        });
        UserFromDB myUser = new UserFromDB();
        myUser.setId(userCounter.incrementAndGet());
        myUser.setUsername("fabrice");
        myUser.setHashpass("$2a$10$Rz0aFUvNc9DmX9Bxh75MUu9VCFJz4EzmyN/IRINKix9qw0IF/b/OW");  //1234
        users.add(myUser);
    }
}

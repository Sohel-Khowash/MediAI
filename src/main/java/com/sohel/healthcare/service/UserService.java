package com.sohel.healthcare.service;
import com.sohel.healthcare.dto.UserProfileDto;
import com.sohel.healthcare.entity.Role;
import com.sohel.healthcare.entity.User;
import com.sohel.healthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(UserProfileDto userProfileDto){

        if(userRepository.existsByName(userProfileDto.getName())) {
            throw new RuntimeException("Username is already in use");
        }

        if(userRepository.existsByEmail(userProfileDto.getEmail())){
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setName(userProfileDto.getName());
        user.setEmail(userProfileDto.getEmail());
        user.setPassword(passwordEncoder.encode(userProfileDto.getPassword()));
        user.setRole(Role.ROLE_PATIENT);

        try{
            userRepository.save(user);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "Saved successfully";
    }
}

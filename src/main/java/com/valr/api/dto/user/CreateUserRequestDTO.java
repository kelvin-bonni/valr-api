//package com.valr.api.dto.user;
//
//import com.valr.api.model.user.Role;
//import com.valr.api.model.user.User;
//import lombok.Getter;
//
//@Getter
//public class CreateUserRequestDTO {
//
//    private String email;
//    private String password;
//    private Role role;
//
//    public User toUser(CreateUserRequestDTO requestDTO){
//        User user = new User();
//        user.setEmail(requestDTO.getEmail());
//        user.setPassword(requestDTO.getPassword());
//        user.setRole(requestDTO.getRole());
//
//        return user;
//    }
//}

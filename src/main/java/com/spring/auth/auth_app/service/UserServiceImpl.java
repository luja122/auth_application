package com.spring.auth.auth_app.service;

import com.spring.auth.auth_app.Exception.ResourceNotFoundException;
import com.spring.auth.auth_app.dtos.UserUpdateDto;
import com.spring.auth.auth_app.dtos.UsersDto;
import com.spring.auth.auth_app.helper.UserHelper;
import com.spring.auth.auth_app.model.Users;
import com.spring.auth.auth_app.repo.UserRepo;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<UsersDto> getAllUsers() {
        List<Users>  users =  userRepo.findAll();
        return null;
    }

    @Override
    public UsersDto getUserById(String id)  {
        Users user = userRepo.findByEmail(id).orElseThrow(()->new ResourceNotFoundException("Email not found"));
        return modelMapper.map(user, UsersDto.class) ;
    }

    @Override
    public UsersDto updateUsers(UserUpdateDto userUpdateDto, MultipartFile image, String id) {
        UUID new_id= UserHelper.praseUuid(id);
        Users existingUser= userRepo.findById(new_id).orElseThrow(()->new ResourceNotFoundException("User not Found of UUID"));
        if(userUpdateDto.getName()!=null || userUpdateDto.getName().isBlank()) {
            throw new IllegalArgumentException("");
        }else{
            existingUser.setName(userUpdateDto.getName());
        }
        if(userUpdateDto.getPassword()!=null || userUpdateDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("");
        }else{
            existingUser.setName(userUpdateDto.getName());
        }
        //not ready but in future to implement
//        if (image != null && !image.isEmpty()) {
//            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
//            Path uploadPath = Paths.get("uploads/images/");
//            Files.createDirectories(uploadPath);
//            Path filePath = uploadPath.resolve(fileName);
//            image.transferTo(filePath.toFile());
//
//            existingUser.setImageUrl("/uploads/images/" + fileName);
//        }
        Users user = userRepo.save(existingUser);
        return modelMapper.map(user,UsersDto.class);
    }

    @Override
    public void deleteUser(String id) {
        UUID conv_id = UserHelper.praseUuid(id);
       Users user = userRepo.findById(conv_id).orElseThrow(()->new ResourceNotFoundException("There is not user of that id to delete"));
        userRepo.deleteById(conv_id);

    }
}

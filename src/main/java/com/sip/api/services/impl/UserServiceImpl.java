package com.sip.api.services.impl;

import com.sip.api.domains.role.Role;
import com.sip.api.domains.enums.UserStatus;
import com.sip.api.domains.user.UserConverter;
import com.sip.api.dtos.role.RoleDto;
import com.sip.api.dtos.user.UserCreationDto;
import com.sip.api.dtos.user.UserDniDto;
import com.sip.api.dtos.user.UserPasswordDto;
import com.sip.api.domains.user.User;
import com.sip.api.dtos.user.UserEmailDto;
import com.sip.api.exceptions.BadRequestException;
import com.sip.api.exceptions.NotFoundException;
import com.sip.api.repositories.UserRepository;
import com.sip.api.services.RoleService;
import com.sip.api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User findByDni(UserDniDto userDniDto) {
        return userRepository.findByDni(userDniDto.getDni()).orElseThrow(() -> new NotFoundException("DNI not found"));
    }

    @Override
    public User findByEmail(UserEmailDto userEmailDto) {
        return userRepository.findByEmail(userEmailDto.getEmail()).orElseThrow(() -> new NotFoundException("Email not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws NotFoundException {
        return findByEmail(new UserEmailDto(email));
    }

    @Override
    public User createUser(UserCreationDto userCreationDto) {
        checkUserRules(userCreationDto.getDni(), userCreationDto.getEmail(), userCreationDto.getPassword(), userCreationDto.getRoles());
        User user = UserConverter.dtoToEntity(userCreationDto);
        // Fetch roles by name
        Set<Role> roles = userCreationDto.getRoles().stream().map(role -> roleService.findByName(role.getName())).collect(Collectors.toSet());
        user.setRoles(roles);
        // Created inactive by default until user activates it by mail
        user.setStatus(UserStatus.INACTIVE);
        user.setPassword(passwordEncoder.encode(userCreationDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateEmail(String userId, UserEmailDto userEmailDto) {
        validateEmail(userEmailDto.getEmail());
        User user = findById(userId);
        user.setEmail(userEmailDto.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(String userId, UserPasswordDto userPasswordDto) {
        validatePassword(userPasswordDto.getPassword());
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(userPasswordDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User activateUser(String userId) {
        User user = findById(userId);
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public void deactivateUser(String userId) {
        User user = findById(userId);
        user.setStatus(UserStatus.DEACTIVATED);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }

    private void checkUserRules(Integer dni, String email, String password, Set<RoleDto > roles) {
        validateDni(dni);
        validateEmail(email);
        validatePassword(password);
        validateRoles(roles);
    }

    private Set<Role> validateRoles(Set<RoleDto> roles) {
        if(roles == null || roles.isEmpty()) throw new BadRequestException("Roles are required");
        return roles.stream().map(role -> roleService.findByName(role.getName())).collect(Collectors.toSet());
    }

    private Integer validateDni(Integer dni) {
        if ((dni != 0) && userRepository.existsByDni(dni))
            throw new BadRequestException("DNI already in use");
        return dni;
    }

    private String validatePassword(String password) {
        if (password.length() < 8) throw new BadRequestException("Password smaller than 8");
        return password;
    }

    private String validateEmail(String email) {
        if ((!email.isEmpty()) && userRepository.existsByEmail(email))
            throw new BadRequestException("Email already in use");
        return email;
    }
}

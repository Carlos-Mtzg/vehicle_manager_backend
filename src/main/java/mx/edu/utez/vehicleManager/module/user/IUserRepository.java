package mx.edu.utez.vehicleManager.module.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.utez.vehicleManager.module.employee.EmployeeModel;

public interface IUserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmployee(EmployeeModel employee);

}
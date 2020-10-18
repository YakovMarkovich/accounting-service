package propets.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import propets.entities.User;


public interface UserRepository extends MongoRepository<User, String> {



	User findByUserLogin(String userName);

}

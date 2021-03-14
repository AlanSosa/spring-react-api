package net.alansosa.backendcursojava;

import net.alansosa.backendcursojava.models.responses.UserRest;
import net.alansosa.backendcursojava.models.shared.dto.UserDto;
import net.alansosa.backendcursojava.security.AppProperties;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
// This bean enables the @CreateDate annotation in the PostEntity to work and any
// other "automatic" fields needed.
@EnableJpaAuditing
public class BackendcursojavaApplication {

	public static void main(String[] args) {

		SpringApplication.run(BackendcursojavaApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder BcryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	//Return SpringApplicationContext as @Bean so it can be accessed by whoever needs it.
	@Bean
	public SpringApplicationContext springApplicationContext(){
		return new SpringApplicationContext();
	}


	/*
	Even though we're using dependency injection in the class. Take that the @Autowired is only
	used for @Beans made with Spring. This bean won't be used by a SPRING bean so we
	make it a normal @Bean because the class that's going to call this object is not a bean.
	* */
	@Bean(name = "AppProperties")
	public AppProperties getAppProperties(){
		return new AppProperties();
	}

	@Bean
	public ModelMapper modelMapper(){
		ModelMapper mapper = new ModelMapper();
		//This configures the mapper to map the fields only that have the same name or relation.
		//Because when mapping from PostDto to PostRest the:
		// boolean expired = false;
		// it only exists in PostRest and not in PostDto
		mapper.getConfiguration()
				.setMatchingStrategy(MatchingStrategies.STRICT);

		/*
		This one is required when we map from PostDto to PostRest.
		Because PostRest contains a field like below:
		private List<PostRest> posts;
		When modelMapper is reading the fields.
		When mapping the PostRest it will go to PostRest class that contains this other field:
		private UserRest user;
		This will make the mapper to stuck in an infinite loop because in UserController.getPosts method
		we mapped a PostDto to Post Rest causing the following behavior.

		PostDto -> maps the list to PostRest -> PostRest contains an UserRest user -> mapper
		reads the <PostRest>list from PostRest -> then goes to PostRest and reads UserRest ->
		UserRest contains <PostRest>list and will return to PostRest and remap again.

		so we need to ignore this field when the mapper is reading UserDto to UserRest.class

		Honestly I made an effort to explain this line.
		Check chapter 74 in the course to understand.

		The thing is that we're ignoring the field setPosts when mapping from UserDto to UserRest
		 */
		mapper.typeMap(UserDto.class, UserRest.class).addMappings( m -> m.skip(UserRest::setPosts));
		return mapper;
	}
}

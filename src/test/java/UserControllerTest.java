import com.fasterxml.jackson.databind.ObjectMapper;
import com.suncoder.resume.user.User;
import com.suncoder.resume.user.UserController;
import com.suncoder.resume.user.UserResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserController.class)
public class UserControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void addUserTest() throws Exception{
		//test for two cases
		User user = new User();
		user.setUserId(123456L);
		user.setAccountName("XXXXXXX");
		user.setRole("admin");

		User user1 = new User();
		user.setUserId(123456L);
		user1.setAccountName("XXXXXXX");
		user1.setRole("user");


		String jsonString = objectMapper.writeValueAsString(user);
		String userString = Base64.getEncoder().encodeToString(jsonString.getBytes());

		String jsonString1 = objectMapper.writeValueAsString(user1);
		String userString1 = Base64.getEncoder().encodeToString(jsonString1.getBytes());

		UserResource userResource = new UserResource();
		userResource.setUserId(123456L);
		List<String> resources = new ArrayList<>();
		resources.add("resource A");
		resources.add("resource B");
		resources.add("resource C");
		userResource.setEndpoint(resources);
		String contentString = objectMapper.writeValueAsString(userResource);
		this.mockMvc.perform(post("/admin/addUser")
				.header("user",userString)
				.param("userResource",contentString))
				.andExpect(status().isOk())
				.andDo(print()).andReturn()
				.getResponse().getContentAsString().equals("success");

		this.mockMvc.perform(post("/admin/addUser")
				.header("user",userString1)
				.param("userResource",contentString))
				.andExpect(status().isOk())
				.andDo(print()).andReturn()
				.getResponse().getContentAsString().equals("no access to this endpoint");


	}

	@Test
	public void verifyResourceTest() throws Exception {
		Long userId = 123457L;
		//test for two cases
		String resource = "resource A";//have access
		String resource1 = "resource D";//have no access
		User user = new User();
		user.setUserId(123456L);
		user.setAccountName("XXXXXXX");
		user.setRole("admin");

		String jsonString = objectMapper.writeValueAsString(user);
		String userString = Base64.getEncoder().encodeToString(jsonString.getBytes());
		this.mockMvc.perform(post("/user/resource")
				.header("user",userString)
				.param("userId" , userId.toString())
				.param("resource", resource))
				.andExpect(status().isOk())
				.andDo(print()).andReturn()
				.getResponse().getContentAsString().equals("success");

		this.mockMvc.perform(post("/user/resource")
				.header("user",userString)
				.param("userId" , userId.toString())
				.param("resource", resource1))
				.andExpect(status().isOk())
				.andDo(print()).andReturn()
				.getResponse().getContentAsString().equals("failure");

	}
}
